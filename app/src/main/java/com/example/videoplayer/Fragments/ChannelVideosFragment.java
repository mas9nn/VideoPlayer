package com.example.videoplayer.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.videoplayer.Adapters.MainPageAdapter;
import com.example.videoplayer.Common.Common;
import com.example.videoplayer.Interfaces.ItemSelecListener;
import com.example.videoplayer.Activities.MainActivity;
import com.example.videoplayer.Models.MainPageItems;
import com.example.videoplayer.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ChannelVideosFragment extends Fragment implements ItemSelecListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private ArrayList<MainPageItems> items = new ArrayList<>();
    private MainPageAdapter adapter;
    private ShimmerFrameLayout shimmer;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int count = 0;
    private SharedPreferences pref;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_main, null);
        ItemSelecListener itemSelecListener = this;
        pref = Objects.requireNonNull(getActivity()).getApplicationContext().getSharedPreferences("MyPref", 0);
        shimmer = (ShimmerFrameLayout) v.findViewById(R.id.shimmer_view_container);
        shimmer.startShimmer();
        recyclerView = v.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout = v.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        adapter = new MainPageAdapter(items, getContext(), itemSelecListener);
        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                try {
                    sendWorkPostRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mSwipeRefreshLayout != null & count == 0) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    shimmer.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    count++;
                }

                // TODO Fetching data from server

            }
        });
        //Restore the fragment's state here
        return v;
    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onItemSelectedListener(View view, int position) {
        try {
            ((MainActivity) Objects.requireNonNull(getActivity())).changePostion();
            ((MainActivity) getActivity()).MaximizePanel(items.get(position).getUrl(), items, position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void sendWorkPostRequest() throws JSONException {
        items.clear();
        String requestUrl = "https://video.orzu.org/api/v1.0/?type=get_videos_by_channel&limit=30&channel_id=" + Common.channelId;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    JSONArray data = j.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        try {
                            JSONObject object = data.getJSONObject(i);
                            JSONObject owner = object.getJSONObject("owner");
                            MainPageItems pageItems = new MainPageItems();
                            pageItems.setPreview_image(object.getString("thumbnail"));
                            pageItems.setChannel_name(owner.getString("first_name"));
                            pageItems.setDays(object.getString("time_ago"));
                            pageItems.setDuration(object.getString("duration"));
                            pageItems.setName(object.getString("title"));
                            pageItems.setViews(object.getString("views"));
                            pageItems.setUrl(object.getString("video_location"));
                            pageItems.setId(object.getString("video_id"));
                            if (!items.contains(pageItems)) {
                                items.add(0, pageItems);
                            }
                            adapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setRefreshing(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    shimmer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                mSwipeRefreshLayout.setRefreshing(false);
                shimmer.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                try {
                    sendWorkPostRequest();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("server_key", "e39111734a4e6a21dd442887dd5112c8");
                postMap.put("s", pref.getString("session", null));
                postMap.put("user_id", pref.getString("userId", null));
                //..... Add as many key value pairs in the map as necessary for your request
                return postMap;
            }
        };
//make the request to your server as indicated in your request url
        Volley.newRequestQueue(Objects.requireNonNull(getContext())).add(stringRequest);
    }

    @Override
    public void onRefresh() {
        try {
            mSwipeRefreshLayout.setRefreshing(false);
            shimmer.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            count++;
            items.clear();
            sendWorkPostRequest();
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
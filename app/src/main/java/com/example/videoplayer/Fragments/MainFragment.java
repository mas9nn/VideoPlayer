package com.example.videoplayer.Fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.example.videoplayer.Interfaces.ItemSelecListener;
import com.example.videoplayer.MainActivity;
import com.example.videoplayer.Models.MainPageItems;
import com.example.videoplayer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainFragment extends Fragment implements ItemSelecListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    ArrayList<MainPageItems> items = new ArrayList<>();
    ItemSelecListener itemSelecListener;
    MainPageAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    int count = 0;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //Restore the fragment's state here

        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, null);
        itemSelecListener = this;

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

                if (mSwipeRefreshLayout != null&count==0) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    count++;
                }

                // TODO Fetching data from server
                try {
                    sendWorkPostRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Log.d("asdasd","save");
        if (savedInstanceState != null) {
            //Restore the fragment's state here

        }
        return v;
    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onItemSelectedListener(View view, int position) {
        try {
            ((MainActivity) getActivity()).changePostion();
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
        Log.d("asdasd","assda");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("asdasd","pau");
        super.onPause();
    }

    private void sendWorkPostRequest() throws JSONException {
        String requestUrl = "https://video.orzu.org/api/v1.0/?type=get_videos&limit=5"+count;
        Log.d("Response", requestUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Respose", "" + response); //the response contains the result from the server, a json string or any other object returned by your server
                try {
                    JSONObject j = new JSONObject(response);
                    JSONObject data = j.getJSONObject("data");
                    JSONArray featured = data.getJSONArray("featured");
                    for (int i = 0; i < featured.length(); i++) {
                        try {
                            Log.d("Response", featured.get(i) + "");
                            JSONObject object = featured.getJSONObject(i);
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
                                items.add(pageItems);
                            }
                            mSwipeRefreshLayout.setRefreshing(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("server_key", "e39111734a4e6a21dd442887dd5112c8");
                //..... Add as many key value pairs in the map as necessary for your request
                return postMap;
            }
        };
//make the request to your server as indicated in your request url
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    @Override
    public void onRefresh() {
        try {
            count++;
            sendWorkPostRequest();
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
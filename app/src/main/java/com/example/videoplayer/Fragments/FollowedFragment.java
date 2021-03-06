package com.example.videoplayer.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.videoplayer.Adapters.FollowedAdapter;
import com.example.videoplayer.Adapters.MainPageAdapter;
import com.example.videoplayer.Interfaces.ItemSelecListener;
import com.example.videoplayer.Activities.MainActivity;
import com.example.videoplayer.Models.FollowedItems;
import com.example.videoplayer.Models.MainPageItems;
import com.example.videoplayer.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FollowedFragment extends Fragment implements ItemSelecListener, FollowedAdapter.ItemClickListener {
    private SharedPreferences pref;
    private RecyclerView logos, videos;
    private List<FollowedItems> list_logos = new ArrayList<>();
    private ItemSelecListener itemSelection;
    private MainPageAdapter adapter;
    private FollowedAdapter logo_adapter;
    private List<MainPageItems> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_following, null);
        pref = Objects.requireNonNull(getActivity()).getApplicationContext().getSharedPreferences("MyPref", 0);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        ItemSelecListener itemSelecListener = this;
        itemSelection = this;
        logos = v.findViewById(R.id.channels_logo);
        videos = v.findViewById(R.id.videos);
        logos.setLayoutManager(layoutManager);
        videos.setLayoutManager(new LinearLayoutManager(getContext()));
        getSubscribedChannels();
        logo_adapter = new FollowedAdapter(getContext(), list_logos);

        logos.setAdapter(logo_adapter);
        logo_adapter.setClickListener(this);
        return v;
    }

    @Override
    public void onItemSelectedListener(View view, int position) {
        try {
            ((MainActivity) Objects.requireNonNull(getActivity())).MaximizePanel(items.get(position).getUrl(), items, position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {

    }

    private void getSubscribedChannels() {
        list_logos.clear();
        String requestUrl = "https://video.orzu.org/api/v1.0/?type=get_subscriptions&channel=1";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    JSONArray data = j.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject channels = data.getJSONObject(i);
                        String id = channels.getString("id");
                        String logo = channels.getString("avatar");
                        String title = channels.getString("first_name");
                        list_logos.add(new FollowedItems(logo, id, title));
                        Log.wtf("idebana", id);
                        if (id != null) {
                            getVideos(id);
                        }
                    }
                    logo_adapter = new FollowedAdapter(getContext(), list_logos);
                    logos.setAdapter(logo_adapter);
                    logo_adapter.setClickListener(new FollowedAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ((MainActivity) Objects.requireNonNull(getActivity())).changePostion();
                            ((MainActivity) getActivity()).openChannel(list_logos.get(position).getTitle(), list_logos.get(position).getId());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
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

    private void getVideos(String id) {
        items.clear();
        String requestUrl = "https://video.orzu.org/api/v1.0/?type=get_videos_by_channel&channel_id=" + id;
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
                            items.add(pageItems);
                            adapter = new MainPageAdapter(items, getContext(), itemSelection);
                            videos.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
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
                getVideos(id);
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
        Volley.newRequestQueue(Objects.requireNonNull(getContext())).add(stringRequest);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("asd", "asd");
    }
}
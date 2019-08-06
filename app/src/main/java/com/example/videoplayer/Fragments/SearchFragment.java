package com.example.videoplayer.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
import com.example.videoplayer.Adapters.ListViewAdapter;
import com.example.videoplayer.Adapters.PanelAdapter;
import com.example.videoplayer.Interfaces.ItemSelecListener;
import com.example.videoplayer.MainActivity;
import com.example.videoplayer.Models.MainPageItems;
import com.example.videoplayer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchFragment extends Fragment implements ItemSelecListener {

    ListView listView;
    RecyclerView recyclerView;
    List<String> choices = new ArrayList<>();
    ListViewAdapter listViewAdapter;
    ItemSelecListener itemSelecListenerl;
    List<MainPageItems> items = new ArrayList<>();
    PanelAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, null);
        listView = v.findViewById(R.id.listview);

        recyclerView = v.findViewById(R.id.videos_in_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setDivider(null);
        listView.setDividerHeight(0);
        itemSelecListenerl = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getVideos(choices.get(i));
                listView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        return v;
    }

    public void getVideos(String query) {
        items.clear();

        String requestUrl = "https://video.orzu.org/api/v1.0/?type=search_videos&keyword=" + query;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    JSONArray data = j.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        try {
                            Log.d("Response", data.get(i) + "");
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
                            adapter = new PanelAdapter(items, getContext(), itemSelecListenerl);
                            recyclerView.setAdapter(adapter);
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

    public void setVisible(int visibility){
        if(visibility == View.GONE){
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.GONE);
        }
        listView.setVisibility(visibility);
    }
    public void getChoices(String query) {
        choices.clear();
        if (listViewAdapter!=null){
            listViewAdapter.notifyDataSetChanged();
        }
        String requestUrl = "https://video.orzu.org/api/v1.0/?type=search_videos&keyword=" + query;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    JSONArray data = j.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        try {
                            Log.d("Response", data.get(i) + "");
                            JSONObject object = data.getJSONObject(i);
                            choices.add(object.getString("title"));
                            listViewAdapter = new ListViewAdapter(getContext(), choices);
                            listView.setAdapter(listViewAdapter);

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
    public void onItemSelectedListener(View view, int position) {
        try {
            ((MainActivity)getActivity()).changePostion();
            ((MainActivity)getActivity()).MaximizePanel(items.get(position).getUrl(),items,position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
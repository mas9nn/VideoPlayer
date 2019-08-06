package com.example.videoplayer.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.videoplayer.Adapters.CategoriesAdapter;
import com.example.videoplayer.Adapters.MainPageAdapter;
import com.example.videoplayer.Interfaces.ItemSelecListener;
import com.example.videoplayer.MainActivity;
import com.example.videoplayer.Models.CategoryItem;
import com.example.videoplayer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CategoryFragment extends Fragment implements ItemSelecListener {
    SharedPreferences pref;
    RecyclerView categoriesRec;
    CategoriesAdapter adapter;
    List<CategoryItem> items = new ArrayList<>();
    ItemSelecListener itemSelecListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_fragmnet, null);
        itemSelecListener = this;
        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        categoriesRec = v.findViewById(R.id.categories);
        categoriesRec.setLayoutManager(new GridLayoutManager(getContext(), 3));
        getCategories();
        return v;
    }

    public void getCategories() {
        String requestUrl = "https://video.orzu.org/api/v1.0/?type=get_settings";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    JSONObject data = j.getJSONObject("data");
                    JSONObject categories = data.getJSONObject("categories");
                    Iterator<String> iter = categories.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        String name = null;
                        try {
                            name = categories.getString(key);
                        } catch (JSONException e) {
                            // Something went wrong!
                        }
                        items.add(new CategoryItem(name, key, R.drawable.ic_tune_black_24dp));
                    }

                    adapter = new CategoriesAdapter(items, getContext(), itemSelecListener);
                    categoriesRec.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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
                postMap.put("server_key", "e39111734a4e6a21dd442887dd5112c8");
                postMap.put("s", pref.getString("session", null));
                postMap.put("user_id", pref.getString("userId", null));
                //..... Add as many key value pairs in the map as necessary for your request
                return postMap;
            }
        };
//make the request to your server as indicated in your request url
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    @Override
    public void onItemSelectedListener(View view, int position) {

        ((MainActivity)getActivity()).openCategory(items.get(position).getId(),items.get(position).getName());
    }

    @Override
    public void onClick(View view) {

    }
}
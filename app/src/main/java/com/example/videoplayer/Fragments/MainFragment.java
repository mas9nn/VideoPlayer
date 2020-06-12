package com.example.videoplayer.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.videoplayer.Adapters.MainPageAdapter;
import com.example.videoplayer.Adapters.MediaRecyclerAdapter;
import com.example.videoplayer.Common.Common;
import com.example.videoplayer.Interfaces.ItemSelecListener;
import com.example.videoplayer.Activities.MainActivity;
import com.example.videoplayer.Layouts.ExoPlayerRecyclerView;
import com.example.videoplayer.Models.MainPageItems;
import com.example.videoplayer.Models.Video;
import com.example.videoplayer.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainFragment extends Fragment implements ItemSelecListener, SwipeRefreshLayout.OnRefreshListener, MediaRecyclerAdapter.ItemClickListener {
    private ExoPlayerRecyclerView recyclerView;
    private ArrayList<MainPageItems> items = new ArrayList<>();
    private MediaRecyclerAdapter adapter;
    private boolean firstTime = true;
    private ShimmerFrameLayout shimmer;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int count = 0;
    private SharedPreferences pref;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //Restore the fragment's state here

        }
        super.onActivityCreated(savedInstanceState);
    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions();

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_main, null);
        pref = Objects.requireNonNull(getActivity()).getApplicationContext().getSharedPreferences("MyPref", 0);
        Log.wtf("postiontion", items.size() + "");
        shimmer = (ShimmerFrameLayout) v.findViewById(R.id.shimmer_view_container);
        shimmer.startShimmer();
        getDataFirebase();
        recyclerView = v.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout = v.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        adapter = new MediaRecyclerAdapter(getContext(), items, initGlide(), this);
        items.clear();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.isPlayVideo(pref.getBoolean("main_autoplay", false));
        if (firstTime) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.playVideo(false);
                }
            });
            firstTime = false;
        }
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                try {
                    getDataFirebase();
                } catch (Exception e) {
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
        return v;
    }

    public void getDataFirebase() {

        db.collection("Videos").document("1").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MainPageItems itemss = documentSnapshot.toObject(MainPageItems.class);
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                items.add(itemss);
                recyclerView.setMediaObjects(items);
                shimmer.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.wtf("asda", e.getMessage());
            }
        });
    }

    @Override
    public void onStop() {
        recyclerView.onPausePlayer();
        recyclerView.isPlayVideo(false);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.wtf("mainfragment", "destroyed");
        super.onDestroy();
    }

    @Override
    public void onItemSelectedListener(View view, int position) {
        try {
            ((MainActivity) Objects.requireNonNull(getActivity())).changePostion();
            Common.MainFragment = true;
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
        String requestUrl = "https://video.orzu.org/api/v1.0/?type=get_videos&limit=5" + count;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    JSONObject data = j.getJSONObject("data");
                    JSONArray featured = data.getJSONArray("featured");
                    for (int i = 0; i < featured.length(); i++) {
                        try {
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
                                items.add(0, pageItems);
                            }
                            adapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setRefreshing(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    recyclerView.setMediaObjects(items);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    public void onRefresh() {
        try {
            mSwipeRefreshLayout.setRefreshing(false);
            shimmer.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            count++;
            items.clear();
            //sendWorkPostRequest();
            getDataFirebase();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playVideo(boolean b) {
        recyclerView.isPlayVideo(b);
        if (!b) {
            recyclerView.onPausePlayer();
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        Log.wtf("clicked", "new recycler");
        ((MainActivity) Objects.requireNonNull(getActivity())).changePostion();
        Common.MainFragment = true;
        try {
            ((MainActivity) getActivity()).MaximizePanel(items.get(position).getUrl(), items, position);
            recyclerView.onPausePlayer();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
package com.example.videoplayer;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.videoplayer.Adapters.PanelAdapter;
import com.example.videoplayer.Draggable.DraggableListener;
import com.example.videoplayer.Draggable.DraggableView;
import com.example.videoplayer.Interfaces.ItemSelecListener;
import com.example.videoplayer.Models.MainPageItems;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;

import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements View.OnTouchListener, ItemSelecListener {

    TextView name, views, likes, dislikes, channel_name, followers, date, name_of_video_bottom, name_of_category;

    ItemSelecListener itemSelecListener;

    DraggableView panel;

    RecyclerView recyclerInPanel;

    List<MainPageItems> items = new ArrayList<>();
    DefaultTrackSelector trackSelector;
    String waterUrl = "https://video.orzu.org/upload/videos/2019/06/1DZjZiEBOEfGfcAslyHh_25_38aa4abb775fd1e9d30afdbf21561613_video_240p_converted.mp4";
    PanelAdapter panelAdapter;
    PanelAdapter adapter;
    private static final String APP_NAME = MainActivity.class.getSimpleName();
    private SimpleExoPlayer player;
    PlayerView exoPlayerView;
    MediaSource videoSource;
    ConstraintLayout bottom_constraint;
    ConcatenatingMediaSource mediaSource = new ConcatenatingMediaSource();
    ImageView search;
    RecyclerView recyclerView;
    @Override
    public void onBackPressed() {
        int orientation = getResources().getConfiguration().orientation;
        if (panel.isMaximized()) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            } else {
                panel.minimize();
            }
        } else if (panel.isMinimized()) {
            panel.closeToLeft();
        } else {
            super.onBackPressed();
        }
    }


    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        recyclerInPanel = findViewById(R.id.recycler_in_panel);
        recyclerInPanel.setLayoutManager(new LinearLayoutManager(this));
        recyclerInPanel.setNestedScrollingEnabled(false);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        name = findViewById(R.id.discription);
        views = findViewById(R.id.count_of_views);
        likes = findViewById(R.id.num_of_like);
        dislikes = findViewById(R.id.num_of_dis);
        channel_name = findViewById(R.id.name_of_channel);
        followers = findViewById(R.id.followers);
        date = findViewById(R.id.date);
        name_of_video_bottom = findViewById(R.id.name_of_video_bottom);
        name_of_category = findViewById(R.id.name_of_category);
        progressBar = findViewById(R.id.bar);
        bottom_constraint = findViewById(R.id.bottom_constraint);
        search = findViewById(R.id.search_button);
        itemSelecListener = this;

        panel = findViewById(R.id.draggable_view);
        panel.setTopViewHeight(600);
        initPlayer();
        panel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
                //exoPlayerView.showController();
                panel.setClickToMaximizeEnabled(false);
                Log.d("maxim", "max");
                // constra.setEnabled(true);
                //player.setPlayWhenReady(true);
            }

            @Override
            public void onMinimized() {
                exoPlayerView.hideController();
                panel.setClickToMaximizeEnabled(true);
                panel.setTopViewHeight(600);

                Log.d("asd", panel.getTopView() + "");
            }

            @Override
            public void onClosedToLeft() {
                player.setPlayWhenReady(false);
                panel.setVisibility(View.GONE);
            }

            @Override
            public void onClosedToRight() {
                player.setPlayWhenReady(false);
                panel.setVisibility(View.GONE);
            }
        });


        ImageButton button = exoPlayerView.findViewById(R.id.fullscreen);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                }
                if (android.provider.Settings.System.getInt(getContentResolver(),
                        Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }

            }
        });
        ImageButton minim = exoPlayerView.findViewById(R.id.minimize);
        minim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        panel.minimize();
                    }
                }, 100);
            }
        });
        final DefaultTimeBar timeBar = exoPlayerView.findViewById(R.id.exo_progress);
        timeBar.addListener(new TimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(TimeBar timeBar, long position) {
                Log.d("dddd", "OnStart");
                panel.disable(false);
                player.seekTo(position);
            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                player.seekTo(position);
                player.setPlayWhenReady(true);
                panel.disable(false);
                Log.d("dddd", "onmove");
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                if (Configuration.ORIENTATION_LANDSCAPE == SearchActivity.this.getResources().getConfiguration().orientation) {
                    panel.setEnabled(false);
                } else {
                    panel.setEnabled(true);
                }
                player.setPlayWhenReady(true);
                Log.d("dddd", "onStop");
            }
        });
        try {
            requstByText("пила");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void MaximizePanel(String url, List<MainPageItems> items, int position) throws JSONException {
        this.items = items;
        progressBar.setVisibility(View.VISIBLE);
        bottom_constraint.setVisibility(View.INVISIBLE);
        panel.setVisibility(View.VISIBLE);
        panel.maximize();
        requstByVideo(items.get(position).getId());
        waterUrl = url;
        panelAdapter = new PanelAdapter(items, this, this);
        recyclerInPanel.setAdapter(panelAdapter);
        Log.d("size", items.size() + "");
        releasePlayer();
        initPlayer();
        initBottomSheet(items);
        Log.d("url", url);
    }

    private void initBottomSheet(List<MainPageItems> items) {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
            videoSource = null;
            trackSelector = null;
        }
    }

    private void initPlayer() {
//        player.stop(true);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        DefaultTrackSelector.Parameters trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
        trackSelector.setParameters(trackSelectorParameters);
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        player.stop(true);
        exoPlayerView = findViewById(R.id.player);
        exoPlayerView.setPlayer(player);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                this,
                Util.getUserAgent(this, APP_NAME),
                (DefaultBandwidthMeter) bandwidthMeter);//note the type casting

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        videoSource = new ExtractorMediaSource(Uri.parse(waterUrl),
                dataSourceFactory, extractorsFactory, null, null);
        mediaSource.addMediaSource(videoSource);


        player.prepare(mediaSource);
        if (mediaSource.getSize() != 1) {
            player.seekTo(mediaSource.getSize() - 1, C.TIME_UNSET);
        }
        player.setPlayWhenReady(true);
    }


    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("asd", "asd");
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) exoPlayerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            exoPlayerView.setLayoutParams(params);
            panel.setTopViewHeight(1080);
            panel.setEnabled(false);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) exoPlayerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = 600;
            exoPlayerView.setLayoutParams(params);
            panel.setTopViewHeight(600);
            panel.setEnabled(true);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
        player.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.setPlayWhenReady(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.release();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                break;
            case MotionEvent.ACTION_MOVE: // движение
                break;
            case MotionEvent.ACTION_UP: // отпускание

            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return true;
    }


    private void requstByVideo(String id) throws JSONException {

        Log.d("Response", "asd");
        String requestUrl = "https://video.orzu.org/api/v1.0/?type=get_video_details&video_id=" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Respose", "" + response); //the response contains the result from the server, a json string or any other object returned by your server
                try {
                    JSONObject j = new JSONObject(response);
                    JSONObject data = j.getJSONObject("data");
                    JSONObject owner = data.getJSONObject("owner");
                    name.setText(data.getString("title"));
                    views.setText(data.getString("views"));
                    likes.setText(data.getString("likes"));
                    dislikes.setText(data.getString("dislikes"));
                    channel_name.setText(owner.getString("first_name"));
                    followers.setText(data.getString("dislikes"));
                    date.setText(data.getString("time_alpha"));
                    name_of_video_bottom.setText(data.getString("title"));
                    name_of_category.setText(data.getString("category_name"));
                    progressBar.setVisibility(View.GONE);
                    bottom_constraint.setVisibility(View.VISIBLE);
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
        Volley.newRequestQueue(SearchActivity.this).add(stringRequest);
    }

    private void requstByText(String text) throws JSONException {
        items.clear();

        String requestUrl = "https://video.orzu.org/api/v1.0/?type=search_videos&keyword=" + text;
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
                            adapter = new PanelAdapter(items, getApplicationContext(), itemSelecListener);
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
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelectedListener(View view, int position) {
        try {
            MaximizePanel(items.get(position).getUrl(), items, position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

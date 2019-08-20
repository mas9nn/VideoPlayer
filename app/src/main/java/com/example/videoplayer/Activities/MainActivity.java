package com.example.videoplayer.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.videoplayer.Adapters.PanelAdapter;
import com.example.videoplayer.Adapters.ViewPagerAdapter;
import com.example.videoplayer.Common.Common;

import com.example.videoplayer.Draggable.DraggableListener;
import com.example.videoplayer.Draggable.DraggableView;
import com.example.videoplayer.Fragments.CategoryFragment;
import com.example.videoplayer.Fragments.CategoryVideosFragment;
import com.example.videoplayer.Fragments.ChannelFragment;
import com.example.videoplayer.Fragments.FollowedFragment;
import com.example.videoplayer.Fragments.LoginFragment;
import com.example.videoplayer.Fragments.MainFragment;
import com.example.videoplayer.Fragments.SearchFragment;
import com.example.videoplayer.Fragments.SettingsFragment;
import com.example.videoplayer.Interfaces.ItemSelecListener;
import com.example.videoplayer.Models.MainPageItems;

import com.example.videoplayer.Player.PlayerView;
import com.example.videoplayer.R;
import com.example.videoplayer.Receivers.NetworkChangeReceiver;
import com.example.videoplayer.ViewPager.CustomViewPager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;

import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, ItemSelecListener, Player.EventListener {
    private FragmentManager fragmentManager;
    TextView name, views, likes, dislikes, channel_name, followers, date, name_of_video_bottom, name_of_category, name_of_video, channel_names, title;

    ItemSelecListener itemSelecListener;

    DraggableView panel;
    Handler handler;
    RecyclerView recyclerInPanel;
    ImageButton like, dislike;
    ImageView hide, cancel, voice;
    Button follow;
    List<String> quality = new ArrayList<>();
    List<String> quality_has = new ArrayList<>();
    List<MainPageItems> suggestions = new ArrayList<>();
    SharedPreferences pref;
    DefaultTrackSelector trackSelector;
    String waterUrl = "https://video.orzu.org/upload/videos/2019/06/1DZjZiEBOEfGfcAslyHh_25_38aa4abb775fd1e9d30afdbf21561613_video_240p_converted.mp4";
    String id = "";
    String url_of_data = "";
    String id_of_category = "";
    String id_of_channel = "";
    String channel_id = "";
    PanelAdapter panelAdapter;
    private static final String APP_NAME = MainActivity.class.getSimpleName();
    private SimpleExoPlayer player;
    PlayerView exoPlayerView;
    MediaSource videoSource;
    MediaSource suggestion;
    ConstraintLayout bottom_constraint, hidable;
    ConcatenatingMediaSource mediaSource = new ConcatenatingMediaSource();
    ImageView search, channel_logo, arrow;
    List<String> ids = new ArrayList<>();
    private boolean onPauseCalled = false;
    int windowIndex, position;
    int lastPosition = -213;
    boolean logedIn;
    boolean liked = false;
    boolean followed = false;
    Toolbar tol;
    FrameLayout searcher;
    int count_of_adapter = 0;
    int clicks;
    int X, Y, cx, cy;
    BroadcastReceiver receiver;
    ProgressBar progressBar;
    LinearLayout layoutBottomSheet;
    Menu menu;
    BottomSheetBehavior sheetBehavior;
    EditText searchView;
    double topView;
    CustomViewPager viewPager;
    ShimmerFrameLayout shimmer;
    private final int REQ_CODE = 100;
    Uri data_uri;
    Switch aSwitch;
    Set<String> set = new LinkedHashSet<String>();
    List<String> choises = new ArrayList<>();
    SharedPreferences.Editor editor;
    MeowBottomNavigation meowBottomNavigation;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchView.setText(result.get(0).toString());
                }
                break;
            }
        }
    }
    @Override
    public void onBackPressed() {
        Log.d("asd", searcher.getVisibility() + " " + panel.isClosedAtRight());
        int orientation = getResources().getConfiguration().orientation;
        if (panel.isMaximized()) {
            Log.d("asd", "max");
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            } else {
                panel.minimize();
            }
            if (searcher.getVisibility() == View.VISIBLE) {
                Log.d("asd", "mbin");
                homePressed();
            }
        } else if (searcher.getVisibility() == View.VISIBLE) {
            homePressed();
            Log.d("asd", "min");
        } else if (panel.isMinimized()) {
            panel.closeToLeft();

        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        if (pref.getBoolean("dark", false)) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_main);

        viewPager = (CustomViewPager) findViewById(R.id.main_container);
        ViewPagerAdapter adapter = new ViewPagerAdapter(super.getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), "MainFragment");
        adapter.addFragment(new CategoryFragment(), "CategoryFragment");
        adapter.addFragment(new FollowedFragment(), "FollowedFragment");
        adapter.addFragment(new LoginFragment(), "LoginFragment");
        viewPager.setAdapter(adapter);
        viewPager.setPagingEnabled(false);
        handler = new Handler();


        editor = pref.edit();

        meowBottomNavigation = findViewById(R.id.meow);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home_black_24dp));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_dashboard_black_24dp));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_subscriptions));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_account_circle_black_24dp));
        meowBottomNavigation.show(1, true);
        meowBottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                if (model.getId() == 1) {
                    Log.d("position", model.getId() + "");
                    count_of_adapter = 1;
                    viewPager.setCurrentItem(0);

                } else if (model.getId() == 2) {
                    Log.d("position", model.getId() + "");
                    viewPager.setCurrentItem(1);
                    Log.d("minim", panel.isMinimized() + "");
                    if (!pref.getBoolean("isLoged", false)) {
                        viewPager.getAdapter().notifyDataSetChanged();
                    }
                } else if (model.getId() == 3) {
                    Log.d("position", model.getId() + "");
                    viewPager.setCurrentItem(2);
                    if (!pref.getBoolean("isLoged", false)) {
                        viewPager.getAdapter().notifyDataSetChanged();
                    }
                } else if (model.getId() == 4) {
                    Log.d("position", model.getId() + "");
                    viewPager.setCurrentItem(3);
                    if (!pref.getBoolean("isLoged", false)) {
                        viewPager.getAdapter().notifyDataSetChanged();
                    }
                }
                return null;
            }
        });


        quality.add("240p");
        quality.add("360p");
        quality.add("480p");
        quality.add("720p");
        quality.add("1080p");
        quality.add("2048p");
        quality.add("4096p");

        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        viewPager.setOffscreenPageLimit(0);
        //  AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        fragmentManager = getSupportFragmentManager();
        recyclerInPanel = findViewById(R.id.recycler_in_panel);
        recyclerInPanel.setLayoutManager(new LinearLayoutManager(this));
        recyclerInPanel.setNestedScrollingEnabled(false);
        name = findViewById(R.id.discription);
        views = findViewById(R.id.count_of_views);
        likes = findViewById(R.id.num_of_like);
        dislikes = findViewById(R.id.num_of_dis);
        channel_name = findViewById(R.id.name_of_channel);
        channel_logo = findViewById(R.id.channel_logo);
        followers = findViewById(R.id.followers);
        date = findViewById(R.id.date);
        name_of_video_bottom = findViewById(R.id.name_of_video_bottom);
        name_of_category = findViewById(R.id.name_of_category);
        progressBar = findViewById(R.id.bar);
        bottom_constraint = findViewById(R.id.bottom_constraint);
        shimmer = findViewById(R.id.shimmer);
        shimmer.startShimmer();
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislick);
        follow = findViewById(R.id.follow);
        search = findViewById(R.id.search_button);
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        arrow = findViewById(R.id.arrow);
        searcher = findViewById(R.id.searcher);
        hide = findViewById(R.id.more);
        hidable = findViewById(R.id.hidable);
        title = findViewById(R.id.title_tol);
        cancel = findViewById(R.id.clear_btn);
        voice = findViewById(R.id.voice);
        searchView = findViewById(R.id.searcher_edit_text);
        itemSelecListener = this;

        tol = findViewById(R.id.toolbar);
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hidable.getVisibility() == View.GONE) {
                    hide.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    hidable.setVisibility(View.VISIBLE);
                } else {
                    hide.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    hidable.setVisibility(View.GONE);
                }
            }
        });

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        tol.inflateMenu(R.menu.search_menu);
        setSupportActionBar(tol);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().replace(R.id.searcher, new SearchFragment(), "categoryVideos").commit();
                title.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                voice.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                tol.setNavigationIcon(R.drawable.ic_keyboard_back);
                tol.inflateMenu(R.menu.second_search);
                showWithRevealEffect(searcher);
                searchView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() >= 4) {
                            SearchFragment lastSMSFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag("categoryVideos");
                            assert lastSMSFragment != null;
                            lastSMSFragment.getChoices(charSequence.toString());
                            lastSMSFragment.setVisible(View.VISIBLE);
                        } else if (charSequence.length() > 0 && charSequence.length() < 4) {
                            SearchFragment lastSMSFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag("categoryVideos");
                            assert lastSMSFragment != null;
                            lastSMSFragment.savedChoises();
                            lastSMSFragment.setVisible(View.VISIBLE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_DONE) {
                            if (pref.getString("set", null) == null) {
                                choises.add(textView.getText().toString());
                                StringBuilder csvList = new StringBuilder();
                                for (String s : choises) {
                                    csvList.append(s);
                                    csvList.append("`,/-");
                                }
                                editor.putString("set", csvList.toString());
                                editor.commit();
                                Log.d("asd", set + "if");
                            } else {
                                String csvList = pref.getString("set", "");
                                choises.clear();
                                String[] items = csvList.split("`,/-");
                                for (int j = 0; j < items.length; j++) {
                                    choises.add(items[j]);
                                }
                                choises.add(textView.getText().toString());
                                StringBuilder csv = new StringBuilder();
                                for (String s : choises) {
                                    csv.append(s);
                                    csv.append("`,/-");
                                }
                                editor.putString("set", csv.toString());
                                editor.commit();
                                Log.d("asd", csv + "else");
                            }
                            Log.d("asd", pref.getString("set", null) + "");
                            SearchFragment lastSMSFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag("categoryVideos");
                            lastSMSFragment.getVideos(textView.getText().toString());
                            lastSMSFragment.setVisible(View.GONE);
                            return true;
                        }
                        return false;
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchView.setText("");
                    }
                });
                searchView.setFocusableInTouchMode(true);
                searchView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        arrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        arrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (liked) {
                    liked = false;
                    like.setImageResource(R.drawable.ic_favorite_black);
                    int count_like = Integer.parseInt(likes.getText().toString());
                    likes.setText((count_like - 1) + "");
                    try {
                        setLike(ids.get(player.getCurrentWindowIndex()), "up");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    liked = true;
                    like.setImageResource(R.drawable.ic_favorite_blue);
                    int count_like = Integer.parseInt(likes.getText().toString());
                    likes.setText((count_like + 1) + "");
                    try {
                        setLike(ids.get(player.getCurrentWindowIndex()), "down");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (followed) {
                    followed = false;
                    follow.setText("подписаться");
                    try {
                        setFollow(channel_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    followed = true;
                    follow.setText("подписка");
                    try {
                        setFollow(channel_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ImageButton share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "https://video.orzu.org/watch/" + ids.get(player.getCurrentWindowIndex()));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://video.orzu.org/watch/" + ids.get(player.getCurrentWindowIndex()));
                startActivity(Intent.createChooser(sharingIntent, "share:"));
            }
        });

        final LinearLayout layout = findViewById(R.id.linear);
        panel = findViewById(R.id.draggable_view);
        Display display = getWindowManager().getDefaultDisplay();
        Point sizee = new Point();
        display.getSize(sizee);
        int width = sizee.x;
        topView = width * 0.55;
        panel.setTopViewHeight((int) topView);
        Log.d("sad1", "" + topView);
        panel.closeToLeft();
        panel.setVisibility(View.GONE);
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float dip = 46f;
                Resources r = getResources();
                float px = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dip,
                        r.getDisplayMetrics()
                );
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.d("sad", layout.getHeight() + "");
                panel.setTopViewMarginBottom((int) (layout.getHeight() + px + panel.getTopViewMarginRight()));
            }
        });

        panel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
                //exoPlayerView.showController();
                panel.setClickToMaximizeEnabled(false);
                panel.setClickToMinimizeEnabled(false);
                Log.d("maxim", panel.getTopView() + "");
                // constra.setEnabled(true);
                // player.setPlayWhenReady(true);
            }

            @Override
            public void onMinimized() {
                if (exoPlayerView != null) {
                    exoPlayerView.hideController();
                    // handler.removeCallbacks();
                    panel.setClickToMaximizeEnabled(true);
                    panel.setTopViewHeight((int) topView);
                    Log.d("asdd", panel.getTopView() + "");
                }
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

        Intent intent = getIntent();
        data_uri = intent.getData();
        Log.d("data", data_uri + "");
        if (data_uri != null) {
            String string = data_uri + "";
            String[] parts = string.split("/");
            Log.d("data", parts[parts.length - 1] + "");

            hide.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            hidable.setVisibility(View.GONE);
            bottom_constraint.setVisibility(View.INVISIBLE);
            shimmer.setVisibility(View.VISIBLE);
            layoutBottomSheet.setVisibility(View.GONE);
            panel.setVisibility(View.VISIBLE);
            try {
                requstByVideo(parts[parts.length - 1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void showWithRevealEffect(View view) {
        cx = X;
        cy = Y;
        Log.d("Asdas", "log");
        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(view.getWidth(), view.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        anim.setDuration(200);

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();
    }


    private void hideWithRevealEffect(final View view) {

        // get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(view.getWidth(), view.getHeight());

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
        anim.setDuration(200);
        // start the animation
        anim.start();
    }

    public void openChannel(String channel_name, String id_channel) {
        showWithRevealEffect(searcher);
        Common.channelId = id_channel;
        fragmentManager.beginTransaction().replace(R.id.searcher, new ChannelFragment(), "categoryVideos").commit();
        tol.getMenu().clear();
        title.setVisibility(View.VISIBLE);
        title.setText(channel_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tol.setNavigationIcon(R.drawable.ic_keyboard_back);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Your code here
        X = (int) ev.getX();
        Y = (int) ev.getY();
        return super.dispatchTouchEvent(ev);
    }

    public void openCategory(String id, String name) {
        showWithRevealEffect(searcher);
        id_of_category = id;
        fragmentManager.beginTransaction().replace(R.id.searcher, new CategoryVideosFragment(), "categoryVideos").commit();
        tol.getMenu().clear();
        title.setVisibility(View.VISIBLE);
        title.setText(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tol.setNavigationIcon(R.drawable.ic_keyboard_back);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void openSettings() {
        showWithRevealEffect(searcher);
        fragmentManager.beginTransaction().replace(R.id.searcher, new SettingsFragment(), "categoryVideos").commit();
        tol.getMenu().clear();
        title.setVisibility(View.VISIBLE);
        title.setText("Настройки");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tol.setNavigationIcon(R.drawable.ic_keyboard_back);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public String getId() {
        return id_of_category;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            homePressed();
        }
        if (item.getItemId() == R.id.action_filter) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_dialog);
            Button cancel = dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void homePressed() {
        title.setVisibility(View.GONE);
        hideWithRevealEffect(searcher);
        Fragment fragment = fragmentManager.findFragmentByTag("categoryVideos");
        fragmentManager.beginTransaction().remove(fragment).commit();
        tol.getMenu().clear();
        search.setVisibility(View.VISIBLE);
        if (searchView != null) {
            searchView.setText("");
        }
        searchView.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        voice.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }

    public void MaximizePanel(String url, List<MainPageItems> items, int position) throws JSONException {
        hide.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        hidable.setVisibility(View.GONE);
        bottom_constraint.setVisibility(View.INVISIBLE);
        shimmer.setVisibility(View.VISIBLE);
        layoutBottomSheet.setVisibility(View.GONE);
        panel.setVisibility(View.VISIBLE);
        id = items.get(position).getId();
        Log.d("items", items.size() + " " + position);
        requstByVideo(items.get(position).getId());
        waterUrl = url;
        Log.d("positions", lastPosition + " " + position + " " + player);
        if (lastPosition != position) {
            releasePlayer();
            initPlayer();
            panel.maximize();
        } else {
            panel.maximize();
        }
        initBottomSheet(items);
        Log.d("url", url);
        lastPosition = position;
    }

    public void changePostion() {
        lastPosition = -123123;
    }

    private void addSuggestion(String url) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                this,
                Util.getUserAgent(this, APP_NAME),
                (DefaultBandwidthMeter) bandwidthMeter);//note the type casting
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        suggestion = new ExtractorMediaSource(Uri.parse(url),
                dataSourceFactory, extractorsFactory, null, null);

        mediaSource.addMediaSource(suggestion);
        ids.add(suggestions.get(0).getId());
    }


    private void initBottomSheet(List<MainPageItems> items) {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
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

        ids.add(id);
        player.addListener(this);
        player.prepare(mediaSource);
        if (mediaSource.getSize() != 1) {
            player.seekTo(mediaSource.getSize() - 1, C.TIME_UNSET);
        }
        if (onPauseCalled) {
            onPauseCalled = false;
            player.seekTo(windowIndex, position);
            player.setPlayWhenReady(false);
        } else {
            player.setPlayWhenReady(true);
        }
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
                // panel.disable(false);
                player.seekTo(position);
                panel.setClickToMaximizeEnabled(false);
            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                player.seekTo(position);
                player.setPlayWhenReady(true);
                //  panel.disable(false);
                panel.setClickToMaximizeEnabled(false);
                Log.d("dddd", "onmove");
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                if (Configuration.ORIENTATION_LANDSCAPE == MainActivity.this.getResources().getConfiguration().orientation) {
                    panel.setEnabled(false);
                } else {
                    panel.setEnabled(true);
                }
                player.setPlayWhenReady(true);
                panel.setClickToMaximizeEnabled(true);
                Log.d("dddd", "onStop");
            }
        });

        ImageButton next = exoPlayerView.findViewById(R.id.exo_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.seekTo(player.getCurrentWindowIndex() + 1, C.POSITION_UNSET);
                try {
                    if (ids.size() != 1) {
                        requstByVideo(ids.get(player.getCurrentWindowIndex()));
                        player.setPlayWhenReady(true);
                        Log.d("ids", ids.get(player.getCurrentWindowIndex()) + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        ImageButton previous = exoPlayerView.findViewById(R.id.exo_prev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player.getCurrentWindowIndex() != 0) {
                    player.seekTo(player.getCurrentWindowIndex() - 1, C.POSITION_UNSET);
                    if (suggestions.size() != 0) {
                        ids.remove(ids.size() - 1);
                        mediaSource.removeMediaSource(mediaSource.getSize() - 1);
                    }
                    try {
                        if (ids.size() != 1) {
                            requstByVideo(ids.get(player.getCurrentWindowIndex()));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        ImageButton quality = exoPlayerView.findViewById(R.id.setting);
        quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, quality);
                popup.getMenu().clear();
                for (int i = 0; i < quality_has.size(); i++) {
                    popup.getMenu().add(1, i, i + 1, quality_has.get(i));
                }
                popup.show();
            }
        });

        ConstraintLayout vpered = findViewById(R.id.vperedd);
        vpered.setVisibility(View.VISIBLE);
        exoPlayerView.setControllerShowTimeoutMs(2000);
        final long[] last_click = {0};
        exoPlayerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {

            }
        });
        final TextView back = findViewById(R.id.back);
        TextView back_txt = findViewById(R.id.back_txt);
        TextView forward = findViewById(R.id.forward);
        TextView forward_txt = findViewById(R.id.frw_text);


        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Log.d("clicks", clicks + " " + (SystemClock.elapsedRealtime() - last_click[0]));
                if (clicks == 1) {
                    if (exoPlayerView.isControllerVisible()) {
                        exoPlayerView.hideController();
                    } else {

                        exoPlayerView.hideController();

                        exoPlayerView.showController();

                    }
                    clicks = 0;
                } else {
                    clicks = 0;
                    back.setVisibility(View.GONE);
                    back_txt.setVisibility(View.GONE);
                    forward.setVisibility(View.GONE);
                    forward_txt.setVisibility(View.GONE);
                }
            }
        };
        final boolean[] frwd = {false};
        final boolean[] bck = {false};
        clicks = 0;
        panel.computeScroll();
        panel.setTouchEnabled(false);
        exoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        exoPlayerView.enableController();
                        if (motionEvent.getX() > exoPlayerView.getWidth() / 2) {
                            Log.d("minim", panel.isMinimized() + "");
                            if (bck[0]) {
                                clicks = 0;
                            }
                            frwd[0] = true;
                            bck[0] = false;
                            back.setVisibility(View.GONE);
                            back_txt.setVisibility(View.GONE);
                            clicks++;
                            if (clicks == 1) {
                                last_click[0] = SystemClock.elapsedRealtime();
                            }
                            if (clicks >= 2 & SystemClock.elapsedRealtime() - last_click[0] < 350) {
                                Log.d("asdasdd", SystemClock.elapsedRealtime() - last_click[0] + "");
                                last_click[0] = SystemClock.elapsedRealtime();
                            }
                            if (clicks >= 2) {
                                forward.setText(((clicks - 1) * pref.getInt("forward", 5)) + "");
                                forward.setVisibility(View.VISIBLE);
                                forward_txt.setVisibility(View.VISIBLE);
                                exoPlayerView.hideController();

                                player.seekTo(player.getCurrentPosition() + (pref.getInt("forward", 5) * 1000));
                            }
                            handler.removeCallbacks(runnable);
                            handler.postDelayed(runnable, 350);
                        } else {
                            if (frwd[0]) {
                                clicks = 0;
                            }
                            bck[0] = true;
                            frwd[0] = false;
                            forward.setVisibility(View.GONE);
                            forward_txt.setVisibility(View.GONE);
                            clicks++;
                            if (clicks == 1) {
                                last_click[0] = SystemClock.elapsedRealtime();
                            }

                            if (clicks >= 2 & SystemClock.elapsedRealtime() - last_click[0] < 350) {
                                Log.d("asdasdd", SystemClock.elapsedRealtime() - last_click[0] + "");
                                last_click[0] = SystemClock.elapsedRealtime();
                            }

                            if (clicks >= 2) {
                                exoPlayerView.hideController();
                                back.setText(((clicks - 1) * pref.getInt("backward", 5)) + "");
                                back.setVisibility(View.VISIBLE);
                                back_txt.setVisibility(View.VISIBLE);
                                player.seekTo(player.getCurrentPosition() - (pref.getInt("backward", 5) * 1000));
                            }
                            handler.removeCallbacks(runnable);
                            handler.postDelayed(runnable, 350);
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        Log.d("move", "moving");
                        exoPlayerView.disableController();
                        return true;
                }
                return false;
            }
        });
        name_of_video = exoPlayerView.findViewById(R.id.video_name);

        channel_names = exoPlayerView.findViewById(R.id.channel_names);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            name_of_video.setVisibility(View.VISIBLE);
            channel_names.setVisibility(View.VISIBLE);
        } else {
            name_of_video.setVisibility(View.GONE);
            channel_names.setVisibility(View.GONE);
        }

    }


    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("asd", "asd");
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            meowBottomNavigation.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) exoPlayerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            exoPlayerView.setLayoutParams(params);
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int height = size.y;
            panel.setTopViewHeight(height);
            Log.d("sad", "" + height);
            panel.setEnabled(false);
            channel_names.setText(channel_name.getText());
            name_of_video.setText(name.getText());
            name_of_video.setVisibility(View.VISIBLE);
            channel_names.setVisibility(View.VISIBLE);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            meowBottomNavigation.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) exoPlayerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            double topView = width * 0.55;
            params.height = (int) topView;
            exoPlayerView.setLayoutParams(params);
            panel.setTopViewHeight((int) topView);
            Log.d("sad", "" + (int) topView);
            panel.setEnabled(true);
            name_of_video.setVisibility(View.GONE);
            channel_names.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        logedIn = pref.getBoolean("isLoged", false);
        Log.d("isLoged", pref.getString("session", null) + "");
        if (player != null & !panel.isClosed()) {
            Log.d("asdas", panel.isClosed() + "");
            initPlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            Log.d("pause", "sad");
            windowIndex = player.getCurrentWindowIndex();
            position = (int) player.getCurrentPosition();
            onPauseCalled = true;
            player.release();
        }

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
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

        String requestUrl = "https://video.orzu.org/api/v1.0/?type=get_video_details&video_id=" + id;
        Log.d("ids", id);
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
                    Picasso.get().load(owner.getString("avatar")).into(channel_logo);
                    channel_id = owner.getString("id");
                    followers.setText(data.getString("dislikes"));
                    date.setText(data.getString("time_alpha"));
                    name_of_video_bottom.setText(data.getString("title"));
                    name_of_category.setText(data.getString("category_name"));
                    url_of_data = data.getString("video_location");

                    if (data.getString("is_liked").equals("1")) {
                        like.setImageResource(R.drawable.ic_favorite_blue);
                    } else
                        like.setImageResource(R.drawable.ic_favorite_black);
                    if (data.getString("is_disliked").equals("1")) {

                    } else {

                    }
                    if (data.getString("is_subscribed").equals("1")) {
                        follow.setText("Подписка");
                        followed = true;
                    } else {
                        follow.setText("подписаться");
                        followed = false;
                    }
                    quality_has.clear();
                    quality_has.add("Авто");
                    for (int i = 0; i < quality.size(); i++) {
                        if (data.getString(quality.get(i)).equals("1")) {
                            quality_has.add(quality.get(i));
                        }
                    }
                    initQuality();
                    progressBar.setVisibility(View.GONE);
                    bottom_constraint.setVisibility(View.VISIBLE);
                    shimmer.setVisibility(View.GONE);
                    layoutBottomSheet.setVisibility(View.VISIBLE);
                    JSONArray suggested = data.getJSONArray("suggested_videos");
                    Log.d("suggested", suggested + "");
                    if (suggested.length() != 0) {
                        suggestions.clear();
                    }
                    for (int i = 0; i < suggested.length(); i++) {
                        try {
                            Log.d("Response", suggested.get(i) + "");
                            JSONObject object = suggested.getJSONObject(i);
                            JSONObject suggested_owner = object.getJSONObject("owner");
                            MainPageItems pageItems = new MainPageItems();
                            pageItems.setPreview_image(object.getString("thumbnail"));
                            pageItems.setChannel_name(suggested_owner.getString("first_name"));
                            pageItems.setDays(object.getString("time_ago"));
                            pageItems.setDuration(object.getString("duration"));
                            pageItems.setName(object.getString("title"));
                            pageItems.setViews(object.getString("views"));
                            pageItems.setUrl(object.getString("video_location"));
                            pageItems.setId(object.getString("video_id"));
                            suggestions.add(pageItems);
                            panelAdapter = new PanelAdapter(suggestions, MainActivity.this, itemSelecListener);
                            recyclerInPanel.setAdapter(panelAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (suggestions.size() != 0) {

                        addSuggestion(suggestions.get(0).getUrl());
                    }
                    if (data_uri != null) {
                        waterUrl = url_of_data;
                        Log.d("positions", lastPosition + " " + position + " " + player);
                        if (lastPosition != position) {
                            releasePlayer();
                            initPlayer();
                            panel.maximize();
                        } else {
                            panel.maximize();
                        }
                        lastPosition = position;
                    }
                    //       addSuggestion(suggestions.get(0).getUrl());
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
                if (logedIn) {
                    if (pref.getString("session", null) != null) {
                        postMap.put("s", pref.getString("session", null));
                        postMap.put("user_id", pref.getString("userId", null));
                    }
                }
                return postMap;
            }
        };
        Volley.newRequestQueue(MainActivity.this).add(stringRequest);
    }

    private void initQuality() {

    }


    private void setLike(String id, String type) throws JSONException {
        String requestUrl = "https://video.orzu.org/api/v1.0/?type=like_dislike_video";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Respose", "" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("server_key", "e39111734a4e6a21dd442887dd5112c8");
                postMap.put("video_id", id);
                postMap.put("action", type);
                postMap.put("user_id", pref.getString("userId", null));
                postMap.put("s", pref.getString("session", null));
                //..... Add as many key value pairs in the map as necessary for your request
                return postMap;
            }
        };
//make the request to your server as indicated in your request url
        Volley.newRequestQueue(MainActivity.this).add(stringRequest);
    }

    private void setFollow(String id) throws JSONException {
        String requestUrl = "https://video.orzu.org/api/v1.0/?type=subscribe_to_channel&channel_id=" + id + "&user_id=" + pref.getString("userId", null)
                + "&s=" + pref.getString("session", null);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Respose", "" + response); //the response contains the result from the server, a json string or any other object returned by your server
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
        Volley.newRequestQueue(MainActivity.this).add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.clickable) {
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    }


    @Override
    public void onItemSelectedListener(View view, int position) {
        try {
            lastPosition = -123;
            Log.d("items", suggestions.get(position).getId() + "");
            MaximizePanel(suggestions.get(position).getUrl(), suggestions, position);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.e("errorPlayer", error.getMessage());
        mediaSource.removeMediaSource(mediaSource.getSize() - 1);
        ids.remove(ids.size() - 1);
        player.prepare(mediaSource);
        player.seekTo(mediaSource.getSize() - 1, -1);
    }

}

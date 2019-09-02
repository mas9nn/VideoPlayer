package com.example.videoplayer.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.videoplayer.Activities.MainActivity;
import com.example.videoplayer.Adapters.LoginAdapter;
import com.example.videoplayer.Models.LoginCategory;
import com.example.videoplayer.R;
import com.example.videoplayer.Activities.LoginActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoginFragment extends Fragment {
    private Button login;
    private ListView listView;
    private ConstraintLayout layout;
    private LoginAdapter adapter;
    private List<LoginCategory> categories = new ArrayList<>();
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView text, username, email;
    private ImageView image, settings, cover;
    private Switch aSwitch;

    @Nullable
    @Override

    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.login_fragment, null);
        login = v.findViewById(R.id.login);
        listView = v.findViewById(R.id.list_view);
        layout = v.findViewById(R.id.hidable_login);
        text = v.findViewById(R.id.text);
        username = v.findViewById(R.id.username);
        email = v.findViewById(R.id.email);
        image = v.findViewById(R.id.image);
        cover = v.findViewById(R.id.cover);
        aSwitch = v.findViewById(R.id.switcher);
        settings = v.findViewById(R.id.settings);
        pref = Objects.requireNonNull(getActivity()).getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        if (pref.getBoolean("dark", false)) {
            aSwitch.setChecked(true);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("dark", true);
                    editor.apply();
                    restartApp();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("dark", false);
                    editor.commit();
                    restartApp();
                }
            }
        });
        categories.add(new LoginCategory(R.drawable.ic_my_video, "Мое видео"));
        categories.add(new LoginCategory(R.drawable.ic_history, "История просмотров"));
        categories.add(new LoginCategory(R.drawable.ic_later, "Посмотреть позже"));
        categories.add(new LoginCategory(R.drawable.ic_border, "Избранное"));
        categories.add(new LoginCategory(R.drawable.ic_border, "Избранное"));
        categories.add(new LoginCategory(R.drawable.ic_exit, "Выйти"));
        adapter = new LoginAdapter(getContext(), categories);
        userInfo();
        boolean logedIn = pref.getBoolean("isLoged", false);
        if (logedIn) {
            text.setText("Вы уже вошли!!!");
            login.setText("Выйти");
        } else {
            text.setText("Войдите или зарегистрируйтесь");
            login.setText("Войти или зарегистрироваться");
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login.getText().toString().equals("Войти или зарегистрироваться")) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    Objects.requireNonNull(getActivity()).startActivity(intent);
                } else {
                    restartApp();
                    exitFromAccount();
                    pref.edit().clear().apply();
                }
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) Objects.requireNonNull(getActivity())).openSettings();
            }
        });
        return v;
    }

    private void restartApp() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        Objects.requireNonNull(getActivity()).finish();
    }

    private void exitFromAccount() {
        String requestUrl = "https://video.orzu.org/api/v1.0/?type=register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Registration", response);
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
                return postMap;
            }
        };
        Volley.newRequestQueue(Objects.requireNonNull(getActivity())).add(stringRequest);
    }

    private void userInfo() {
        String requestUrl = "https://video.orzu.org/api/v1.0/?type=get_channel_info&channel_id=" + pref.getString("userId", "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    JSONObject data = j.getJSONObject("data");
                    username.setText(data.getString("username"));
                    email.setText(data.getString("email"));
                    Picasso.get().load(data.getString("avatar")).into(image);
                    Picasso.get().load(data.getString("avatar")).into(image);
                    Picasso.get().load(data.getString("cover")).into(cover);
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
                return postMap;
            }
        };
        Volley.newRequestQueue(Objects.requireNonNull(getActivity())).add(stringRequest);
    }
}
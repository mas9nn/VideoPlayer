package com.example.videoplayer.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.videoplayer.Adapters.LoginAdapter;
import com.example.videoplayer.Models.LoginCategory;
import com.example.videoplayer.R;
import com.example.videoplayer.RegistrationActivity;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {
    Button login;
    ListView listView;
    ConstraintLayout layout;
    LoginAdapter adapter;
    List<LoginCategory> categories = new ArrayList<>();
    SharedPreferences pref;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment, null);
        login = v.findViewById(R.id.login);
        listView = v.findViewById(R.id.list_view);
        layout = v.findViewById(R.id.hidable_login);
        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        categories.add(new LoginCategory(R.drawable.ic_my_video, "Мое видео"));
        categories.add(new LoginCategory(R.drawable.ic_history, "История просмотров"));
        categories.add(new LoginCategory(R.drawable.ic_later, "Посмотреть позже"));
        categories.add(new LoginCategory(R.drawable.ic_border, "Избранное"));
        categories.add(new LoginCategory(R.drawable.ic_border, "Избранное"));
        categories.add(new LoginCategory(R.drawable.ic_exit, "Выйти"));
        adapter = new LoginAdapter(getContext(), categories);
        boolean logedIn = pref.getBoolean("isLoged", false);
        if(logedIn){
            layout.setVisibility(View.GONE);
        }else{
            layout.setVisibility(View.VISIBLE);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return v;
    }
}
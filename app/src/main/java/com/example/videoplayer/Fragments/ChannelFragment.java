package com.example.videoplayer.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.videoplayer.Adapters.ViewPagerAdapter;
import com.example.videoplayer.R;
import com.example.videoplayer.ViewPager.CustomViewPager;
import com.google.android.material.tabs.TabLayout;

public class ChannelFragment extends Fragment {

    TabLayout tabs;
    CustomViewPager viewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_channel, null);
        tabs = v.findViewById(R.id.tabs);
        viewPager = v.findViewById(R.id.channel_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ChannelVideosFragment(), "Видео");
        adapter.addFragment(new CategoryFragment(), "Плейлисты");
        adapter.addFragment(new FollowedFragment(), "Мне Нравится");
        adapter.addFragment(new LoginFragment(), "Описание");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        adapter.notifyDataSetChanged();
        tabs.setupWithViewPager(viewPager);
        return v;
    }

}
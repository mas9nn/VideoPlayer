package com.example.videoplayer.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoplayer.Adapters.CategoriesAdapter;
import com.example.videoplayer.Adapters.CategoryListViewAdapter;
import com.example.videoplayer.Adapters.CategoryLogosAdapter;
import com.example.videoplayer.Interfaces.ItemSelecListener;
import com.example.videoplayer.Activities.MainActivity;
import com.example.videoplayer.Models.CategoryItem;
import com.example.videoplayer.Models.FollowedItems;
import com.example.videoplayer.Models.ListViewItems;
import com.example.videoplayer.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryFragment extends Fragment implements ItemSelecListener {
    private SharedPreferences pref;
    private RecyclerView categoriesRec, channels, list_of_category;
    private CategoriesAdapter adapter;
    private List<CategoryItem> items = new ArrayList<>();
    private ItemSelecListener itemSelecListener;
    private CategoryListViewAdapter adapter_of_listview;
    private List<ListViewItems> mData = new ArrayList<>();
    private List<FollowedItems> channel_logos = new ArrayList<>();
    private CategoryLogosAdapter channel_adapter;

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.bottom_fragmnet, null);
        itemSelecListener = this;
        pref = Objects.requireNonNull(getActivity()).getApplicationContext().getSharedPreferences("MyPref", 0);

        categoriesRec = v.findViewById(R.id.categories);
        categoriesRec.setLayoutManager(new GridLayoutManager(getContext(), 3));

        channels = v.findViewById(R.id.channels);
        initChannels();

        list_of_category = v.findViewById(R.id.list_of_category);
        initCategory();

        getCategories();
        return v;
    }

    private void initChannels() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        channels.setLayoutManager(layoutManager);
        channel_logos.clear();
        channel_logos.add(new FollowedItems(R.drawable.ic_star));
        channel_logos.add(new FollowedItems(R.drawable.ic_star));
        channel_logos.add(new FollowedItems(R.drawable.ic_star));
        channel_logos.add(new FollowedItems(R.drawable.ic_star));
        channel_logos.add(new FollowedItems(R.drawable.ic_star));
        channel_logos.add(new FollowedItems(R.drawable.ic_star));
        channel_logos.add(new FollowedItems(R.drawable.ic_star));
        channel_logos.add(new FollowedItems(R.drawable.ic_star));
        channel_adapter = new CategoryLogosAdapter(getContext(), channel_logos);
        channels.setAdapter(channel_adapter);
    }

    private void initCategory() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list_of_category.setLayoutManager(layoutManager);
        mData.clear();
        mData.add(new ListViewItems(R.drawable.ic_event, "Топ недели"));
        mData.add(new ListViewItems(R.drawable.ic_wb_sunny, "Новинки"));
        mData.add(new ListViewItems(R.drawable.ic_trending, "Тренды"));
        mData.add(new ListViewItems(R.drawable.ic_star, "Выбор редакций"));
        adapter_of_listview = new CategoryListViewAdapter(getContext(), mData);
        list_of_category.setAdapter(adapter_of_listview);
        adapter_of_listview.setClickListener(new CategoryListViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ((MainActivity) Objects.requireNonNull(getActivity())).openCategory("9", mData.get(position).getName());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getCategories() {
        items.clear();

        items.add(new CategoryItem("Юмор", "9", R.drawable.ic_umor, "#D52964"));
        items.add(new CategoryItem("Фильмы", "1", R.drawable.ic_filmy, "#9A1FAF"));
        items.add(new CategoryItem("Музыка", "3", R.drawable.ic_music, "#643BA9"));
        items.add(new CategoryItem("Мультфильмы", "4", R.drawable.ic_mult, "#D55E81"));
        items.add(new CategoryItem("Спорт", "5", R.drawable.ic_sport, "#1E92EC"));
        items.add(new CategoryItem("Путешествия и события", "6", R.drawable.ic_putewestvie, "#22B8CF"));
        items.add(new CategoryItem("Игры", "7", R.drawable.ic_videogame, "#209694"));
        items.add(new CategoryItem("Люди и блоги", "8", R.drawable.ic_blog, "#49B154"));
        items.add(new CategoryItem("Развлечение", "10", R.drawable.ic_razvlechenie, "#D2E05E"));
        items.add(new CategoryItem("Новости", "11", R.drawable.ic_novosti, "#EFDD3D"));
        items.add(new CategoryItem("Стиль жизни", "12", R.drawable.ic_stil, "#FFBF15"));
        items.add(new CategoryItem("Образование и наука", "13", R.drawable.ic_obrozov, "#F06016"));
        items.add(new CategoryItem("Сериалы", "563", R.drawable.ic_serial, "#3B54C0"));
        adapter = new CategoriesAdapter(items, getContext(), this);
        categoriesRec.setAdapter(adapter);
    }

    @Override
    public void onItemSelectedListener(View view, int position) {

        ((MainActivity) Objects.requireNonNull(getActivity())).openCategory(items.get(position).getId(), items.get(position).getName());
    }

    @Override
    public void onClick(View view) {

    }

}
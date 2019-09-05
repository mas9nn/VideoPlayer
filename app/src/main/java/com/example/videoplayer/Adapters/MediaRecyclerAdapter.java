package com.example.videoplayer.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.videoplayer.Models.MainPageItems;
import com.example.videoplayer.Models.MediaObject;
import com.example.videoplayer.R;
import com.example.videoplayer.ViewHolder.PlayerViewHolder;

import java.util.ArrayList;


/**
 * Created by Morris on 03,June,2019
 */
public class MediaRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MainPageItems> mediaObjects;
    private RequestManager requestManager;
    private ItemClickListener mClickListener;
    private Context context;

    public MediaRecyclerAdapter(Context context,ArrayList<MainPageItems> mediaObjects,
                                RequestManager requestManager,ItemClickListener mClickListener) {
        this.mediaObjects = mediaObjects;
        this.requestManager = requestManager;
        this.mClickListener = mClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PlayerViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.main_items, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((PlayerViewHolder) viewHolder).onBind(context,mediaObjects.get(i), requestManager,mClickListener);
    }

    @Override
    public int getItemCount() {
        return mediaObjects.size();
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
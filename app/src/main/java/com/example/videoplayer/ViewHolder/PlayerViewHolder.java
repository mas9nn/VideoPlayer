package com.example.videoplayer.ViewHolder;


import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;


import com.example.videoplayer.Adapters.MediaRecyclerAdapter;
import com.example.videoplayer.Models.MainPageItems;
import com.example.videoplayer.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Morris on 03,June,2019
 */
public class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /**
     * below view have public modifier because
     * we have access PlayerViewHolder inside the ExoPlayerRecyclerView
     */
    public FrameLayout mediaContainer;
    public ImageView mediaCoverImage, volumeControl;
    public ProgressBar progressBar;
    public RequestManager requestManager;
    private View parent;

    private ImageView preview;
    private ImageButton button;
    private TextView duration, name, channel_name, views, days;
    private MediaRecyclerAdapter.ItemClickListener mClickListener;

    public PlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        mediaContainer = itemView.findViewById(R.id.container);
        duration = itemView.findViewById(R.id.duration);
        name = itemView.findViewById(R.id.name_of_video);
        channel_name = itemView.findViewById(R.id.channel_name);
        views = itemView.findViewById(R.id.count);
        days = itemView.findViewById(R.id.day);
        mediaCoverImage = itemView.findViewById(R.id.preview);
        button = itemView.findViewById(R.id.vert);
        itemView.setOnClickListener(this);
    }

    public void onBind(Context context, MainPageItems mediaObject, RequestManager requestManager, MediaRecyclerAdapter.ItemClickListener mClickListener) {
        this.requestManager = requestManager;
        this.mClickListener = mClickListener;
        parent.setTag(this);
        Picasso.get().load(mediaObject.getPreview_image()).into(mediaCoverImage);
        duration.setText(mediaObject.getDuration());
        name.setText(mediaObject.getName());
        channel_name.setText(mediaObject.getChannel_name());
        views.setText(mediaObject.getViews());
        days.setText(mediaObject.getDays());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, button);
                //inflating menu from xml resource
                popup.inflate(R.menu.item_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_fav:
                                //handle menu1 click
                                return true;
                            case R.id.later:
                                //handle menu2 click
                                return true;
                            case R.id.report:
                                //handle menu3 click
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
    }
}
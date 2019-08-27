package com.example.videoplayer.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoplayer.Common.Common;
import com.example.videoplayer.Models.FollowedItems;
import com.example.videoplayer.Models.ListViewItems;
import com.example.videoplayer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryListViewAdapter extends RecyclerView.Adapter<CategoryListViewAdapter.ViewHolder> {

    private List<ListViewItems> logos;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public CategoryListViewAdapter(Context context, List<ListViewItems> logos) {
        this.mInflater = LayoutInflater.from(context);
        this.logos = logos;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_view_items, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.preview.setImageResource(logos.get(position).getResource());
        holder.name.setText(logos.get(position).getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return logos.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView preview;
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            preview = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.query);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public ListViewItems getItem(int id) {
        return logos.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
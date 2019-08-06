package com.example.videoplayer.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoplayer.Models.FollowedItems;
import com.example.videoplayer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FollowedAdapter extends RecyclerView.Adapter<FollowedAdapter.ViewHolder> {

    List<FollowedItems> list;
    Context context;
    private ItemClickListener mClickListener;

    public FollowedAdapter(List<FollowedItems> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public FollowedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.logo_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowedAdapter.ViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getLogo()).into(holder.logo);
        Log.d("prikol",list.get(position).getLogo());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView logo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.logo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

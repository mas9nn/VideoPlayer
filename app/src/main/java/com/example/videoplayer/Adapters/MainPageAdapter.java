package com.example.videoplayer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoplayer.Interfaces.ItemSelecListener;
import com.example.videoplayer.Models.MainPageItems;
import com.example.videoplayer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainPageAdapter extends RecyclerView.Adapter<MainPageAdapter.ViewHolder> {

    private List<MainPageItems> list;
    private Context context;
    private ItemSelecListener select;

    public MainPageAdapter(List<MainPageItems> list, Context context,ItemSelecListener select) {
        this.list = list;
        this.context = context;
        this.select = select;
    }


    @NonNull
    @Override
    public MainPageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_items,parent,false);
        return new ViewHolder(view,select);
    }

    @Override
    public void onBindViewHolder(@NonNull MainPageAdapter.ViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getPreview_image()).into(holder.preview);
        holder.duration.setText(list.get(position).getDuration());
        holder.name.setText(list.get(position).getName());
        holder.channel_name.setText(list.get(position).getChannel_name());
        holder.views.setText(list.get(position).getViews());
        holder.days.setText(list.get(position).getDays());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.button);
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
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView preview;
        TextView duration,name,channel_name,views,days;
        ItemSelecListener selecListener;
        ImageButton button;

        public void setSelecListener(ItemSelecListener selecListener) {
            this.selecListener = selecListener;
        }

        ViewHolder(@NonNull View itemView, ItemSelecListener selecListener) {
            super(itemView);
            preview = itemView.findViewById(R.id.preview);
            duration = itemView.findViewById(R.id.duration);
            name = itemView.findViewById(R.id.name_of_video);
            channel_name = itemView.findViewById(R.id.channel_name);
            views = itemView.findViewById(R.id.count);
            days = itemView.findViewById(R.id.day);
            button = itemView.findViewById(R.id.vert);
            itemView.setOnClickListener(this);
            this.selecListener = selecListener;
        }

        @Override
        public void onClick(View view) {
            selecListener.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}

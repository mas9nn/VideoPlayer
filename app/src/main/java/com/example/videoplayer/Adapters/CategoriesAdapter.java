package com.example.videoplayer.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoplayer.Interfaces.ItemSelecListener;
import com.example.videoplayer.Models.CategoryItem;
import com.example.videoplayer.R;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<CategoryItem> list;
    private Context context;
    private ItemSelecListener select;

    public CategoriesAdapter(List<CategoryItem> list, Context context,ItemSelecListener select) {
        this.list = list;
        this.context = context;
        this.select = select;
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category,parent,false);
        return new ViewHolder(view,select);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int position) {
        holder.preview.setImageResource(list.get(position).getImage());
        holder.name.setText(list.get(position).getName());
        holder.view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(list.get(position).getBack_color())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView preview;
        TextView name;
        ItemSelecListener selecListener;
        ImageView view;
        public void setSelecListener(ItemSelecListener selecListener) {
            this.selecListener = selecListener;
        }

        ViewHolder(@NonNull View itemView, ItemSelecListener selecListener) {
            super(itemView);
            preview = itemView.findViewById(R.id.logo);
            name = itemView.findViewById(R.id.category_name);
            view = itemView.findViewById(R.id.back_of_category);
            itemView.setOnClickListener(this);
            this.selecListener = selecListener;
        }

        @Override
        public void onClick(View view) {
            selecListener.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}

package com.example.videoplayer.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.videoplayer.Models.LoginCategory;
import com.example.videoplayer.R;

import java.util.List;


public class LoginAdapter extends BaseAdapter {

    private List<LoginCategory> mData;
    Context context;

    public LoginAdapter(Context context, List<LoginCategory> mData) {
        this.context = context;
        this.mData = mData;
    }

    public void addItem(final LoginCategory item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public LoginCategory getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View v = LayoutInflater.from(context).inflate(R.layout.list_view_items, parent, false);
        TextView text = v.findViewById(R.id.query);
        text.setText(mData.get(position).getNaming());
        ImageView imageView = v.findViewById(R.id.image);
        imageView.setImageResource(mData.get(position).getIcon());
        return v;
    }

}
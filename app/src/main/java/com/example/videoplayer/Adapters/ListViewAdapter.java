package com.example.videoplayer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.videoplayer.R;

import java.util.List;


public class ListViewAdapter extends BaseAdapter {

    private List<String> mData;
    Context context;

    public ListViewAdapter(Context context, List<String> mData) {
        this.context = context;
        this.mData = mData;
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clearItems() {
        mData.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_view_items, parent, false);
        TextView text = v.findViewById(R.id.query);
        text.setText(mData.get(position));
        return v;
    }

}

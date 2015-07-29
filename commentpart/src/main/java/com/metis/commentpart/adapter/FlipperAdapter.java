package com.metis.commentpart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metis.commentpart.R;

public class FlipperAdapter extends BaseAdapter {

    private String[] mStringArray = null;
    private Context mContext = null;

    public FlipperAdapter (Context context, String[] stringArray) {
        mContext = context;
        mStringArray = stringArray;
    }

    @Override
    public int getCount() {
        return mStringArray.length;
    }

    @Override
    public Object getItem(int position) {
        return mStringArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FlipperHolder holder = null;
        if (convertView == null) {
            holder = new FlipperHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_flipper_item, null);
            holder.flipperTv = (TextView)convertView.findViewById(R.id.flipper_content);
            convertView.setTag(holder);
        } else {
            holder = (FlipperHolder)convertView.getTag();
        }
        holder.flipperTv.setText(mStringArray[position]);
        return convertView;
    }

    private class FlipperHolder {
        public TextView flipperTv = null;
    }
}
package com.metis.base.widget.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.metis.base.R;

/**
 * Created by Beak on 2015/7/31.
 */
public class FilterHolder extends RecyclerView.ViewHolder {

    public TextView titleTv = null;

    public FilterHolder(View itemView) {
        super(itemView);
        titleTv = (TextView)itemView.findViewById(R.id.filter_title);
    }

}

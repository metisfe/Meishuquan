package com.metis.coursepart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.delegate.StudioFilterDelegate;

/**
 * Created by Beak on 2015/7/14.
 */
public class StudioFilterHolder extends AbsViewHolder<StudioFilterDelegate> {

    TextView titleTv = null;

    public StudioFilterHolder(View itemView) {
        super(itemView);
        titleTv = (TextView)itemView.findViewById(R.id.filter_item_title);
    }

    @Override
    public void bindData(Context context, StudioFilterDelegate studioFilterDelegate, RecyclerView.Adapter adapter, int position) {
        titleTv.setText(studioFilterDelegate.getSource().name);
        titleTv.setSelected(studioFilterDelegate.isSelected());
    }
}

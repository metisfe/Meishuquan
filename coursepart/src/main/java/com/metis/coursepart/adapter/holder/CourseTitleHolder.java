package com.metis.coursepart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.delegate.CourseTitleDelegate;

/**
 * Created by Beak on 2015/7/14.
 */
public class CourseTitleHolder extends AbsViewHolder<CourseTitleDelegate> {

    TextView titleTv = null;

    public CourseTitleHolder(View itemView) {
        super(itemView);
        titleTv = (TextView)itemView.findViewById(R.id.item_title);
    }

    @Override
    public void bindData(Context context, CourseTitleDelegate courseTitleDelegate, RecyclerView.Adapter adapter, int position) {
        titleTv.setText(courseTitleDelegate.getSource());
        if (position == 0) {
            itemView.setBackgroundResource(R.drawable.video_item_bg_small_sel_top);
        } else if (position == adapter.getItemCount() - 1) {
            itemView.setBackgroundResource(R.drawable.video_item_bg_small_sel_bottom);
        } else {
            itemView.setBackgroundResource(R.drawable.video_item_bg_small_sel);
        }
    }
}

package com.metis.coursepart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.delegate.CourseDelegate;
import com.metis.coursepart.module.Course;

/**
 * Created by Beak on 2015/7/14.
 */
public class CourseHolder extends AbsViewHolder<CourseDelegate> {

    public ImageView mIconIv = null;
    public TextView mTitleTv = null;
    public TextView mDurationTv = null;
    public ImageView mDownloadBtn = null;

    public CourseHolder(View itemView) {
        super(itemView);

        mTitleTv = (TextView)itemView.findViewById(R.id.item_title);
        mDurationTv = (TextView)itemView.findViewById(R.id.item_duration);
        mDownloadBtn = (ImageView)itemView.findViewById(R.id.item_download_btn);
    }

    @Override
    public void bindData(Context context, CourseDelegate courseDelegate, RecyclerView.Adapter adapter, int position) {
        Course course = courseDelegate.getSource();
        mTitleTv.setText(course.subCourseName);
        mDurationTv.setText(course.videoTime);
        if (position == 0) {
            itemView.setBackgroundResource(R.drawable.video_item_bg_small_sel_top);
        } else if (position == adapter.getItemCount() - 1) {
            itemView.setBackgroundResource(R.drawable.video_item_bg_small_sel_bottom);
        } else {
            itemView.setBackgroundResource(R.drawable.video_item_bg_small_sel);
        }
        itemView.setSelected(courseDelegate.isSelected());
    }
}

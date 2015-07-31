package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.TeacherCbDelegate;
import com.metis.commentpart.manager.TeacherManager;

/**
 * Created by Beak on 2015/7/30.
 */
public class TeacherCbHolder extends TeacherHolder<TeacherCbDelegate> {

    private static final String TAG = TeacherCbHolder.class.getSimpleName();

    public ImageView checkBoxIv;

    public TeacherCbHolder(View itemView) {
        super(itemView);

        checkBoxIv = (ImageView)itemView.findViewById(R.id.item_check_flag);
    }

    @Override
    public void bindData(final Context context, final TeacherCbDelegate teacherDelegate, final RecyclerView.Adapter adapter, int position) {
        super.bindData(context, teacherDelegate, adapter, position);
        checkBoxIv.setSelected(teacherDelegate.isChecked());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teacherDelegate.isChecked()) {
                    TeacherManager.getInstance(context).unSelectTeacher(teacherDelegate);
                } else {
                    TeacherManager.getInstance(context).selectTeacher(teacherDelegate);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}

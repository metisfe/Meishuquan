package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.TeacherBtnDelegate;

/**
 * Created by Beak on 2015/8/4.
 */
public class TeacherBtnHolder extends TeacherHolder<TeacherBtnDelegate> {

    public TextView actionBtn;

    public TeacherBtnHolder(View itemView) {
        super(itemView);
        actionBtn = (TextView)itemView.findViewById(R.id.item_btn);
    }

    @Override
    public void bindData(Context context, TeacherBtnDelegate teacherBtnDelegate, RecyclerView.Adapter adapter, int position) {
        super.bindData(context, teacherBtnDelegate, adapter, position);
    }
}

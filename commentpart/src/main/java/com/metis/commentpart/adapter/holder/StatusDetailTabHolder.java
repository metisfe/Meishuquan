package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.StatusDetailTabDelegate;
import com.metis.commentpart.module.StatusDetailTabItem;

/**
 * Created by Beak on 2015/8/5.
 */
public class StatusDetailTabHolder extends AbsViewHolder<StatusDetailTabDelegate> {

    public TextView teacherTv, studentTv;

    public StatusDetailTabHolder(View itemView) {
        super(itemView);
        teacherTv = (TextView)itemView.findViewById(R.id.tab_teacher);
        studentTv = (TextView)itemView.findViewById(R.id.tab_student);
    }

    @Override
    public void bindData(Context context, StatusDetailTabDelegate statusDetailTabDelegate, RecyclerView.Adapter adapter, int position) {
        StatusDetailTabItem item = statusDetailTabDelegate.getSource();
        teacherTv.setText(item.getTextLeft());
        studentTv.setText(item.getTextRight());

        teacherTv.setOnClickListener(item.getOnClickListenerLeft());
        studentTv.setOnClickListener(item.getOnClickListenerRight());
    }
}

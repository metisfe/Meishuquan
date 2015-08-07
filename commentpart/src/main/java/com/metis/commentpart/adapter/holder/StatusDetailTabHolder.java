package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.StatusDetailTabDelegate;
import com.metis.commentpart.module.StatusDetailTabItem;

/**
 * Created by Beak on 2015/8/5.
 */
public class StatusDetailTabHolder extends AbsViewHolder<StatusDetailTabDelegate> {

    private RadioGroup mGroup;
    public RadioButton teacherTv, studentTv;

    public StatusDetailTabHolder(View itemView) {
        super(itemView);
        mGroup = (RadioGroup)itemView.findViewById(R.id.tab_container);
        teacherTv = (RadioButton)itemView.findViewById(R.id.tab_teacher);
        studentTv = (RadioButton)itemView.findViewById(R.id.tab_student);

    }

    @Override
    public void bindData(final Context context, final StatusDetailTabDelegate statusDetailTabDelegate, RecyclerView.Adapter adapter, int position) {
        final StatusDetailTabItem item = statusDetailTabDelegate.getSource();
        teacherTv.setText(item.getTextLeft());
        studentTv.setText(item.getTextRight());

        mGroup.check(statusDetailTabDelegate.getCurrentCheckedId());

        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                StatusDetailTabItem.OnTabSelectListener listener = item.getOnTabSelectListener();
                if (listener != null) {
                    if (checkedId == teacherTv.getId()) {
                        listener.onLeftSelected();
                    } else {
                        listener.onRightSelected();
                    }
                }
                statusDetailTabDelegate.setCurrentCheckedId(checkedId);
            }
        });
        /*teacherTv.setOnCheckedChangeListener(item.getLeftCheckListener());
        studentTv.setOnCheckedChangeListener(item.getRightCheckListener());*/
    }
}

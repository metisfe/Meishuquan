package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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

    private LinearLayout mGroup;
    public TextView teacherTv, studentTv;

    public StatusDetailTabHolder(View itemView) {
        super(itemView);
        mGroup = (LinearLayout)itemView.findViewById(R.id.tab_container);
        teacherTv = (TextView)itemView.findViewById(R.id.tab_teacher);
        studentTv = (TextView)itemView.findViewById(R.id.tab_student);

    }

    @Override
    public void bindData(final Context context, final StatusDetailTabDelegate statusDetailTabDelegate, RecyclerView.Adapter adapter, int position) {
        final StatusDetailTabItem item = statusDetailTabDelegate.getSource();
        teacherTv.setText(item.getTextLeft());
        studentTv.setText(item.getTextRight());

        teacherTv.setSelected(statusDetailTabDelegate.getCurrentCheckedId() == teacherTv.getId());
        studentTv.setSelected(statusDetailTabDelegate.getCurrentCheckedId() == studentTv.getId());

        //mGroup.check(statusDetailTabDelegate.getCurrentCheckedId());

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int id = v.getId();
                if (id == statusDetailTabDelegate.getCurrentCheckedId()) {
                    return;
                }
                StatusDetailTabItem.OnTabSelectListener listener = item.getOnTabSelectListener();
                if (id == teacherTv.getId()) {
                    if (listener != null) {
                        listener.onLeftSelected();
                    }
                } else {
                    if (listener != null) {
                        listener.onRightSelected();
                    }
                }
                statusDetailTabDelegate.setCurrentCheckedId(id);
            }
        };

        teacherTv.setOnClickListener(listener);
        studentTv.setOnClickListener(listener);

        /*mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (listener != null) {
                    if (checkedId == teacherTv.getId()) {
                        if (statusDetailTabDelegate.getCurrentCheckedId() != checkedId) {
                            listener.onLeftSelected();
                        }
                    } else {
                        if (statusDetailTabDelegate.getCurrentCheckedId() != checkedId) {
                            listener.onRightSelected();
                        }
                    }
                }
                statusDetailTabDelegate.setCurrentCheckedId(checkedId);
            }
        });*/
        /*teacherTv.setOnCheckedChangeListener(item.getLeftCheckListener());
        studentTv.setOnCheckedChangeListener(item.getRightCheckListener());*/
    }
}

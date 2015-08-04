package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metis.base.ActivityDispatcher;
import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.R;
import com.metis.commentpart.activity.TeacherListActivity;
import com.metis.commentpart.adapter.delegate.TeacherContainerDelegate;
import com.metis.commentpart.module.Teacher;

import java.util.List;

/**
 * Created by Beak on 2015/8/4.
 */
public class TeacherContainerHolder extends AbsViewHolder<TeacherContainerDelegate> {

    private TextView titleTv, subTitleTv;
    private LinearLayout mTeacherContainer = null;

    public TeacherContainerHolder(View itemView) {
        super(itemView);
        titleTv = (TextView)itemView.findViewById(R.id.title_text);
        subTitleTv = (TextView)itemView.findViewById(R.id.title_sub_text);
        mTeacherContainer = (LinearLayout)itemView.findViewById(R.id.teacher_container_in_status_list);
    }

    @Override
    public void bindData(final Context context, TeacherContainerDelegate teacherContainerDelegate, RecyclerView.Adapter adapter, int position) {
        titleTv.setText(context.getString(R.string.status_item_famous_teacher_list));
        subTitleTv.setText(context.getString(R.string.title_more));
        subTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, TeacherListActivity.class);
                context.startActivity(it);
                //TODO
            }
        });
        List<Teacher> teacherList = teacherContainerDelegate.getSource();
        if (teacherList != null && !teacherList.isEmpty()) {
            mTeacherContainer.removeAllViews();
            final int length = teacherList.size();
            LayoutInflater inflater = LayoutInflater.from(context);
            for (int i = 0; i < length; i++) {
                Teacher teacher = teacherList.get(i);
                if (teacher == null) {
                    continue;
                }
                final User user = teacher.user;
                if (user == null) {
                    continue;
                }
                View view = inflater.inflate(R.layout.layout_teacher_item_with_btn, null);
                ImageView profileIv = (ImageView)view.findViewById(R.id.teacher_profile);
                TextView nameTv = (TextView)view.findViewById(R.id.teacher_name);
                TextView extraInfoTv = (TextView)view.findViewById(R.id.teacher_extra_info);
                TextView actionBtn = (TextView)view.findViewById(R.id.item_btn);
                DisplayManager.getInstance(context).display(teacher.user.avatar, profileIv);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityDispatcher.userActivity(context, user.userId);
                    }
                });
                nameTv.setText(user.name);
                //TODO
                actionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
                    }
                });
                mTeacherContainer.addView(view);
                if (i < length - 1) {
                    View divider = new View (context);
                    divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
                    divider.setBackgroundColor(context.getResources().getColor(R.color.color_c6));
                    mTeacherContainer.addView(divider);
                }

            }
        }

    }
}

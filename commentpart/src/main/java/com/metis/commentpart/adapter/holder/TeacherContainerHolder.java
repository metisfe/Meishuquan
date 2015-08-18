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
import android.widget.Toast;

import com.metis.base.ActivityDispatcher;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.DisplayManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.User;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.R;
import com.metis.commentpart.activity.TeacherListActivity;
import com.metis.commentpart.adapter.delegate.TeacherContainerDelegate;
import com.metis.commentpart.module.Teacher;
import com.metis.msnetworklib.contract.ReturnInfo;

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
    public void bindData(final Context context, TeacherContainerDelegate teacherContainerDelegate, final RecyclerView.Adapter adapter, int position) {
        titleTv.setText(context.getString(R.string.status_item_famous_teacher_list));
        subTitleTv.setText(context.getString(R.string.title_more));
        subTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, TeacherListActivity.class);
                context.startActivity(it);
            }
        });
        List<Teacher> teacherList = teacherContainerDelegate.getSource();
        if (teacherList != null && !teacherList.isEmpty()) {
            mTeacherContainer.removeAllViews();
            final int length = teacherList.size();
            LayoutInflater inflater = LayoutInflater.from(context);
            final int size = Math.min(length, 3);
            for (int i = 0; i < size; i++) {
                final Teacher teacher = teacherList.get(i);
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
                DisplayManager.getInstance(context).displayProfile(teacher.user.avatar, profileIv);
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityDispatcher.userActivity(context, user.userId);
                    }
                };
                profileIv.setOnClickListener(listener);
                nameTv.setOnClickListener(listener);
                nameTv.setText(user.name);
                StringBuilder builder = new StringBuilder();
                if (teacher.commentCount > 0) {
                    builder.append(context.getString(R.string.invite_teacher_extra_info_answer, teacher.commentCount));
                }
                if (teacher.supportCount > 0) {
                    builder.append("  " + context.getString(R.string.invite_teacher_extra_info_support, teacher.supportCount));
                }
                extraInfoTv.setText(builder.toString());
                User me = AccountManager.getInstance(context).getMe();
                actionBtn.setVisibility(user.equals(me) ? View.GONE : View.VISIBLE);

                if (teacher.relationType == Teacher.RELATION_TYPE_EACH || teacher.relationType == Teacher.RELATION_TYPE_I_FOCUS) {
                    actionBtn.setSelected(true);
                    actionBtn.setText(R.string.btn_has_focused);
                    actionBtn.setOnClickListener(null);
                } else if (teacher.relationType == Teacher.RELATION_TYPE_I_WAS_FOLLOWED || teacher.relationType == Teacher.RELATION_TYPE_NONE) {
                    actionBtn.setSelected(false);
                    actionBtn.setText(R.string.btn_focus);

                }
                actionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (teacher.relationType == Teacher.RELATION_TYPE_I_WAS_FOLLOWED || teacher.relationType == Teacher.RELATION_TYPE_NONE) {
                            AccountManager.getInstance(context).attention(user.userId, 1, new RequestCallback() {
                                @Override
                                public void callback(ReturnInfo returnInfo, String callbackId) {
                                    if (returnInfo.isSuccess()) {
                                        if (teacher.relationType == Teacher.RELATION_TYPE_I_WAS_FOLLOWED) {
                                            teacher.relationType = Teacher.RELATION_TYPE_EACH;
                                        } else if (teacher.relationType == Teacher.RELATION_TYPE_NONE) {
                                            teacher.relationType = Teacher.RELATION_TYPE_I_FOCUS;
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(context, context.getString(R.string.toast_attention_failed, returnInfo.getMessage()), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            AccountManager.getInstance(context).cancelAttention(user.userId, new RequestCallback() {
                                @Override
                                public void callback(ReturnInfo returnInfo, String callbackId) {
                                    if (returnInfo.isSuccess()) {
                                        if (teacher.relationType == Teacher.RELATION_TYPE_EACH) {
                                            teacher.relationType = Teacher.RELATION_TYPE_I_WAS_FOLLOWED;
                                        } else if (teacher.relationType == Teacher.RELATION_TYPE_I_FOCUS) {
                                            teacher.relationType = Teacher.RELATION_TYPE_NONE;
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(context, context.getString(R.string.toast_cancel_attention_failed, returnInfo.getMessage
                                                ()), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                });
                mTeacherContainer.addView(view);
                if (i < size - 1) {
                    View divider = new View (context);
                    divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
                    divider.setBackgroundColor(context.getResources().getColor(R.color.color_c6));
                    mTeacherContainer.addView(divider);
                }

            }
        }

    }
}

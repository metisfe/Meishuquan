package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.User;
import com.metis.base.module.UserMark;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.TeacherBtnDelegate;
import com.metis.commentpart.module.Teacher;
import com.metis.msnetworklib.contract.ReturnInfo;

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
    public void bindData(final Context context, TeacherBtnDelegate teacherBtnDelegate, final RecyclerView.Adapter adapter, int position) {
        super.bindData(context, teacherBtnDelegate, adapter, position);
        final Teacher teacher = teacherBtnDelegate.getSource();
        final User user = teacher.user;

        User me = AccountManager.getInstance(context).getMe();

        if (user != null) {
            actionBtn.setVisibility(user.equals(me) ? View.GONE : View.VISIBLE);
            actionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //AccountManager.getInstance(context).getAttentionGroup();
                    if (teacher.relationType == Teacher.RELATION_TYPE_NONE || teacher.relationType == Teacher.RELATION_TYPE_I_WAS_FOLLOWED)
                    AccountManager.getInstance(context)
                            .attention(user.userId, 1, new RequestCallback() {
                                @Override
                                public void callback(ReturnInfo returnInfo, String callbackId) {
                                    if (returnInfo.isSuccess()) {
                                        if (teacher.relationType == Teacher.RELATION_TYPE_NONE) {
                                            teacher.relationType = Teacher.RELATION_TYPE_I_FOCUS;
                                        } else if (teacher.relationType == Teacher.RELATION_TYPE_I_WAS_FOLLOWED) {
                                            teacher.relationType = Teacher.RELATION_TYPE_EACH;
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                }
            });
        }
        if (teacher.relationType == Teacher.RELATION_TYPE_EACH) {
            actionBtn.setSelected(true);
            actionBtn.setText(R.string.btn_has_focused);
        } else if (teacher.relationType == Teacher.RELATION_TYPE_I_FOCUS) {
            actionBtn.setSelected(true);
            actionBtn.setText(R.string.btn_has_focused);
        } else if (teacher.relationType == Teacher.RELATION_TYPE_NONE) {
            actionBtn.setSelected(false);
            actionBtn.setText(R.string.btn_focus);
        } else if (teacher.relationType == Teacher.RELATION_TYPE_I_WAS_FOLLOWED) {
            actionBtn.setText(R.string.btn_focus);
        }
    }
}

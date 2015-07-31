package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.TeacherDelegate;
import com.metis.commentpart.module.Teacher;

/**
 * Created by Beak on 2015/7/30.
 */
public abstract class TeacherHolder<T extends TeacherDelegate> extends AbsViewHolder<T> {

    public ImageView profileIv;
    public TextView nameTv, extraInfoTv;


    public TeacherHolder(View itemView) {
        super(itemView);

        profileIv = (ImageView)itemView.findViewById(R.id.teacher_profile);
        nameTv = (TextView)itemView.findViewById(R.id.teacher_name);
        extraInfoTv = (TextView)itemView.findViewById(R.id.teacher_extra_info);
    }

    @Override
    public void bindData(Context context, T t, RecyclerView.Adapter adapter, int position) {
        Teacher teacher = t.getSource();
        User user = teacher.user;
        if (user != null) {
            DisplayManager.getInstance(context).display(
                    user.avatar, profileIv);

            nameTv.setText(user.name);
        }
    }
}

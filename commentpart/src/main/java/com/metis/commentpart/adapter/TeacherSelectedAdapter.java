package com.metis.commentpart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.TeacherCbDelegate;
import com.metis.commentpart.adapter.holder.TeacherSqHolder;
import com.metis.commentpart.manager.TeacherManager;
import com.metis.commentpart.module.Teacher;

import java.util.List;

/**
 * Created by Beak on 2015/7/31.
 */
public class TeacherSelectedAdapter extends RecyclerView.Adapter<TeacherSqHolder> {

    private Context mContext = null;
    private List<TeacherCbDelegate> mDataList = null;

    public TeacherSelectedAdapter (Context context) {
        mContext = context;
        mDataList = TeacherManager.getInstance(context).getSelectedTeachers();
    }

    @Override
    public TeacherSqHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_teacher_item_square, null);
        return new TeacherSqHolder(view);
    }

    @Override
    public void onBindViewHolder(TeacherSqHolder holder, int position) {
        final TeacherCbDelegate delegate = mDataList.get(position);
        final Teacher teacher = delegate.getSource();
        User user = teacher.user;
        if (user != null) {
            DisplayManager.getInstance(mContext).displayProfile(user.getAvailableAvatar(), holder.profileIv);
            holder.nameTv.setText(user.name);
        }
        holder.closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherManager.getInstance(mContext).unSelectTeacher(delegate);
                //notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}

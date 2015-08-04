package com.metis.commentpart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.base.widget.adapter.delegate.DelegateType;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.base.widget.adapter.holder.DividerHolder;
import com.metis.base.widget.adapter.holder.FooterHolder;
import com.metis.commentpart.adapter.delegate.CommentDelegateType;
import com.metis.commentpart.adapter.delegate.TeacherCbDelegate;
import com.metis.commentpart.adapter.holder.TeacherCbHolder;
import com.metis.commentpart.module.Teacher;

/**
 * Created by Beak on 2015/8/3.
 */
public class TeacherCbAdapter extends DelegateAdapter {

    public TeacherCbAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case CommentDelegateType.ID.ID_TEACHER_WITH_CHECK_BOX:
                return new TeacherCbHolder(view);
            case DelegateType.ID.ID_LIST_DIVIDER:
                return new DividerHolder(view);
            case DelegateType.ID.ID_FOOTER:
                return new FooterHolder(view);
        }
        throw new IllegalArgumentException("TeacherCbAdapter onCreateAbsViewHolder return null");
    }

    public void unSelected (Teacher teacher) {
        final int length = getItemCount();
        for (int i = 0; i < length; i++) {
            AbsDelegate delegate = getDataItem(i);
            if (delegate instanceof TeacherCbDelegate) {
                if (((TeacherCbDelegate) delegate).getSource().equals(teacher)) {
                    ((TeacherCbDelegate) delegate).setChecked(false);
                }
            }
        }
        notifyDataSetChanged();
    }
}

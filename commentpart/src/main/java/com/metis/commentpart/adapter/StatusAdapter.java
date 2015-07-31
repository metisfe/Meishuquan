package com.metis.commentpart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.DelegateType;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.base.widget.adapter.holder.DividerHolder;
import com.metis.commentpart.adapter.delegate.CommentDelegateType;
import com.metis.commentpart.adapter.holder.StatusHolder;
import com.metis.commentpart.adapter.holder.TeacherCbHolder;
import com.metis.commentpart.adapter.holder.TeacherSqHolder;

/**
 * Created by Beak on 2015/7/28.
 */
public class StatusAdapter extends DelegateAdapter {

    public StatusAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case CommentDelegateType.ID.ID_STATUS_ITEM:
                return new StatusHolder (view);
            case CommentDelegateType.ID.ID_TEACHER_WITH_CHECK_BOX:
                return new TeacherCbHolder(view);
            case DelegateType.ID.ID_LIST_DIVIDER:
                return new DividerHolder(view);
        }
        throw new IllegalArgumentException("DelegateAdapter onCreateAbsViewHolder return null");
    }
}

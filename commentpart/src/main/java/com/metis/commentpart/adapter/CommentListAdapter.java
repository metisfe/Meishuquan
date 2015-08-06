package com.metis.commentpart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.adapter.delegate.CommentDelegateType;
import com.metis.commentpart.adapter.holder.CommentItemHolder;
import com.metis.commentpart.adapter.holder.StatusDetailTabHolder;
import com.metis.commentpart.adapter.holder.StatusHolder;

/**
 * Created by Beak on 2015/8/6.
 */
public class CommentListAdapter extends DelegateAdapter {

    public CommentListAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case CommentDelegateType.ID.ID_STATUS_ITEM:
                return new StatusHolder(view);
            case CommentDelegateType.ID.ID_STATUS_DETAIL_TAB:
                return new StatusDetailTabHolder(view);
            case CommentDelegateType.ID.ID_COMMENT_LIST_ITEM:
                return new CommentItemHolder(view);
        }
        throw new IllegalArgumentException("CommentListAdapter onCreateAbsViewHolder return null");
    }
}

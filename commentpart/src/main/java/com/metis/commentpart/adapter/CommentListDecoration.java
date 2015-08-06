package com.metis.commentpart.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.CommentItemDelegate;

/**
 * Created by Beak on 2015/8/6.
 */
public class CommentListDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        if (!(adapter instanceof CommentListAdapter)) {
            return;
        }

        final int position = parent.getChildAdapterPosition(view);
        AbsDelegate delegate = ((CommentListAdapter) adapter).getDataItem(position);
        if (!(delegate instanceof CommentItemDelegate)) {
            return;
        }
        final int offset = parent.getContext().getResources().getDimensionPixelSize(R.dimen.margin_middle);
        if (position == adapter.getItemCount() - 1) {
            outRect.set(offset, offset, offset, offset);
        } else {
            outRect.set(offset, offset, offset, 0);
        }
    }
}

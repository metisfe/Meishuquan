package com.metis.commentpart.adapter;

import android.graphics.Canvas;
import android.graphics.Paint;
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

    private Paint mPaint = null;

    public CommentListDecoration () {
        mPaint = new Paint();
    }

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
        if (position == adapter.getItemCount() - 1 && position == 2) {
            outRect.set(offset, offset, offset, offset);
        } else if (position == adapter.getItemCount() - 1) {
            outRect.set(offset, 0, offset, offset);
        } else if (position == 2) {
            outRect.set(offset, offset, offset, 0);
        } else {
            outRect.set(offset, 0, offset, 0);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        if (!(adapter instanceof CommentListAdapter)) {
            return;
        }
        mPaint.setColor(parent.getContext().getResources().getColor(R.color.color_c6));
        mPaint.setStrokeWidth(1);
        final int offset = parent.getContext().getResources().getDimensionPixelSize(R.dimen.margin_middle);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(view);
            if (position <= 1 || position == adapter.getItemCount() - 1) {
                continue;
            }
            c.drawLine(view.getLeft() + offset, view.getBottom(), view.getRight() - offset, view.getBottom(), mPaint);
        }
    }
}

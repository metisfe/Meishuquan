package com.metis.newslib.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.newslib.R;
import com.metis.newslib.adapter.delegate.NewsCommentDelegate;
import com.metis.newslib.adapter.delegate.NewsRelativeDelegate;
import com.metis.newslib.adapter.delegate.NewsCardHeaderDelegate;

/**
 * Created by Beak on 2015/9/2.
 */
public class NewsDetailDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint = null;

    public NewsDetailDecoration() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final int size = parent.getContext().getResources().getDimensionPixelSize(R.dimen.margin_middle);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter != null && adapter instanceof NewsDetailAdapter) {
            NewsDetailAdapter newsAdapter = (NewsDetailAdapter)adapter;
            final int position = parent.getChildAdapterPosition(view);
            AbsDelegate delegate = newsAdapter.getDataItem(position);
            if (delegate instanceof NewsRelativeDelegate && ((NewsRelativeDelegate) delegate).isFirst()) {
                outRect.set(size, /*size * 2*/0, size, 0);
            } else if (delegate instanceof NewsCardHeaderDelegate) {
                outRect.set(size, size * 2, size, 0);
            } else {
                if (delegate instanceof NewsCommentDelegate) {
                    AbsDelegate previousDelegate = null;
                    if (position > 0) {
                        previousDelegate = ((DelegateAdapter) adapter).getDataItem(position - 1);
                    }
                    AbsDelegate nextDelegate = null;
                    if (position < adapter.getItemCount() - 1) {
                        nextDelegate = ((DelegateAdapter) adapter).getDataItem(position + 1);
                    }
                    if (previousDelegate != null && previousDelegate instanceof NewsCommentDelegate && nextDelegate != null && nextDelegate instanceof NewsCommentDelegate) {
                        outRect.set(size, 0, size, 0);
                    } else if (previousDelegate != null && previousDelegate instanceof NewsCommentDelegate) {
                        outRect.set(size, 0, size, size * 2);
                    } else if (nextDelegate != null && nextDelegate instanceof NewsCommentDelegate) {
                        outRect.set(size, 0/*size * 2*/, size, 0);
                    } else {
                        outRect.set(size, 0/*size * 2*/, size, size * 2);
                    }
                } else if (delegate instanceof NewsRelativeDelegate) {
                    AbsDelegate previousDelegate = null;
                    if (position > 0) {
                        previousDelegate = ((DelegateAdapter) adapter).getDataItem(position - 1);
                    }
                    AbsDelegate nextDelegate = null;
                    if (position < adapter.getItemCount() - 1) {
                        nextDelegate = ((DelegateAdapter) adapter).getDataItem(position + 1);
                    }
                    if (previousDelegate != null && previousDelegate instanceof NewsRelativeDelegate && nextDelegate != null && nextDelegate instanceof NewsRelativeDelegate) {
                        outRect.set(size, 0, size, 0);
                    } else if (previousDelegate != null && previousDelegate instanceof NewsRelativeDelegate) {
                        outRect.set(size, 0, size, size * 2);
                    } else if (nextDelegate != null && nextDelegate instanceof NewsRelativeDelegate) {
                        outRect.set(size, size * 2, size, 0);
                    } else {
                        outRect.set(size, size * 2, size, size * 2);
                    }
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int size = parent.getContext().getResources().getDimensionPixelSize(R.dimen.margin_middle);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter != null && adapter instanceof NewsDetailAdapter) {
            NewsDetailAdapter newsAdapter = (NewsDetailAdapter)adapter;
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                final int position = parent.getChildAdapterPosition(child);
                AbsDelegate delegate = newsAdapter.getDataItem(position);
                AbsDelegate nextDelegate = null;
                if (position < adapter.getItemCount() - 1) {
                    nextDelegate = newsAdapter.getDataItem(position + 1);
                }
                if ((delegate instanceof NewsRelativeDelegate && nextDelegate != null && nextDelegate instanceof NewsRelativeDelegate)
                        || (delegate instanceof NewsCommentDelegate && nextDelegate != null && nextDelegate instanceof  NewsCommentDelegate)) {
                    c.drawLine(child.getLeft() + size, child.getBottom(), child.getRight() - size, child.getBottom(), mPaint);
                }/* else {
                    if (delegate instanceof NewsCommentDelegate) {
                        AbsDelegate previousDelegate = null;
                        if (position > 0) {
                            previousDelegate = ((DelegateAdapter) adapter).getDataItem(position - 1);
                        }
                        AbsDelegate nextDelegate1 = null;
                        if (position < adapter.getItemCount() - 1) {
                            nextDelegate1 = ((DelegateAdapter) adapter).getDataItem(position + 1);
                        }
                        if (previousDelegate != null && previousDelegate instanceof NewsCommentDelegate && nextDelegate != null && nextDelegate instanceof NewsCommentDelegate) {
                            outRect.set(size, 0, size, 0);
                        } else if (previousDelegate != null && previousDelegate instanceof NewsCommentDelegate) {
                            outRect.set(size, 0, size, size * 2);
                        } else if (nextDelegate != null && nextDelegate instanceof NewsCommentDelegate) {
                            outRect.set(size, size * 2, size, 0);
                        } else {
                            outRect.set(size, size * 2, size, size * 2);
                        }
                    }
                }*/
            }
        }
    }

}

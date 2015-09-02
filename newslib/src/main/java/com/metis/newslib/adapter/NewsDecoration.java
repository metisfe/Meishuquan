package com.metis.newslib.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.newslib.R;
import com.metis.newslib.adapter.delegate.NewsBigDelegate;
import com.metis.newslib.adapter.delegate.NewsSmallDelegate;

/**
 * Created by Beak on 2015/9/2.
 */
public class NewsDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint = null;

    public NewsDecoration() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final int size = parent.getContext().getResources().getDimensionPixelSize(R.dimen.margin_middle);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter != null && adapter instanceof NewsAdapter) {
            NewsAdapter newsAdapter = (NewsAdapter)adapter;
            final int position = parent.getChildAdapterPosition(view);
            AbsDelegate delegate = newsAdapter.getDataItem(position);
            if (delegate instanceof NewsBigDelegate) {
                outRect.set(size, size * 2, size, size * 2);
            } else {
                outRect.set(size, 0, size, 0);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int size = parent.getContext().getResources().getDimensionPixelSize(R.dimen.margin_middle);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter != null && adapter instanceof NewsAdapter) {
            NewsAdapter newsAdapter = (NewsAdapter)adapter;
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                final int position = parent.getChildAdapterPosition(child);
                AbsDelegate delegate = newsAdapter.getDataItem(position);
                AbsDelegate nextDelegate = null;
                if (position < adapter.getItemCount() - 1) {
                    nextDelegate = newsAdapter.getDataItem(position + 1);
                }
                if (delegate instanceof NewsSmallDelegate && nextDelegate != null && nextDelegate instanceof NewsSmallDelegate) {
                    c.drawLine(child.getLeft() + size, child.getBottom(), child.getRight() - size, child.getBottom(), mPaint);
                }
            }
        }
    }

}

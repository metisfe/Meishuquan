package com.metis.commentpart.adapter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.TeacherDelegate;

/**
 * Created by Beak on 2015/8/28.
 */
public class TeacherDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = TeacherDecoration.class.getSimpleName();

    private Paint mPaint = null;

    public TeacherDecoration () {
        mPaint = new Paint();
        mPaint.setStrokeWidth(1);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int childCount = parent.getChildCount();
        if (childCount > 0) {
            RecyclerView.Adapter adapter = parent.getAdapter();
            if (!(adapter instanceof DelegateAdapter)) {
                return;
            }
            DelegateAdapter delegateAdapter = (DelegateAdapter)adapter;
            final int color = parent.getContext().getResources().getColor(R.color.color_c6);
            mPaint.setColor(color);
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                final int position = parent.getChildAdapterPosition(child);
                AbsDelegate delegate = delegateAdapter.getDataItem(position);
                AbsDelegate nextDelegate = null;
                if (position < delegateAdapter.getItemCount() - 2) {
                    nextDelegate = delegateAdapter.getDataItem(position + 1);
                }
                if (nextDelegate != null && nextDelegate instanceof TeacherDelegate && delegate instanceof TeacherDelegate && position != delegateAdapter.getItemCount() - 1) {
                    c.drawLine(child.getLeft(), child.getBottom(), child.getRight(), child.getBottom(), mPaint);
                }
            }
        }
    }

}

package com.metis.coursepart.adapter.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.coursepart.R;
import com.metis.coursepart.adapter.delegate.CourseDelegateType;

/**
 * Created by Beak on 2015/7/10.
 */
public class VideoItemSmallDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint = null;

    public VideoItemSmallDecoration() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        final int childCount = parent.getChildCount();
        final int itemCount = parent.getAdapter().getItemCount();
        final int deviderPading = parent.getContext().getResources().getDimensionPixelSize(R.dimen.video_divider_padding);

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (position == itemCount - 1) {
                continue;
            }
            int type = adapter.getItemViewType(position);
            if (type == CourseDelegateType.ID.ID_VIDEO_ITEM_SMALL) {
                final int left = child.getLeft() + deviderPading;
                final int y = child.getBottom();
                final int right = child.getRight() - deviderPading;
                c.drawLine(left, y, right, y, mPaint);
            }
        }
    }
}

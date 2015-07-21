package com.metis.coursepart.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.coursepart.R;

/**
 * Created by Beak on 2015/7/14.
 */
public class GalleryFilterMarginDecoration extends RecyclerView.ItemDecoration {

    private int mFirstItemTop = 0;
    //private int mLastItemBottom = 0;

    public GalleryFilterMarginDecoration(int first) {
        mFirstItemTop = first;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        final int position = parent.getChildAdapterPosition(view);
        final int size = view.getResources().getDimensionPixelSize(R.dimen.video_decoration_size);
        final int halfSize = size / 2;
        if (position == 0 || position == 1) {
            outRect.set(halfSize, mFirstItemTop + size, halfSize, 0);
        }else if (position == adapter.getItemCount() - 1) {
            outRect.set(halfSize, 0, halfSize, size * 4);
        } else {
            outRect.set(halfSize, 0, halfSize, 0);
        }
    }
}

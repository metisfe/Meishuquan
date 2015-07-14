package com.metis.coursepart.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.coursepart.R;

/**
 * Created by Beak on 2015/7/14.
 */
public class MarginDecoration extends RecyclerView.ItemDecoration {

    private int mFirstItemTop = 0;
    //private int mLastItemBottom = 0;

    public MarginDecoration (int first) {
        mFirstItemTop = first;
    }

    /*public MarginDecoration (int first, int last) {
        mFirstItemTop = first;
        mLastItemBottom = last;
    }*/

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        final int position = parent.getChildAdapterPosition(view);
        final int size = view.getResources().getDimensionPixelSize(R.dimen.video_decoration_size);
        if (position == 0) {
            outRect.set(size, mFirstItemTop + size, size, 0);
        }else if (position == adapter.getItemCount() - 1) {
            outRect.set(size, 0, size, size * 4);
        } else {
            outRect.set(size, 0, size, 0);
        }
    }
}

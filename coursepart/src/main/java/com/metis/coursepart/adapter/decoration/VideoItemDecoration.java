package com.metis.coursepart.adapter.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.coursepart.R;

/**
 * Created by Beak on 2015/7/6.
 */
public class VideoItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        Context context = view.getContext();
        final int size = context.getResources().getDimensionPixelSize(R.dimen.video_decoration_size);
        outRect.set(size, size * 4, size, 0);
        /*RecyclerView.Adapter adapter = parent.getAdapter();
        final int count = adapter.getItemCount();
        for (int i = 0; i < count; i++) {
            int type = adapter.getItemViewType(i);
            //TODO
            //outRect.set();
        }*/
    }
}

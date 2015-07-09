package com.metis.coursepart.adapter.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.metis.coursepart.R;

/**
 * Created by Beak on 2015/7/9.
 */
public class GalleryItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = GalleryItemDecoration.class.getSimpleName();

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        Context context = view.getContext();
        final int size = context.getResources().getDimensionPixelSize(R.dimen.gallery_decoration_size);
        final int halfSize = size / 2;
        outRect.set(halfSize, size, halfSize, 0);
        /*final int position = parent.getChildAdapterPosition(view);
        //final int itemCount = parent.getAdapter().getItemCount();

        final int spanCount = context.getResources().getInteger(R.integer.gallery_span_count);
        final int left = view.getWidth();
        final int parentWidth = parent.getWidth();
        final int perWidth = parentWidth / spanCount;
        Log.v(TAG, "position=" + position + " left=" + left + " parentWidth=" + parentWidth);

        if (left < perWidth) {
            outRect.set(size, size, halfSize, 0);
        } else if (left >= perWidth * (spanCount - 1)) {
            outRect.set(halfSize, size, size, 0);
        } else {
            outRect.set(halfSize, size, halfSize, 0);
        }*/
    }
}

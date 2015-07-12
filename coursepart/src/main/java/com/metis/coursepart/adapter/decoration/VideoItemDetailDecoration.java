package com.metis.coursepart.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.coursepart.R;

/**
 * Created by gaoyunfei on 15/7/11.
 */
public class VideoItemDetailDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        final int position = parent.getChildAdapterPosition(view);
        final int size = view.getResources().getDimensionPixelSize(R.dimen.video_decoration_size);
        if (position == 0) {
            outRect.set(size, size * 4, size, 0);
        } else {
            outRect.set(size, 0, size, 0);
        }
    }
}

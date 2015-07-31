package com.metis.commentpart.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.commentpart.R;

/**
 * Created by Beak on 2015/7/29.
 */
public class StatusItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final int offset = parent.getContext().getResources().getDimensionPixelSize(R.dimen.status_item_decoration_offset);
        outRect.set(offset, offset, offset, offset);
    }
}

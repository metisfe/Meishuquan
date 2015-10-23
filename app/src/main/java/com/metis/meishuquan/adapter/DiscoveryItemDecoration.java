package com.metis.meishuquan.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Beak on 2015/10/21.
 */
public class DiscoveryItemDecoration extends RecyclerView.ItemDecoration {

    private int mOffsetMargin = -1;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOffsetMargin < 0) {
            mOffsetMargin = view.getContext().getResources().getDimensionPixelSize(com.metis.base.R.dimen.margin_big);
        }
        outRect.set(0, mOffsetMargin, 0, 0);
    }
}

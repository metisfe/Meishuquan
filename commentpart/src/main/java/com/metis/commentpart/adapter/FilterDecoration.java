package com.metis.commentpart.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.commentpart.R;

/**
 * Created by Beak on 2015/8/4.
 */
public class FilterDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        Context context = parent.getContext();
        final int offset = context.getResources().getDimensionPixelOffset(R.dimen.status_item_decoration_offset);
        final int header = context.getResources().getDimensionPixelOffset(R.dimen.filter_status_list_decoration_top_offset) + offset;
        final int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            outRect.set(offset, header, offset, offset);
        } else {
            outRect.set(offset, offset, offset, offset);
        }
    }
}

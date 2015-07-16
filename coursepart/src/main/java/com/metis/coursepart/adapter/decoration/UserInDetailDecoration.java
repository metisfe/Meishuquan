package com.metis.coursepart.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.coursepart.R;
import com.metis.coursepart.adapter.delegate.CourseDelegateType;

/**
 * Created by Beak on 2015/7/15.
 */
public class UserInDetailDecoration extends  VideoItemDetailDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        final int positon = parent.getChildAdapterPosition(view);
        int type = adapter.getItemViewType(positon);
        final int size = view.getResources().getDimensionPixelSize(R.dimen.video_decoration_size);
        if (positon == 0 && type == CourseDelegateType.ID.ID_USER_IN_DETAIL) {
            outRect.set(size, size * 4, size, size * 4);
            return;
        }
        super.getItemOffsets(outRect, view, parent, state);
    }
}

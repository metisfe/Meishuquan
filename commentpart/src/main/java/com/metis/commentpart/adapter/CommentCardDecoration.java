package com.metis.commentpart.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.CardFooterDelegate;
import com.metis.commentpart.adapter.delegate.CardHeaderDelegate;
import com.metis.commentpart.adapter.delegate.CardTextSDelegate;
import com.metis.commentpart.adapter.delegate.CardTextTDelegate;
import com.metis.commentpart.adapter.delegate.CardVoiceTDelegate;
import com.metis.commentpart.adapter.delegate.StatusDelegate;
import com.metis.commentpart.adapter.delegate.StatusDetailTabDelegate;

/**
 * Created by Beak on 2015/8/5.
 */
public class CommentCardDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null || !(adapter instanceof CommentCardAdapter)) {
            return;
        }
        final int offset = parent.getContext().getResources().getDimensionPixelSize(R.dimen.margin_middle);
        CommentCardAdapter cardAdapter = (CommentCardAdapter)adapter;
        final int position = parent.getChildAdapterPosition(view);

        AbsDelegate delegate = cardAdapter.getDataItem(position);
        if (position == adapter.getItemCount() - 1) {
            if (delegate instanceof StatusDetailTabDelegate || delegate instanceof StatusDelegate) {
                outRect.set(0, 0, 0, offset);
            } else {
                outRect.set(offset, 0, offset, offset);
            }
            return;
        }
        if (delegate instanceof CardHeaderDelegate) {
            outRect.set(offset, offset, offset, 0);
        } else if (delegate instanceof CardFooterDelegate) {
            outRect.set(offset, 0, offset, 0);
        } else if (delegate instanceof CardTextTDelegate || delegate instanceof CardTextSDelegate || delegate instanceof CardVoiceTDelegate) {
            outRect.set(offset, 0, offset, 0);
        }
    }
}

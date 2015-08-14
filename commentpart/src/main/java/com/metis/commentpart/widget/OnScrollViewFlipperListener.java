package com.metis.commentpart.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/27.
 */
public class OnScrollViewFlipperListener extends RecyclerView.OnScrollListener {

    private List<ViewFlippable> mFlippableList = new ArrayList<ViewFlippable>();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            mFlippableList.addAll(collectFilppable(recyclerView));
            startAll();
        } else {
            stopAll();
            mFlippableList.clear();
        }
    }

    private List<ViewFlippable> collectFilppable (RecyclerView recyclerView) {
        List<ViewFlippable> flippables = new ArrayList<ViewFlippable>();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null) {
            final int childCount = recyclerView.getChildCount();
            if (childCount <= 0) {
                return flippables;
            }
            for (int i = 0; i < childCount; i++) {
                RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                if (holder instanceof ViewFlippable) {
                    flippables.add((ViewFlippable)holder);
                }
            }
        }
        return flippables;
    }

    private synchronized void stopAll () {
        final int length = mFlippableList.size();
        for (int i = 0; i < length; i++) {
            ViewFlippable flippable = mFlippableList.get(i);
            AdapterViewFlipper flipper = flippable.getViewFlipper();
            if (flipper != null) {
                flipper.stopFlipping();
            }
        }
    }

    private synchronized void startAll () {
        final int length = mFlippableList.size();
        for (int i = 0; i < length; i++) {
            ViewFlippable flippable = mFlippableList.get(i);
            AdapterViewFlipper flipper = flippable.getViewFlipper();
            if (flipper != null) {
                flipper.startFlipping();
                flipper.showNext();
            }
        }
    }
}

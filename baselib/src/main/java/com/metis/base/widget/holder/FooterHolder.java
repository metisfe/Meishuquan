package com.metis.base.widget.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.delegate.FooterDelegate;

/**
 * Created by Beak on 2015/7/8.
 */
public class FooterHolder extends AbsViewHolder<FooterDelegate> {

    public FooterHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindData(Context context, FooterDelegate footerDelegate) {
        if (footerDelegate.isInStaggeredGrid()) {

            StaggeredGridLayoutManager.LayoutParams params
                    = (StaggeredGridLayoutManager.LayoutParams)itemView.getLayoutParams();
            if (params == null) {
                params = new StaggeredGridLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            params.setFullSpan(true);
            itemView.setLayoutParams(params);
        }
        //params.
    }
}

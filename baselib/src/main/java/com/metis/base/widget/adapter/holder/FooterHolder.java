package com.metis.base.widget.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.metis.base.R;
import com.metis.base.module.Footer;
import com.metis.base.widget.adapter.delegate.FooterDelegate;

/**
 * Created by Beak on 2015/7/8.
 */
public class FooterHolder extends AbsViewHolder<FooterDelegate> {

    public ProgressBar mProgressBar = null;
    public TextView mTipTv = null;

    public FooterHolder(View itemView) {
        super(itemView);
        mProgressBar = (ProgressBar)itemView.findViewById(R.id.footer_progress);
        mTipTv = (TextView)itemView.findViewById(R.id.footer_tip);
    }

    @Override
    public void bindData(Context context, FooterDelegate footerDelegate, RecyclerView.Adapter adapter, int position) {
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
            switch (footerDelegate.getSource().getState()) {
                case Footer.STATE_FAILED:
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mTipTv.setVisibility(View.VISIBLE);
                    mTipTv.setText(R.string.footer_failed);
                    break;
                case Footer.STATE_IDLE:
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mTipTv.setVisibility(View.INVISIBLE);
                    mTipTv.setText("");
                    break;
                case Footer.STATE_NO_MORE:
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mTipTv.setVisibility(View.VISIBLE);
                    mTipTv.setText(R.string.footer_no_more);
                    break;
                case Footer.STATE_SUCCESS:
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mTipTv.setVisibility(View.VISIBLE);
                    mTipTv.setText(R.string.footer_success);
                    break;
                case Footer.STATE_WAITTING:
                    mProgressBar.setVisibility(View.VISIBLE);
                    mTipTv.setVisibility(View.INVISIBLE);
                    mTipTv.setText("");
                    break;
            }
        }
        //params.
    }
}

package com.metis.base.widget.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.metis.base.R;
import com.metis.base.widget.adapter.delegate.DividerDelegate;

/**
 * Created by Beak on 2015/7/31.
 */
public class DividerHolder extends AbsViewHolder<DividerDelegate> {

    public TextView dividerTitleTv = null;

    public DividerHolder(View itemView) {
        super(itemView);
        dividerTitleTv = (TextView)itemView.findViewById(R.id.divider_title);
    }

    @Override
    public void bindData(Context context, DividerDelegate dividerDelegate, RecyclerView.Adapter adapter, int position) {
        dividerTitleTv.setText(dividerDelegate.getSource());
    }
}

package com.metis.base.widget.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by gaoyunfei on 15/5/24.
 */
public abstract class AbsViewHolder<T> extends RecyclerView.ViewHolder {

    public AbsViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindData (Context context, T t, RecyclerView.Adapter adapter, int position);
}

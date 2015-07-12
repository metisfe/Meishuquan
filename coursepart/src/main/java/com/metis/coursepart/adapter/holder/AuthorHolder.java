package com.metis.coursepart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.coursepart.adapter.delegate.AuthorDelegate;

/**
 * Created by gaoyunfei on 15/7/12.
 */
public class AuthorHolder extends AbsViewHolder<AuthorDelegate> {
    public AuthorHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindData(Context context, AuthorDelegate authorDelegate, RecyclerView.Adapter adapter, int position) {

    }
}

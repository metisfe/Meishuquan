package com.metis.newslib.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.newslib.R;
import com.metis.newslib.adapter.delegate.NewsCardHeaderDelegate;

/**
 * Created by Beak on 2015/9/11.
 */
public class NewsCardHeaderHolder extends AbsViewHolder<NewsCardHeaderDelegate> {

    public TextView headerTitleTv = null;

    public NewsCardHeaderHolder(View itemView) {
        super(itemView);
        headerTitleTv = (TextView)itemView.findViewById(R.id.related_title);
    }

    @Override
    public void bindData(Context context, NewsCardHeaderDelegate newsRelativeHeaderDelegate, RecyclerView.Adapter adapter, int position) {
        headerTitleTv.setText(newsRelativeHeaderDelegate.getSource());
    }

}

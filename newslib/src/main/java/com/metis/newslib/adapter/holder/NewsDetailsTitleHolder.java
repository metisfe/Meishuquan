package com.metis.newslib.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.newslib.R;
import com.metis.newslib.adapter.delegate.NewsDetailsTitleDelegate;
import com.metis.newslib.module.NewsDetails;

/**
 * Created by Beak on 2015/9/4.
 */
public class NewsDetailsTitleHolder extends AbsViewHolder<NewsDetailsTitleDelegate> {

    public TextView titleTv, sourceTv, timeTv;

    public NewsDetailsTitleHolder(View itemView) {
        super(itemView);
        titleTv = (TextView)itemView.findViewById(R.id.title_text);
        sourceTv = (TextView)itemView.findViewById(R.id.title_source);
        timeTv = (TextView)itemView.findViewById(R.id.title_time);
    }

    @Override
    public void bindData(Context context, NewsDetailsTitleDelegate newsDetailsTitleDelegate, RecyclerView.Adapter adapter, int position) {
        final NewsDetails details = newsDetailsTitleDelegate.getSource();
        if (TextUtils.isEmpty(details.title)) {
            titleTv.setText("");
        } else {
            titleTv.setText(details.title.trim());
        }
        if (details.source != null) {
            sourceTv.setText(details.source.title);
        } else {
            sourceTv.setText(R.string.app_name);
        }
        if (TextUtils.isEmpty(details.author)) {

        } else {
            sourceTv.setText(details.author.trim());
        }

        if (TextUtils.isEmpty(details.modifyTime)) {
            timeTv.setText("");
        } else {
            timeTv.setText(details.modifyTime.trim());
        }
    }
}

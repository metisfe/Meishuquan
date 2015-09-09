package com.metis.newslib.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.manager.DisplayManager;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.newslib.R;
import com.metis.newslib.adapter.delegate.NewsDetailsImgDelegate;
import com.metis.newslib.adapter.delegate.NewsDetailsTxtDelegate;
import com.metis.newslib.module.NewsDetails;

/**
 * Created by Beak on 2015/9/4.
 */
public class NewsDetailsTxtHolder extends AbsViewHolder<NewsDetailsTxtDelegate> {
    public TextView textView;
    public NewsDetailsTxtHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.news_detail_txt);
    }

    @Override
    public void bindData(Context context, NewsDetailsTxtDelegate newsDetailsTxtDelegate, RecyclerView.Adapter adapter, int position) {
        NewsDetails.Item item = newsDetailsTxtDelegate.getSource();
        if (TextUtils.isEmpty(item.data.Content)) {
            textView.setText("");
        } else {
            textView.setText(item.data.Content);
        }
    }
}

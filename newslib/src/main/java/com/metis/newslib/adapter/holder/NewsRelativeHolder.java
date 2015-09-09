package com.metis.newslib.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.manager.DisplayManager;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.newslib.ActivityDispatcher;
import com.metis.newslib.R;
import com.metis.newslib.adapter.delegate.NewsRelativeDelegate;
import com.metis.newslib.module.NewsItem;
import com.metis.newslib.module.NewsItemRelated;

/**
 * Created by Beak on 2015/9/4.
 */
public class NewsRelativeHolder extends AbsViewHolder<NewsRelativeDelegate> {

    public ImageView smallThumbIv = null;
    public TextView smallTitleTv = null;
    public TextView smallSocialInfoTv = null;
    public TextView smallSourceTv = null;

    public NewsRelativeHolder(View itemView) {
        super(itemView);
        smallThumbIv = (ImageView)itemView.findViewById(R.id.news_small_thumb);
        smallTitleTv = (TextView)itemView.findViewById(R.id.news_small_title);
        smallSocialInfoTv = (TextView)itemView.findViewById(R.id.news_small_social_info);
        smallSourceTv = (TextView)itemView.findViewById(R.id.news_small_source);
    }

    @Override
    public void bindData(final Context context, NewsRelativeDelegate newsRelativeDelegate, RecyclerView.Adapter adapter, int position) {
        final NewsItemRelated item = newsRelativeDelegate.getSource();
        //TODO
        DisplayManager.getInstance(context).display("", smallThumbIv);
        if (TextUtils.isEmpty(item.title)) {
            smallTitleTv.setText("");
        } else {
            smallTitleTv.setText(item.title);
        }

        if (item.source != null) {
            smallSourceTv.setText(item.source);
        } else {
            smallSourceTv.setText("");
        }
        if (newsRelativeDelegate.isFirst() && newsRelativeDelegate.isLast()) {
            itemView.setBackgroundResource(R.drawable.std_list_item_round_bg);
        } else if (newsRelativeDelegate.isLast()) {
            itemView.setBackgroundResource(R.drawable.footer_round_conner_bg_sel);
        } else if (newsRelativeDelegate.isFirst()) {
            itemView.setBackgroundResource(R.drawable.header_round_conner_bg_sel);
        } else {
            itemView.setBackgroundResource(R.drawable.std_list_item_bg);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.newsDetailActivity(context, item.newsId);
            }
        });
    }
}

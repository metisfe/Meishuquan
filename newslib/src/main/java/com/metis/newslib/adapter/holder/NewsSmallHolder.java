package com.metis.newslib.adapter.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.manager.DisplayManager;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.newslib.ActivityDispatcher;
import com.metis.newslib.R;
import com.metis.newslib.adapter.delegate.NewsSmallDelegate;
import com.metis.newslib.module.NewsItem;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by Beak on 2015/9/2.
 */
public class NewsSmallHolder extends AbsViewHolder<NewsSmallDelegate> {

    public ImageView smallThumbIv = null;
    public TextView smallTitleTv = null;
    public TextView smallSourceTv = null;
    public TextView smallCommentTv, smallReadTv;

    public NewsSmallHolder(View itemView) {
        super(itemView);
        smallThumbIv = (ImageView)itemView.findViewById(R.id.news_small_thumb);
        smallTitleTv = (TextView)itemView.findViewById(R.id.news_small_title);
        smallSourceTv = (TextView)itemView.findViewById(R.id.news_small_source);
        smallCommentTv = (TextView)itemView.findViewById(R.id.news_small_comment_count);
        smallReadTv = (TextView)itemView.findViewById(R.id.news_small_read_count);
    }

    @Override
    public void bindData(final Context context, NewsSmallDelegate newsSmallDelegate, RecyclerView.Adapter adapter, int position) {
        final NewsItem item = newsSmallDelegate.getSource();
        DisplayManager.getInstance(context).display(item.imgUrl, smallThumbIv, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                if (view instanceof ImageView) {
                    ((ImageView)view).setImageDrawable(null);
                    view.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                }
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                view.setBackground(null);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        if (TextUtils.isEmpty(item.title)) {
            smallTitleTv.setText("");
        } else {
            smallTitleTv.setText(item.title);
        }

        if (item.source != null) {
            smallSourceTv.setText(item.source.title);
        } else {
            smallSourceTv.setText("");
        }
        if (item.commentCount > 0) {
            smallCommentTv.setText(context.getString(R.string.text_news_related_comment_count, item.commentCount));
        } else {
            smallCommentTv.setText("");
        }
        if (item.pageViewCount > 0) {
            smallReadTv.setText(context.getString(R.string.text_news_related_read_count, item.pageViewCount));
        } else {
            smallReadTv.setText("");
        }
        if (newsSmallDelegate.isAboveBig() && newsSmallDelegate.isBelowBig()) {
            itemView.setBackgroundResource(R.drawable.std_list_item_round_bg);
        } else if (newsSmallDelegate.isAboveBig()) {
            itemView.setBackgroundResource(R.drawable.footer_round_conner_bg_sel);
        } else if (newsSmallDelegate.isBelowBig()) {
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

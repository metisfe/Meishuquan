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
import com.metis.newslib.adapter.delegate.NewsBigDelegate;
import com.metis.newslib.module.NewsItem;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by Beak on 2015/9/2.
 */
public class NewsBigHolder extends AbsViewHolder<NewsBigDelegate> {

    public ImageView bigThumbIv = null;
    public TextView bigTitleTv = null;
    public TextView bigContentTv = null;

    public NewsBigHolder(View itemView) {
        super(itemView);

        bigThumbIv = (ImageView)itemView.findViewById(R.id.news_big_thumb);
        bigTitleTv = (TextView)itemView.findViewById(R.id.news_big_title);
        bigContentTv = (TextView)itemView.findViewById(R.id.news_big_content);
    }

    @Override
    public void bindData(final Context context, NewsBigDelegate newsBigDelegate, RecyclerView.Adapter adapter, int position) {
        final NewsItem item = newsBigDelegate.getSource();
        DisplayManager.getInstance(context).display(item.imgUrl, bigThumbIv, new ImageLoadingListener() {
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
        if (!TextUtils.isEmpty(item.title)) {
            bigTitleTv.setText(item.title.trim().replaceAll("\n", "").replaceAll(" ", ""));
        } else {
            bigTitleTv.setText("");
        }
        if (!TextUtils.isEmpty(item.desc)) {
            bigContentTv.setText(item.desc.trim().replaceAll("\n", "").replaceAll(" ", ""));
        } else {
            bigContentTv.setText("");
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.newsDetailActivity(context, item.newsId);
            }
        });
    }
}

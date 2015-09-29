package com.metis.newslib.adapter.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.metis.base.ActivityDispatcher;
import com.metis.base.manager.DisplayManager;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.newslib.R;
import com.metis.newslib.adapter.delegate.NewsDetailsImgDelegate;
import com.metis.newslib.module.NewsDetails;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by Beak on 2015/9/4.
 */
public class NewsDetailsImgHolder extends AbsViewHolder<NewsDetailsImgDelegate> {
    public ImageView imageView;
    public NewsDetailsImgHolder(View itemView) {
        super(itemView);
        imageView = (ImageView)itemView.findViewById(R.id.news_detail_img);
    }

    @Override
    public void bindData(final Context context, final NewsDetailsImgDelegate newsDetailsImgDelegate, RecyclerView.Adapter adapter, int position) {
        final NewsDetails.Item item = newsDetailsImgDelegate.getSource();
        if (newsDetailsImgDelegate.getWidth() > 0 && newsDetailsImgDelegate.getHeight() > 0) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)imageView.getLayoutParams();
            if (params != null) {
                params.width = newsDetailsImgDelegate.getWidth();
                params.height = newsDetailsImgDelegate.getHeight();
            } else {
                params = new RelativeLayout.LayoutParams(newsDetailsImgDelegate.getWidth(), newsDetailsImgDelegate.getHeight());
            }
            imageView.setLayoutParams(params);
        }

        DisplayManager.getInstance(context).display(item.data.URL, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                view.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                view.setBackground(null);
                final int width = context.getResources().getDisplayMetrics().widthPixels;
                final int height = (int)((float)width / bitmap.getWidth() * bitmap.getHeight());
                newsDetailsImgDelegate.setWidth(width);
                newsDetailsImgDelegate.setHeight(height);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.imagePreviewActivity(context, item.data);
            }
        });
    }
}

package com.metis.newslib.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.metis.base.ActivityDispatcher;
import com.metis.base.manager.DisplayManager;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.newslib.R;
import com.metis.newslib.adapter.delegate.NewsDetailsVdoDelegate;
import com.metis.newslib.module.NewsDetails;

/**
 * Created by Beak on 2015/9/8.
 */
public class NewsDetailsVdoHolder extends AbsViewHolder<NewsDetailsVdoDelegate> {

    private static final String TAG = NewsDetailsVdoHolder.class.getSimpleName();

    private ImageView mCoverIv = null;

    public NewsDetailsVdoHolder(View itemView) {
        super(itemView);

        mCoverIv = (ImageView)itemView.findViewById(R.id.details_video_cover);
    }

    @Override
    public void bindData(final Context context, NewsDetailsVdoDelegate newsDetailsVdoDelegate, RecyclerView.Adapter adapter, int position) {
        final NewsDetails.Item item = newsDetailsVdoDelegate.getSource();
        if (item != null && item.data != null) {

            DisplayManager.getInstance(context).display(item.data.ThumbnailsURL, mCoverIv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDispatcher.playVideo(context, item.data.URL);
                }
            });
        }
    }

}

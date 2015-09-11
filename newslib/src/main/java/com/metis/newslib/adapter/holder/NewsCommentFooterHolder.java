package com.metis.newslib.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.newslib.R;
import com.metis.newslib.adapter.delegate.NewsCommentFooterDelegate;

/**
 * Created by Beak on 2015/9/11.
 */
public class NewsCommentFooterHolder extends AbsViewHolder<NewsCommentFooterDelegate> {

    public TextView msgTv = null;

    public NewsCommentFooterHolder(View itemView) {
        super(itemView);
        msgTv = (TextView)itemView.findViewById(R.id.footer_msg);
    }

    @Override
    public void bindData(Context context, NewsCommentFooterDelegate newsCommentFooter, RecyclerView.Adapter adapter, int position) {
        msgTv.setText(newsCommentFooter.getSource());
        itemView.setOnClickListener(newsCommentFooter.getOnClickListener());
    }
}

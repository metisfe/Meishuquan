package com.metis.newslib.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.newslib.ActivityDispatcher;
import com.metis.newslib.R;
import com.metis.newslib.adapter.delegate.NewsRelativeDelegate;
import com.metis.newslib.module.NewsItemRelated;

import org.w3c.dom.Text;

/**
 * Created by Beak on 2015/9/4.
 */
public class NewsRelativeHolder extends AbsViewHolder<NewsRelativeDelegate> {

    public TextView contentTv, sourceTv, readTv, commentTv;
    public View dividerView;

    public NewsRelativeHolder(View itemView) {
        super(itemView);
        contentTv = (TextView)itemView.findViewById(R.id.news_item_related_content);
        sourceTv = (TextView)itemView.findViewById(R.id.news_item_related_source);
        readTv = (TextView)itemView.findViewById(R.id.news_item_related_read);
        commentTv = (TextView)itemView.findViewById(R.id.news_item_related_read);
        dividerView = itemView.findViewById(R.id.news_item_related_divider);
    }

    @Override
    public void bindData(final Context context, NewsRelativeDelegate newsRelativeDelegate, RecyclerView.Adapter adapter, int position) {
        final NewsItemRelated item = newsRelativeDelegate.getSource();
        if (TextUtils.isEmpty(item.title)) {
            contentTv.setText("");
        } else {
            contentTv.setText(item.title);
        }

        if (!TextUtils.isEmpty(item.source)) {
            sourceTv.setText(item.source);
        } else {
            sourceTv.setText("");
        }

        if (item.pageViewCount > 0) {
            readTv.setText(context.getString(R.string.text_news_related_read_count, item.pageViewCount));
        } else {
            readTv.setText("");
        }

        if (item.commentCount > 0) {
            commentTv.setText(context.getString(R.string.text_news_related_comment_count, item.commentCount));
        } else {
            commentTv.setText("");
        }

        dividerView.setVisibility(!TextUtils.isEmpty(item.source) && item.pageViewCount > 0 && item.commentCount > 0 ? View.VISIBLE : View.GONE);

        if (newsRelativeDelegate.isLast()) {
            itemView.setBackgroundResource(R.drawable.footer_round_conner_bg_sel);
        }

        /*if (newsRelativeDelegate.isFirst() && newsRelativeDelegate.isLast()) {
            itemView.setBackgroundResource(R.drawable.std_list_item_round_bg);
        } else if (newsRelativeDelegate.isLast()) {
            itemView.setBackgroundResource(R.drawable.footer_round_conner_bg_sel);
        } else if (newsRelativeDelegate.isFirst()) {
            itemView.setBackgroundResource(R.drawable.header_round_conner_bg_sel);
        } else {
            itemView.setBackgroundResource(R.drawable.std_list_item_bg);
        }*/
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.newsDetailActivity(context, item.newsId);
            }
        });
    }
}

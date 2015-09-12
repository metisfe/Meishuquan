package com.metis.newslib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.base.widget.adapter.delegate.DelegateType;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.base.widget.adapter.holder.FooterHolder;
import com.metis.newslib.adapter.delegate.NewsCardHeaderDelegate;
import com.metis.newslib.adapter.delegate.NewsCommentDelegate;
import com.metis.newslib.adapter.delegate.NewsCommentFooterDelegate;
import com.metis.newslib.adapter.delegate.NewsDetailsImgDelegate;
import com.metis.newslib.adapter.delegate.NewsDetailsTitleDelegate;
import com.metis.newslib.adapter.delegate.NewsDetailsTxtDelegate;
import com.metis.newslib.adapter.delegate.NewsDetailsVdoDelegate;
import com.metis.newslib.adapter.delegate.NewsRelativeDelegate;
import com.metis.newslib.adapter.holder.NewsCommentFooterHolder;
import com.metis.newslib.adapter.holder.NewsCommentHolder;
import com.metis.newslib.adapter.holder.NewsDetailsImgHolder;
import com.metis.newslib.adapter.holder.NewsDetailsTitleHolder;
import com.metis.newslib.adapter.holder.NewsDetailsTxtHolder;
import com.metis.newslib.adapter.holder.NewsDetailsVdoHolder;
import com.metis.newslib.adapter.holder.NewsCardHeaderHolder;
import com.metis.newslib.adapter.holder.NewsRelativeHolder;

import java.util.List;

/**
 * Created by Beak on 2015/9/2.
 */
public class NewsDetailAdapter extends DelegateAdapter {

    private static final String TAG = NewsDetailAdapter.class.getSimpleName();

    public NewsDetailAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case NewsDelegateType.ID.ID_NEWS_DETAIL_TITLE:
                return new NewsDetailsTitleHolder(view);
            case NewsDelegateType.ID.ID_NEWS_DETAIL_IMG:
                return new NewsDetailsImgHolder(view);
            case NewsDelegateType.ID.ID_NEWS_DETAIL_TXT:
                return new NewsDetailsTxtHolder(view);
            case NewsDelegateType.ID.ID_NEWS_DETAIL_VDO:
                return new NewsDetailsVdoHolder(view);
            case NewsDelegateType.ID.ID_NEWS_RELATIVE_ITEM:
                return new NewsRelativeHolder(view);
            case NewsDelegateType.ID.ID_NEWS_COMMENT_ITEM:
                return new NewsCommentHolder(view);
            case NewsDelegateType.ID.ID_NEWS_CARD_ITEM_HEADER:
                return new NewsCardHeaderHolder(view);
            case NewsDelegateType.ID.ID_NEWS_COMMENT_FOOTER:
                return new NewsCommentFooterHolder(view);
            case DelegateType.ID.ID_FOOTER:
                return new FooterHolder(view);
        }
        throw new UnknownError("no viewholder found for viewType=" + viewType + " in " + getClass().getName());
    }

    public void addNewsCommentDelegate (NewsCommentDelegate item, NewsCardHeaderDelegate headerDelegate) {
        final int length = getItemCount();
        int i = length - 1;
        for (; i >= 0; i--) {
            AbsDelegate delegate = getDataItem(i);
            if (/*delegate instanceof NewsCommentDelegate
                    || */delegate instanceof NewsRelativeDelegate
                    || delegate instanceof NewsDetailsImgDelegate
                    || delegate instanceof NewsDetailsTxtDelegate
                    || delegate instanceof NewsDetailsVdoDelegate
                    || delegate instanceof NewsDetailsTitleDelegate
                    || delegate instanceof NewsCommentFooterDelegate
                    || delegate instanceof NewsCardHeaderDelegate) {
                break;
            }
        }
        this.addDataItem(i + 1, item);
        if (headerDelegate != null) {
            this.addDataItem(i + 1, headerDelegate);
        }
        notifyDataSetChanged();
    }

    public boolean hasFooter () {
        final int length = getItemCount();
        for (int i = length - 1; i >= 0; i--) {
            AbsDelegate delegate = getDataItem(i);
            if (delegate instanceof FooterDelegate) {
                return true;
            }
        }
        return false;
    }

}

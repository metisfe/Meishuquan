package com.metis.newslib.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.newslib.adapter.NewsDelegateType;
import com.metis.newslib.module.NewsCommentItem;
import com.metis.newslib.module.NewsDetails;

/**
 * Created by Beak on 2015/9/4.
 */
public class NewsCommentDelegate extends BaseDelegate<NewsCommentItem> {

    private NewsDetails mDetails = null;

    public NewsCommentDelegate(NewsCommentItem newsCommentItem) {
        super(newsCommentItem);
    }

    @Override
    public int getDelegateType() {
        return NewsDelegateType.TYPE_NEWS_COMMENT_ITEM.getTypeId();
    }

    public void setDetails (NewsDetails details) {
        mDetails = details;
    }

    public NewsDetails getDetails () {
        return mDetails;
    }
}

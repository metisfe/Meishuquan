package com.metis.newslib.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.newslib.adapter.NewsDelegateType;
import com.metis.newslib.module.NewsDetails;

/**
 * Created by Beak on 2015/9/4.
 */
public class NewsDetailsTitleDelegate extends BaseDelegate<NewsDetails> {

    public NewsDetailsTitleDelegate(NewsDetails details) {
        super(details);
    }

    @Override
    public int getDelegateType() {
        return NewsDelegateType.TYPE_NEWS_DETAILS_TITLE.getTypeId();
    }
}

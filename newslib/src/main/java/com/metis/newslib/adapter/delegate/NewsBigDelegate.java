package com.metis.newslib.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.newslib.adapter.NewsDelegateType;
import com.metis.newslib.module.NewsItem;

/**
 * Created by Beak on 2015/9/2.
 */
public class NewsBigDelegate extends BaseDelegate<NewsItem> {

    public NewsBigDelegate(NewsItem newsItem) {
        super(newsItem);
    }

    @Override
    public int getDelegateType() {
        return NewsDelegateType.TYPE_NEWS_BIG.getTypeId();
    }
}

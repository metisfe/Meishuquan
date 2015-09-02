package com.metis.newslib.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.newslib.adapter.NewsDelegateType;
import com.metis.newslib.module.NewsItem;

/**
 * Created by Beak on 2015/9/2.
 */
public class NewsSmallDelegate extends BaseDelegate<NewsItem> {

    private boolean isBelowBig = false;
    private boolean isAboveBig = false;

    public NewsSmallDelegate(NewsItem newsItem) {
        super(newsItem);
    }

    @Override
    public int getDelegateType() {
        return NewsDelegateType.TYPE_NEWS_SMALL.getTypeId();
    }

    public void belowBig (boolean belowBig) {
        isBelowBig = belowBig;
    }

    public void aboveBig (boolean aboveBig) {
        isAboveBig = aboveBig;
    }

    public boolean isBelowBig() {
        return isBelowBig;
    }

    public boolean isAboveBig() {
        return isAboveBig;
    }
}

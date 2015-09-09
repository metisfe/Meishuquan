package com.metis.newslib.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.newslib.adapter.NewsDelegateType;
import com.metis.newslib.module.NewsItemRelated;

/**
 * Created by Beak on 2015/9/4.
 */
public class NewsRelativeDelegate extends BaseDelegate<NewsItemRelated> {

    private boolean isFirst = false, isLast = false;

    public NewsRelativeDelegate(NewsItemRelated newsItemRelated) {
        super(newsItemRelated);
    }

    @Override
    public int getDelegateType() {
        return NewsDelegateType.TYPE_NEWS_RELATIVE_ITEM.getTypeId();
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }
}

package com.metis.newslib.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.newslib.adapter.NewsDelegateType;
import com.metis.newslib.module.NewsDetails;

/**
 * Created by Beak on 2015/9/4.
 */
public class NewsDetailsTxtDelegate extends BaseDelegate<NewsDetails.Item> {

    private NewsDetails mDetails;

    public NewsDetailsTxtDelegate(NewsDetails.Item details) {
        super(details);
    }

    @Override
    public int getDelegateType() {
        return NewsDelegateType.TYPE_NEWS_DETAILS_TXT.getTypeId();
    }

    public NewsDetails getDetails() {
        return mDetails;
    }

    public void setDetails(NewsDetails details) {
        this.mDetails = details;
    }
}

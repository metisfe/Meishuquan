package com.metis.newslib.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.newslib.adapter.NewsDelegateType;
import com.metis.newslib.module.NewsDetails;

/**
 * Created by Beak on 2015/9/8.
 */
public class NewsDetailsVdoDelegate extends BaseDelegate<NewsDetails.Item> {

    private int mCurrent = 0;

    public NewsDetailsVdoDelegate(NewsDetails.Item item) {
        super(item);
    }

    @Override
    public int getDelegateType() {
        return NewsDelegateType.TYPE_NEWS_DETAILS_VDO.getTypeId();
    }

    public void setCurrent (int current) {
        mCurrent = current;
    }

    public int getCurrent () {
        return mCurrent;
    }
}

package com.metis.newslib.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.newslib.adapter.NewsDelegateType;

/**
 * Created by Beak on 2015/9/11.
 */
public class NewsCardHeaderDelegate extends BaseDelegate<String> {

    public NewsCardHeaderDelegate(String details) {
        super(details);
    }

    @Override
    public int getDelegateType() {
        return NewsDelegateType.TYPE_NEWS_CARD_HEADER.getTypeId();
    }
}

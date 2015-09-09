package com.metis.newslib.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.newslib.adapter.NewsDelegateType;
import com.metis.newslib.module.NewsDetails;

/**
 * Created by Beak on 2015/9/4.
 */
public class NewsDetailsImgDelegate extends BaseDelegate<NewsDetails.Item> {

    private NewsDetails mDetails;

    private int width, height;

    public NewsDetailsImgDelegate(NewsDetails.Item details) {
        super(details);
    }

    @Override
    public int getDelegateType() {
        return NewsDelegateType.TYPE_NEWS_DETAILS_IMG.getTypeId();
    }

    public NewsDetails getDetails() {
        return mDetails;
    }

    public void setDetails(NewsDetails details) {
        this.mDetails = details;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

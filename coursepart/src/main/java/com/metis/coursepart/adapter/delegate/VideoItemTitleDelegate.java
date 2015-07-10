package com.metis.coursepart.adapter.delegate;

import com.metis.base.widget.delegate.BaseDelegate;

/**
 * Created by Beak on 2015/7/10.
 */
public class VideoItemTitleDelegate extends BaseDelegate<String> {

    public VideoItemTitleDelegate(String s) {
        super(s);
    }

    @Override
    public int getDelegateType() {
        return CourseDelegateType.TYPE_VIDEO_ITEM_SMALL_TITLE.getType();
    }
}

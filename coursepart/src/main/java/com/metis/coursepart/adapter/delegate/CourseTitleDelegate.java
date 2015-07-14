package com.metis.coursepart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;

/**
 * Created by Beak on 2015/7/14.
 */
public class CourseTitleDelegate extends BaseDelegate<String> {
    public CourseTitleDelegate(String s) {
        super(s);
    }

    @Override
    public int getDelegateType() {
        return CourseDelegateType.TYPE_COURSE_TITLE.getType();
    }
}

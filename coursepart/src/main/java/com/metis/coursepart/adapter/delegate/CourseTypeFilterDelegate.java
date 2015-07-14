package com.metis.coursepart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.coursepart.adapter.FilterAdapter;
import com.metis.coursepart.module.CourseType;

/**
 * Created by Beak on 2015/7/14.
 */
public class CourseTypeFilterDelegate extends BaseDelegate<CourseType> implements FilterAdapter.FilterSelectable{

    private boolean isSelected = false;

    public CourseTypeFilterDelegate(CourseType courseType) {
        super(courseType);
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public long getFilterId() {
        return getSource().channelid;
    }

    @Override
    public int getDelegateType() {
        return CourseDelegateType.TYPE_FILTER_COURSE_TYPE.getType();
    }
}

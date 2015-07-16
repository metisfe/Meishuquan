package com.metis.coursepart.adapter.delegate;

import com.metis.base.widget.Selectable;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.coursepart.module.Course;

/**
 * Created by Beak on 2015/7/14.
 */
public class CourseDelegate extends BaseDelegate<Course> implements Selectable{

    private boolean isSelected = false;

    public CourseDelegate(Course course) {
        super(course);
    }

    @Override
    public int getDelegateType() {
        return CourseDelegateType.TYPE_COURSE_ITEM.getType();
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

}

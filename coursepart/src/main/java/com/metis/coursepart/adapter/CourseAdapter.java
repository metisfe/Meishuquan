package com.metis.coursepart.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.coursepart.adapter.delegate.CourseDelegateType;
import com.metis.coursepart.adapter.holder.CourseHolder;
import com.metis.coursepart.adapter.holder.CourseTitleHolder;

/**
 * Created by Beak on 2015/7/14.
 */
public class CourseAdapter extends DelegateAdapter {
    public CourseAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case CourseDelegateType.ID.ID_COURSE_ITEM:
                return new CourseHolder(view);
            case CourseDelegateType.ID.ID_COURSE_TITLE:
                return new CourseTitleHolder(view);
        }
        throw new Resources.NotFoundException("not viewholder found for viewtype:" + viewType + " in " + this.getClass().getSimpleName());
    }
}

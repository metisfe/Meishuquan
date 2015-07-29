package com.metis.coursepart.adapter.delegate;

import com.metis.base.module.User;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.coursepart.adapter.FilterAdapter;

/**
 * Created by Beak on 2015/7/14.
 */
public class StudioFilterDelegate extends BaseDelegate<User> implements FilterAdapter.FilterSelectable{

    private boolean isSelected = false;

    public StudioFilterDelegate(User studioInfo) {
        super(studioInfo);
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
        return getSource().userId;
    }

    @Override
    public int getDelegateType() {
        return CourseDelegateType.TYPE_FILTER_STUDIO.getType();
    }
}

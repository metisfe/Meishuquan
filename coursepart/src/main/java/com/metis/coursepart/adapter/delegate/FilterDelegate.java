package com.metis.coursepart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.coursepart.adapter.FilterAdapter;
import com.metis.coursepart.module.Filter;

/**
 * Created by Beak on 2015/7/14.
 */
public class FilterDelegate extends BaseDelegate<Filter> implements FilterAdapter.FilterSelectable {

    private boolean isSelected = false;

    public FilterDelegate(Filter filter) {
        super(filter);
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
        return getSource().id;
    }

    @Override
    public int getDelegateType() {
        return CourseDelegateType.TYPE_FILTER.getType();
    }

}

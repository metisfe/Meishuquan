package com.metis.commentpart.module;

import com.metis.base.widget.filter.FilterProvider;

/**
 * Created by Beak on 2015/8/4.
 */
public class AreaFilter implements FilterProvider {

    private AssessCRegionItem mItem = null;
    private boolean isChecked = false;

    public AreaFilter (AssessCRegionItem regionItem) {
        mItem = regionItem;
    }

    @Override
    public long getFilterId() {
        return mItem.codeid;
    }

    @Override
    public String getFilterName() {
        return mItem.cityName;
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        isChecked = !isChecked;
    }
}

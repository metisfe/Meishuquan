package com.metis.commentpart.module;

import com.metis.base.widget.filter.FilterProvider;

/**
 * Created by Beak on 2015/8/4.
 */
public class CategoryFilter implements FilterProvider {

    private ChannelItem mItem = null;
    private boolean isChecked = false;

    public CategoryFilter (ChannelItem channel) {
        mItem = channel;
    }

    @Override
    public long getFilterId() {
        return mItem.channelId;
    }

    @Override
    public String getFilterName() {
        return mItem.name;
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

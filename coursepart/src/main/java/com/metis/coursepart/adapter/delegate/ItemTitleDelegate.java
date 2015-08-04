package com.metis.coursepart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.base.widget.adapter.delegate.DelegateType;
import com.metis.coursepart.adapter.FilterAdapter;

/**
 * Created by Beak on 2015/7/10.
 */
public class ItemTitleDelegate extends BaseDelegate<String> implements FilterAdapter.FilterSelectable{

    private boolean isClickable = false;
    private long mFilterId = 0;

    public ItemTitleDelegate(String s) {
        super(s);
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    @Override
    public int getDelegateType() {
        return DelegateType.TYPE_ITEM_SMALL_TITLE.getType();
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void setSelected(boolean isSelected) {

    }

    @Override
    public long getFilterId() {
        return mFilterId;
    }

    public void setFilterId (long id) {
        mFilterId = id;
    }
}

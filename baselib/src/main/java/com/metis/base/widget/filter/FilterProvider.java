package com.metis.base.widget.filter;

import android.widget.Checkable;

/**
 * Created by Beak on 2015/7/31.
 */
public interface FilterProvider extends Checkable {

    public long getFilterId ();
    public String getFilterName ();

}

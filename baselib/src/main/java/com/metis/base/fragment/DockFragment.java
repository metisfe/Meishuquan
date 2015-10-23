package com.metis.base.fragment;

import android.content.Context;

import com.metis.base.widget.dock.DockBar;
import com.metis.base.widget.dock.Dockable;

/**
 * Created by Beak on 2015/7/2.
 */
public abstract class DockFragment extends BaseFragment implements Dockable {
    @Override
    public String getCustomTag() {
        return getClass().getSimpleName();
    }
}

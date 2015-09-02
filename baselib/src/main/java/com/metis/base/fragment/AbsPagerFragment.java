package com.metis.base.fragment;

import android.content.Context;

/**
 * Created by Beak on 2015/9/1.
 */
public abstract class AbsPagerFragment extends BaseFragment {
    public abstract CharSequence getTitle (Context context);

    public void onSelected () {

    }
}
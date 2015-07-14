package com.metis.coursepart.module;

import android.support.annotation.StringRes;

import com.metis.coursepart.R;

/**
 * Created by Beak on 2015/7/14.
 */
public class Filter {

    public static final Filter ALL = new Filter(0, R.string.filter_all);

    public int id;
    @StringRes
    public int stringRes;

    public Filter (int id, @StringRes int stringRes) {
        this.id = id;
        this.stringRes = stringRes;
    }

}

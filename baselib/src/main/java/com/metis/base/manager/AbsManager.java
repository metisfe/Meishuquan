package com.metis.base.manager;

import android.content.Context;

/**
 * Created by Beak on 2015/7/3.
 */
public abstract class AbsManager {

    private Context mContext = null;

    public AbsManager (Context context) {
        mContext = context;
    }

    public Context getContext () {
        return mContext;
    }
}

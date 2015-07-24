package com.metis.base.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Beak on 2015/7/3.
 */
public abstract class AbsManager {

    private Context mContext = null;

    private Gson mGson = null;

    public AbsManager (Context context) {
        mContext = context;
        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

    public Context getContext () {
        return mContext;
    }

    public Gson getGson () {
        return mGson;
    }

}

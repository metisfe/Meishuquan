package com.metis.base.module;

import android.view.View;

/**
 * Created by Beak on 2015/8/4.
 */
public class Title {

    private String mTitle, mSubTitle;
    private View.OnClickListener mListener = null;

    public Title (String title, String subTitle, View.OnClickListener listener) {
        mTitle = title;
        mSubTitle = subTitle;
        mListener = listener;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public void setSubTitle(String mSubTitle) {
        this.mSubTitle = mSubTitle;
    }

    public View.OnClickListener getListener() {
        return mListener;
    }

    public void setListener(View.OnClickListener mListener) {
        this.mListener = mListener;
    }
}

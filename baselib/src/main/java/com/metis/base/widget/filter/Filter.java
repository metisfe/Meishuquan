package com.metis.base.widget.filter;

/**
 * Created by Beak on 2015/7/31.
 */
public class Filter {

    private int mId;
    private CharSequence mTitle;

    public Filter (int id, CharSequence title) {
        mId = id;
        mTitle = title;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public void setTitle(CharSequence mTitle) {
        this.mTitle = mTitle;
    }
}

package com.metis.commentpart.module;

import android.view.View;
import android.widget.CompoundButton;

/**
 * Created by Beak on 2015/8/5.
 */
public class StatusDetailTabItem {
    private String mTextLeft;
    private String mTextRight;
    private OnTabSelectListener mTabListener;

    public String getTextLeft() {
        return mTextLeft;
    }

    public void setTextLeft(String mTextLeft) {
        this.mTextLeft = mTextLeft;
    }

    public String getTextRight() {
        return mTextRight;
    }

    public void setTextRight(String mTextRight) {
        this.mTextRight = mTextRight;
    }

    public void setOnTabSelectListener (OnTabSelectListener listener) {
        mTabListener = listener;
    }

    public OnTabSelectListener getOnTabSelectListener () {
        return mTabListener;
    }

    public static interface OnTabSelectListener {
        public void onLeftSelected ();
        public void onRightSelected ();
    }
}

package com.metis.commentpart.module;

import android.view.View;

/**
 * Created by Beak on 2015/8/5.
 */
public class StatusDetailTabItem {
    private String mTextLeft;
    private View.OnClickListener mOnClickListenerLeft;
    private String mTextRight;
    private View.OnClickListener mOnClickListenerRight;

    public String getTextLeft() {
        return mTextLeft;
    }

    public void setTextLeft(String mTextLeft) {
        this.mTextLeft = mTextLeft;
    }

    public View.OnClickListener getOnClickListenerLeft() {
        return mOnClickListenerLeft;
    }

    public void setOnClickListenerLeft(View.OnClickListener mOnClickListenerLeft) {
        this.mOnClickListenerLeft = mOnClickListenerLeft;
    }

    public String getTextRight() {
        return mTextRight;
    }

    public void setTextRight(String mTextRight) {
        this.mTextRight = mTextRight;
    }

    public View.OnClickListener getOnClickListenerRight() {
        return mOnClickListenerRight;
    }

    public void setOnClickListenerRight(View.OnClickListener mOnClickListenerRight) {
        this.mOnClickListenerRight = mOnClickListenerRight;
    }
}

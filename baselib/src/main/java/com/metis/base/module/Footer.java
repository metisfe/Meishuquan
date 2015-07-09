package com.metis.base.module;

/**
 * Created by Beak on 2015/7/8.
 */
public class Footer {
    public static final int
            STATE_IDLE = 0,
            STATE_WAITTING = 1,
            STATE_SUCCESS = 2,
            STATE_FAILED = 3,
            STATE_NO_MORE = 4;

    private int mState = STATE_IDLE;

    public Footer (int state) {
        mState = state;
    }

    public Footer () {
        this (STATE_IDLE);
    }

    public int getState() {
        return mState;
    }

    public void setState(int mState) {
        this.mState = mState;
    }
}

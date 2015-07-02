package com.metis.base.widget.delegate;

/**
 * Created by gaoyunfei on 15/6/15.
 */
public abstract class BaseDelegate<T> extends AbsDelegate<T> {

    public static final int STATE_IDLE = 0, STATE_ILLEGAL = -1, STATE_EXCUTING = 1;

    private boolean accessable = true;
    private long mLocalCreateTime = System.currentTimeMillis();
    private String mTag = null;
    private int mState = STATE_IDLE;

    public BaseDelegate(T t) {
        super(t);
    }

    public boolean isAccessable() {
        return accessable;
    }

    public void setAccessable(boolean accessable) {
        this.accessable = accessable;
    }

    public long getLocalCreateTime() {
        return mLocalCreateTime;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String mTag) {
        this.mTag = mTag;
    }

    public void setState (int state) {
        mState = state;
    }

    public int getState () {
        return mState;
    }
}

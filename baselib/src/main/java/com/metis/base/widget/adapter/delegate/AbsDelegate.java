package com.metis.base.widget.adapter.delegate;

/**
 * Created by gaoyunfei on 15/5/22.
 */
public abstract class AbsDelegate<T> {

    private T t = null;

    public AbsDelegate(T t) {
        this.t = t;
    }

    public T getSource () {
        return t;
    }

    public abstract int getDelegateType ();

    public void setSource (T t) {
        this.t = t;
    }


}

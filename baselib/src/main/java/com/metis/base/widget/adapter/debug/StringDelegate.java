package com.metis.base.widget.adapter.debug;

import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.base.widget.adapter.delegate.DelegateType;

/**
 * Created by gaoyunfei on 15/7/12.
 */
public class StringDelegate extends BaseDelegate<String> {

    public StringDelegate(String s) {
        super(s);
    }

    @Override
    public int getDelegateType() {
        return DelegateType.TYPE_DEBUG_STRING.getType();
    }
}

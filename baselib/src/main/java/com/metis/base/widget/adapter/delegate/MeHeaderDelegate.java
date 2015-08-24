package com.metis.base.widget.adapter.delegate;

import com.metis.base.module.User;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.base.widget.adapter.MeDelegateType;

/**
 * Created by Beak on 2015/8/24.
 */
public class MeHeaderDelegate extends BaseDelegate<User> {
    public MeHeaderDelegate(User me) {
        super(me);
    }

    @Override
    public int getDelegateType() {
        return MeDelegateType.TYPE_ME_HEADER.getType();
    }
}

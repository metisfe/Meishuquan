package com.metis.coursepart.adapter.delegate;

import com.metis.base.module.User;
import com.metis.base.widget.adapter.delegate.BaseDelegate;

/**
 * Created by gaoyunfei on 15/7/12.
 */
public class AuthorDelegate extends BaseDelegate<User> {

    public AuthorDelegate(User author) {
        super(author);
    }

    @Override
    public int getDelegateType() {
        return 0;
    }
}

package com.metis.coursepart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.coursepart.module.Author;

/**
 * Created by gaoyunfei on 15/7/12.
 */
public class AuthorDelegate extends BaseDelegate<Author> {

    public AuthorDelegate(Author author) {
        super(author);
    }

    @Override
    public int getDelegateType() {
        return 0;
    }
}

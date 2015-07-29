package com.metis.commentpart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;

/**
 * Created by Beak on 2015/7/28.
 */
public class StatusDelegate extends BaseDelegate<String[]> {

    public StatusDelegate(String[] strings) {
        super(strings);
    }

    @Override
    public int getDelegateType() {
        return CommentDelegateType.TYPE_STATUS_ITEM.getType();
    }
}

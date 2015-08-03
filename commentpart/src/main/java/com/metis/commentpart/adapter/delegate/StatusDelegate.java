package com.metis.commentpart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.commentpart.module.Status;

/**
 * Created by Beak on 2015/7/28.
 */
public class StatusDelegate extends BaseDelegate<Status> {

    private int mItemHeight = 0;

    public StatusDelegate(Status status) {
        super(status);
    }

    @Override
    public int getDelegateType() {
        return CommentDelegateType.TYPE_STATUS_ITEM.getType();
    }

    public void setItemHeight (int height) {
        mItemHeight = height;
    }

    public int getItemHeight () {
        return mItemHeight;
    }
}

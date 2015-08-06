package com.metis.commentpart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.commentpart.module.StatusDetailTabItem;

/**
 * Created by Beak on 2015/8/5.
 */
public class StatusDetailTabDelegate extends BaseDelegate<StatusDetailTabItem> {

    public StatusDetailTabDelegate(StatusDetailTabItem statusDetailTabItem) {
        super(statusDetailTabItem);
    }

    @Override
    public int getDelegateType() {
        return CommentDelegateType.TYPE_STATUS_DETAIL_TAB.getType();
    }
}

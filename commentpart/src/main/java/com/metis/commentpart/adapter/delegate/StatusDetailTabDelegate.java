package com.metis.commentpart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.commentpart.R;
import com.metis.commentpart.module.StatusDetailTabItem;

/**
 * Created by Beak on 2015/8/5.
 */
public class StatusDetailTabDelegate extends BaseDelegate<StatusDetailTabItem> {

    private int mCurrentCheckedId = R.id.tab_teacher;

    public StatusDetailTabDelegate(StatusDetailTabItem statusDetailTabItem) {
        super(statusDetailTabItem);
    }

    @Override
    public int getDelegateType() {
        return CommentDelegateType.TYPE_STATUS_DETAIL_TAB.getType();
    }

    public void setCurrentCheckedId (int id) {
        mCurrentCheckedId = id;
    }
    public int getCurrentCheckedId () {
        return mCurrentCheckedId;
    }

    public boolean isLeft () {
        return getCurrentCheckedId() == R.id.tab_teacher;
    }
}

package com.metis.commentpart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.Status;

/**
 * Created by Beak on 2015/8/6.
 */
public abstract class CommentDelegate extends BaseDelegate<Comment> {

    private Status mStatus = null;

    public CommentDelegate(Comment comment, Status status) {
        super(comment);
        mStatus = status;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status mStatus) {
        this.mStatus = mStatus;
    }
}

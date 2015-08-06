package com.metis.commentpart.adapter.delegate;

import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.Status;

/**
 * Created by Beak on 2015/8/6.
 */
public class CommentItemDelegate extends CommentDelegate {

    public CommentItemDelegate(Comment comment, Status status) {
        super(comment, status);
    }

    @Override
    public int getDelegateType() {
        return CommentDelegateType.TYPE_COMMENT_LIST_ITEM.getType();
    }
}

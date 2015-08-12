package com.metis.commentpart.adapter.delegate;

import android.view.View;

import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.Status;

/**
 * Created by Beak on 2015/8/6.
 */
public class CommentItemDelegate extends CommentDelegate {

    private OnCommentActionListener mActionListener = null;

    public CommentItemDelegate(Comment comment, Status status) {
        super(comment, status);
    }

    @Override
    public int getDelegateType() {
        return CommentDelegateType.TYPE_COMMENT_LIST_ITEM.getType();
    }

    public void setOnCommentActionListener (OnCommentActionListener listener) {
        mActionListener = listener;
    }

    public OnCommentActionListener getOnCommentActionListener () {
        return mActionListener;
    }

    public static interface OnCommentActionListener {
        public void onClick (View v, Comment comment, Status status);
        public void onLongClick (View v, Comment comment, Status status);
    }
}

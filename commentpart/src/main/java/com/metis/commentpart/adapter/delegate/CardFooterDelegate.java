package com.metis.commentpart.adapter.delegate;

import android.view.View;

import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.Status;

/**
 * Created by Beak on 2015/8/5.
 */
public class CardFooterDelegate extends CommentDelegate {

    public static final int REPLY_TYPE_TEXT = 0, REPLY_TYPE_VOICE = 1;

    private OnCommentFooterClickListener mCommentListener = null;

    public CardFooterDelegate(Comment comment, Status status) {
        super(comment, status);
    }

    @Override
    public int getDelegateType() {
        return CommentDelegateType.TYPE_COMMENT_CARD_FOOTER.getType();
    }

    public void setOnCommentFooterClickListener (OnCommentFooterClickListener listener) {
        mCommentListener = listener;
    }

    public OnCommentFooterClickListener getOnCommentFooterClickListener () {
        return mCommentListener;
    }

    public static interface OnCommentFooterClickListener {
        public void onClick (View view, Comment comment, int replyType);
    }
}

package com.metis.commentpart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.commentpart.module.Comment;

/**
 * Created by Beak on 2015/8/5.
 */
public class CardTextTDelegate extends BaseDelegate<Comment> {

    public CardTextTDelegate(Comment comment) {
        super(comment);
    }

    @Override
    public int getDelegateType() {
        return CommentDelegateType.TYPE_COMMENT_CARD_TEXT_T.getType();
    }
}

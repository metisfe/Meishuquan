package com.metis.commentpart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.commentpart.module.Comment;

/**
 * Created by Beak on 2015/8/10.
 */
public class CardVoiceTDelegate extends BaseDelegate<Comment> {

    public CardVoiceTDelegate(Comment comment) {
        super(comment);
    }

    @Override
    public int getDelegateType() {
        return CommentDelegateType.TYPE_COMMENT_CARD_VOICE_T.getType();
    }
}

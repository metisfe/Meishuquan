package com.metis.commentpart.utils;

import com.metis.base.module.User;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.commentpart.adapter.delegate.CardTextSDelegate;
import com.metis.commentpart.adapter.delegate.CardTextTDelegate;
import com.metis.commentpart.adapter.delegate.CardVoiceTDelegate;
import com.metis.commentpart.module.Comment;

/**
 * Created by Beak on 2015/8/5.
 */
public final class CommentFactory {
    private CommentFactory () {

    }

    public static BaseDelegate makeCommentDelegate (Comment comment) {
        User user = comment.user;
        int commentType = comment.commentType;
        switch (user.userRole) {
            case User.USER_ROLE_STUDIO:
            case User.USER_ROLE_TEACHER:
                switch (commentType) {
                    case Comment.COMMENT_TYPE_TEXT:
                        return new CardTextTDelegate(comment);
                    case Comment.COMMENT_TYPE_VOICE:
                        return new CardVoiceTDelegate(comment);
                }

            case User.USER_ROLE_STUDENT:
            case User.USER_ROLE_PARENTS:
                return new CardTextSDelegate(comment);
        }
        return null;
    }

    public static int getCommentSource (User user) {
        switch (user.userRole) {
            case User.USER_ROLE_STUDIO:
            case User.USER_ROLE_TEACHER:
                return 0;
            case User.USER_ROLE_STUDENT:
            case User.USER_ROLE_PARENTS:
                return 1;
        }
        return 1;
    }
}

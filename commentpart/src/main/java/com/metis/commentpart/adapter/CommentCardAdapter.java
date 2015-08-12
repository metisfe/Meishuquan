package com.metis.commentpart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.module.User;
import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.base.widget.adapter.holder.FooterHolder;
import com.metis.commentpart.adapter.delegate.CardFooterDelegate;
import com.metis.commentpart.adapter.delegate.CardHeaderDelegate;
import com.metis.commentpart.adapter.delegate.CommentDelegateType;
import com.metis.commentpart.adapter.holder.CardFooterHolder;
import com.metis.commentpart.adapter.holder.CardHeaderHolder;
import com.metis.commentpart.adapter.holder.CardTextSHolder;
import com.metis.commentpart.adapter.holder.CardTextTHolder;
import com.metis.commentpart.adapter.holder.CardVoiceTHolder;
import com.metis.commentpart.adapter.holder.StatusDetailTabHolder;
import com.metis.commentpart.adapter.holder.StatusHolder;
import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.Status;
import com.metis.commentpart.utils.CommentFactory;

/**
 * Created by Beak on 2015/8/5.
 */
public class CommentCardAdapter extends DelegateAdapter {

    public CommentCardAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case CommentDelegateType.ID.ID_STATUS_ITEM:
                return new StatusHolder(view);
            case CommentDelegateType.ID.ID_STATUS_DETAIL_TAB:
                return new StatusDetailTabHolder(view);
            case CommentDelegateType.ID.ID_COMMENT_CARD_HEADER:
                return new CardHeaderHolder(view);
            case CommentDelegateType.ID.ID_COMMENT_CARD_FOOTER:
                return new CardFooterHolder(view);
            case CommentDelegateType.ID.ID_COMMENT_CARD_TEXT_T:
                return new CardTextTHolder(view);
            case CommentDelegateType.ID.ID_COMMENT_CARD_TEXT_S:
                return new CardTextSHolder(view);
            case CommentDelegateType.ID.ID_COMMENT_CARD_VOICE_T:
                return new CardVoiceTHolder(view);
        }
        throw new IllegalArgumentException("CommentCardAdapter onCreateAbsViewHolder return null");
    }

    public Comment getMyCardHeader (long userId) {
        final int length = getItemCount();
        for (int i = 0; i < length; i++) {
            AbsDelegate delegate = getDataItem(i);
            if (delegate instanceof CardHeaderDelegate) {
                CardHeaderDelegate headerDelegate = (CardHeaderDelegate)delegate;
                Comment comment = headerDelegate.getSource();
                if (comment.user.userId == userId) {
                    return comment;
                }
            }
        }
        return null;
    }

    public void addCommentFollow (Status status, Comment comment, Comment newComment) {
        if (comment == null) {
            addDataItem(new CardHeaderDelegate(newComment));
            addDataItem(CommentFactory.makeCommentDelegate(newComment));
            addDataItem(new CardFooterDelegate(newComment, status));
            return;
        }
        final int length = getItemCount();
        int i = 0;
        for (i = 0; i < length; i++) {
            AbsDelegate delegate = getDataItem(i);
            if (delegate instanceof CardFooterDelegate) {
                break;
            }
        }
        addDataItem(i, CommentFactory.makeCommentDelegate(newComment));
    }

    public int getCommentCardCount () {
        final int length = getItemCount();
        int count = 0;
        for (int i = 0; i < length; i++) {
            if (getDataItem(i) instanceof CardHeaderDelegate) {
                count++;
            }
        }
        return count;
    }

    public Comment getMyCardHeader (User user) {
        return getMyCardHeader(user.userId);
    }
}

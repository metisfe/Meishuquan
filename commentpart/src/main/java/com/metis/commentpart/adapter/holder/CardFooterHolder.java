package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.manager.AccountManager;
import com.metis.base.module.User;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.CardFooterDelegate;
import com.metis.commentpart.adapter.delegate.CommentDelegate;
import com.metis.commentpart.module.Status;

/**
 * Created by Beak on 2015/8/5.
 */
public class CardFooterHolder extends AbsViewHolder<CardFooterDelegate> {

    public TextView replyBtn;
    public ImageView replyVoiceBtn;

    public CardFooterHolder(View itemView) {
        super(itemView);
        replyBtn = (TextView)itemView.findViewById(R.id.footer_reply_btn);
        replyVoiceBtn = (ImageView)itemView.findViewById(R.id.footer_reply_voice_btn);
    }

    @Override
    public void bindData(Context context, final CardFooterDelegate cardFooterDelegate, RecyclerView.Adapter adapter, int position) {
        User commentUser = cardFooterDelegate.getSource().user;
        Status status = cardFooterDelegate.getStatus();
        User statusUser = status.user;
        User me = AccountManager.getInstance(context).getMe();
        replyBtn.setVisibility(statusUser.equals(me) || commentUser.equals(me) ? View.VISIBLE : View.GONE);
        replyVoiceBtn.setVisibility(me == null || me.userRole == User.USER_ROLE_STUDIO || me.userRole == User.USER_ROLE_TEACHER ? View.VISIBLE : View.GONE);
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardFooterDelegate.getOnCommentFooterClickListener() != null) {
                    cardFooterDelegate.getOnCommentFooterClickListener().onClick(v, cardFooterDelegate.getSource(), CardFooterDelegate.REPLY_TYPE_TEXT);
                }
            }
        });
        replyVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardFooterDelegate.getOnCommentFooterClickListener() != null) {
                    cardFooterDelegate.getOnCommentFooterClickListener().onClick(v, cardFooterDelegate.getSource(), CardFooterDelegate.REPLY_TYPE_VOICE);
                }
            }
        });
    }
}

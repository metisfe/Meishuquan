package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.base.ActivityDispatcher;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.module.UserMark;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.CommentItemDelegate;
import com.metis.commentpart.manager.StatusManager;
import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.Status;

/**
 * Created by Beak on 2015/8/6.
 */
public class CommentItemHolder extends AbsViewHolder<CommentItemDelegate> {

    private static final String TAG = CommentItemHolder.class.getSimpleName();

    public ImageView profileIv, supportIv;
    public TextView nameTv, contentTv, timeTv, supportCountTv;

    public CommentItemHolder(View itemView) {
        super(itemView);

        profileIv = (ImageView)itemView.findViewById(R.id.comment_list_item_profile);
        supportIv = (ImageView)itemView.findViewById(R.id.comment_list_item_support);
        nameTv = (TextView)itemView.findViewById(R.id.comment_list_item_name);
        contentTv = (TextView)itemView.findViewById(R.id.comment_list_item_content);
        timeTv = (TextView)itemView.findViewById(R.id.comment_list_item_time);
        supportCountTv = (TextView)itemView.findViewById(R.id.comment_list_item_support_count);
    }

    @Override
    public void bindData(final Context context, final CommentItemDelegate commentItemDelegate, final RecyclerView.Adapter adapter, int position) {
        final Comment comment = commentItemDelegate.getSource();
        final Status status = commentItemDelegate.getStatus();
        final User user = comment.user;
        final User replyUser = comment.replyUser;
        final UserMark mark = comment.userMark;
        if (user != null) {
            DisplayManager.getInstance(context).displayProfile(user.avatar, profileIv);
            String nameStr = user.name;
            if (replyUser != null) {
                nameStr = context.getString(R.string.comment_item_reply_to_whom, user.name, replyUser.name);
            }
            nameTv.setText(nameStr);
            profileIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDispatcher.userActivity(context, user.userId);
                }
            });
        }
        contentTv.setText(comment.content);
        supportIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User me = AccountManager.getInstance(context).getMe();
                if (me == null) {
                    //TODO
                    return;
                }
                if (mark != null) {
                    if (mark.isSupport) {
                        Toast.makeText(context, R.string.status_detail_has_supported, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                UserMark userMark = null;
                if (mark == null) {
                    userMark = new UserMark();
                } else {
                    userMark = mark;
                }
                userMark.isSupport = true;
                comment.supportCount++;
                comment.userMark = userMark;
                StatusManager.getInstance(context).supportComment(user.userId, comment.id, me.getCookie());
                adapter.notifyDataSetChanged();
            }
        });
        if (mark != null) {
            supportIv.setSelected(mark.isSupport);
            supportCountTv.setSelected(mark.isSupport);
        }
        if (comment.supportCount > 0) {
            supportCountTv.setText(comment.supportCount + "");
        }
        //TODO
        if (adapter.getItemCount() == 3 && position == 2) {
            itemView.setBackgroundResource(R.drawable.std_list_item_bg_nor);
        } else if (position == 2) {
            itemView.setBackgroundResource(R.drawable.header_round_conner_bg_nor);
        } else if (position == adapter.getItemCount() - 1) {
            itemView.setBackgroundResource(R.drawable.footer_round_conner_bg_nor);
        } else {
            itemView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentItemDelegate.OnCommentActionListener listener = commentItemDelegate.getOnCommentActionListener();
                User me = AccountManager.getInstance(context).getMe();
                if (me == null) {
                    //TODO
                    return;
                }
                Log.v(TAG, TAG + " click happened");
                if (listener != null && (me.userRole == User.USER_ROLE_STUDENT || me.userRole == User.USER_ROLE_PARENTS)) {
                    listener.onClick(v, comment, status);
                }
            }
        });
    }
}

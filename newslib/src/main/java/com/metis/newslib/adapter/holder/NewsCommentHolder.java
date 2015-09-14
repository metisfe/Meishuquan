package com.metis.newslib.adapter.holder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.base.ActivityDispatcher;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.DisplayManager;
import com.metis.base.manager.SupportManager;
import com.metis.base.module.User;
import com.metis.base.module.UserMark;
import com.metis.base.utils.TimeUtils;
import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.newslib.R;
import com.metis.newslib.adapter.delegate.NewsCommentDelegate;
import com.metis.newslib.module.NewsCommentItem;

/**
 * Created by Beak on 2015/9/4.
 */
public class NewsCommentHolder extends AbsViewHolder<NewsCommentDelegate> {

    public ImageView profileIv;
    public TextView nameTv, supportCountTv, commentCountTv, timeTv, contentTv;
    public View supportBtn = null, commentBtn;

    public NewsCommentHolder(View itemView) {
        super(itemView);
        profileIv = (ImageView)itemView.findViewById(R.id.comment_item_profile);
        nameTv = (TextView)itemView.findViewById(R.id.comment_item_name);
        supportCountTv = (TextView)itemView.findViewById(R.id.comment_item_support_count);
        commentCountTv = (TextView)itemView.findViewById(R.id.comment_item_comment_count);
        timeTv = (TextView)itemView.findViewById(R.id.comment_item_time);
        contentTv = (TextView)itemView.findViewById(R.id.comment_item_content);
        supportBtn = itemView.findViewById(R.id.comment_item_thumb_up);
        commentBtn = itemView.findViewById(R.id.comment_item_comment);
    }

    @Override
    public void bindData(final Context context, final NewsCommentDelegate newsCommentDelegate, final RecyclerView.Adapter adapter, int position) {
        final NewsCommentItem item = newsCommentDelegate.getSource();
        final User user = item.user;
        if (user != null) {
            DisplayManager.getInstance(context).displayProfile(user.getAvailableAvatar(), profileIv);
            if (item.replyUser != null/* && item.replyUser.userId != user.userId*/) {
                nameTv.setText(context.getString(R.string.text_reply_one_to_one, user.name, item.replyUser.name));
            } else {
                nameTv.setText(user.name);
            }

            profileIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDispatcher.userActivity(context, user.userId);
                }
            });
        }
        timeTv.setText(TimeUtils.formatStdTime(context, item.commentDateTime));
        if (TextUtils.isEmpty(item.content)) {
            contentTv.setText("");
        } else {
            contentTv.setText(item.content.trim());
        }

        if (adapter instanceof DelegateAdapter) {
            AbsDelegate previousDelegate = null;
            if (position > 0) {
                previousDelegate = ((DelegateAdapter) adapter).getDataItem(position - 1);
            }
            AbsDelegate nextDelegate = null;
            if (position < adapter.getItemCount() - 1) {
                nextDelegate = ((DelegateAdapter) adapter).getDataItem(position + 1);
            }
            if (previousDelegate != null && previousDelegate instanceof NewsCommentDelegate && nextDelegate != null && nextDelegate instanceof NewsCommentDelegate) {
                itemView.setBackgroundResource(R.drawable.std_list_item_bg);
            } else if (previousDelegate != null && previousDelegate instanceof NewsCommentDelegate) {
                itemView.setBackgroundResource(R.drawable.footer_round_conner_bg_sel);
            } else if (nextDelegate != null && nextDelegate instanceof NewsCommentDelegate) {
                itemView.setBackgroundResource(R.drawable.header_round_conner_bg_sel);
            } else {
                itemView.setBackgroundResource(R.drawable.std_list_item_round_bg);
            }
        }
        if (item.supportCount > 0) {
            supportCountTv.setText(context.getString(R.string.text_count_rex, item.supportCount));
        } else {
            supportCountTv.setText("");
        }
        if (item.replyCount > 0) {
            commentCountTv.setText(context.getString(R.string.text_count_rex, item.replyCount));
        } else {
            commentCountTv.setText("");
        }
        supportBtn.setSelected(item.userMark != null && item.userMark.isSupport);
        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User me = AccountManager.getInstance(context).getMe();
                if (me == null) {
                    ActivityDispatcher.loginActivityWhenAlreadyIn(context);
                    return;
                }
                if (item.userMark == null || !item.userMark.isSupport) {
                    SupportManager.getInstance(context).supportNewsComment(me.userId, item.id, me.getCookie());
                    if (item.userMark == null) {
                        item.userMark = new UserMark();
                        item.userMark.isSupport = true;
                    } else {
                        item.userMark.isSupport = true;
                    }
                    item.supportCount++;
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, R.string.toast_supported, Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User me = AccountManager.getInstance(context).getMe();
                if (me == null) {
                    ActivityDispatcher.loginActivityWhenAlreadyIn(context);
                    return;
                }
                com.metis.newslib.ActivityDispatcher.replyActivity((Activity)context, newsCommentDelegate.getDetails(), item);
            }
        });
        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }
}

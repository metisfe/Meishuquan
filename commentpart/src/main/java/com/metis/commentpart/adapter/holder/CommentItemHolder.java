package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.ActivityDispatcher;
import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.CommentItemDelegate;
import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.Status;

/**
 * Created by Beak on 2015/8/6.
 */
public class CommentItemHolder extends AbsViewHolder<CommentItemDelegate> {

    public ImageView profileIv;
    public TextView nameTv, contentTv, timeTv;

    public CommentItemHolder(View itemView) {
        super(itemView);

        profileIv = (ImageView)itemView.findViewById(R.id.comment_list_item_profile);
        nameTv = (TextView)itemView.findViewById(R.id.comment_list_item_name);
        contentTv = (TextView)itemView.findViewById(R.id.comment_list_item_content);
        timeTv = (TextView)itemView.findViewById(R.id.comment_list_item_time);
    }

    @Override
    public void bindData(final Context context, CommentItemDelegate commentItemDelegate, RecyclerView.Adapter adapter, int position) {
        Comment comment = commentItemDelegate.getSource();
        Status status = commentItemDelegate.getStatus();
        final User user = comment.user;
        if (user != null) {
            DisplayManager.getInstance(context).display(user.avatar, profileIv);
            nameTv.setText(user.name);
            profileIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDispatcher.userActivity(context, user.userId);
                }
            });
        }
        contentTv.setText(comment.content);
        //TODO
        if (adapter.getItemCount() == 3 && position == 2) {
            itemView.setBackgroundResource(R.drawable.std_list_item_bg_nor);
        } else if (position == 2) {
            itemView.setBackgroundResource(R.drawable.header_round_conner_bg_nor);
        } else if (position == adapter.getItemCount() - 1) {
            itemView.setBackgroundResource(R.drawable.footer_round_conner_bg_nor);
        }
    }
}

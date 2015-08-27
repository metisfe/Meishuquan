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
import com.metis.base.utils.TimeUtils;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.CardHeaderDelegate;
import com.metis.commentpart.manager.StatusManager;
import com.metis.commentpart.module.Comment;

/**
 * Created by Beak on 2015/8/5.
 */
public class CardHeaderHolder extends AbsViewHolder<CardHeaderDelegate> {

    private static final String TAG = CardHeaderHolder.class.getSimpleName();

    public ImageView profileIv;
    public TextView nameTv, locationTv, supportCountTv, timeTv;
    public ImageView thumbUpIv;

    public CardHeaderHolder(View itemView) {
        super(itemView);
        profileIv = (ImageView)itemView.findViewById(R.id.header_profile);
        nameTv = (TextView)itemView.findViewById(R.id.header_name);
        timeTv = (TextView)itemView.findViewById(R.id.header_time);
        locationTv = (TextView)itemView.findViewById(R.id.header_location);
        thumbUpIv = (ImageView)itemView.findViewById(R.id.header_support);
        supportCountTv = (TextView)itemView.findViewById(R.id.header_support_count);
    }

    @Override
    public void bindData(final Context context, CardHeaderDelegate cardHeaderDelegate, final RecyclerView.Adapter adapter, int position) {
        final Comment comment = cardHeaderDelegate.getSource();
        final User user = comment.user;
        if (user != null) {
            DisplayManager.getInstance(context).displayProfile(user.getAvailableAvatar(), profileIv);
            nameTv.setText(user.name);
            locationTv.setText(user.location);
            profileIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDispatcher.userActivity(context, user.userId);
                }
            });
        }
        timeTv.setText(TimeUtils.formatStdTime(context, comment.commentDateTime));
        final UserMark mark = comment.userMark;
        thumbUpIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User me = AccountManager.getInstance(context).getMe();
                if (me == null) {
                    com.metis.base.ActivityDispatcher.loginActivity(context);
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
            thumbUpIv.setSelected(mark.isSupport);
            supportCountTv.setSelected(mark.isSupport);
        }
        if (comment.supportCount > 0) {
            supportCountTv.setText(comment.supportCount + "");
        }
    }
}

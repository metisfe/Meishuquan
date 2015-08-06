package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.ActivityDispatcher;
import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.CardHeaderDelegate;
import com.metis.commentpart.module.Comment;

/**
 * Created by Beak on 2015/8/5.
 */
public class CardHeaderHolder extends AbsViewHolder<CardHeaderDelegate> {

    private static final String TAG = CardHeaderHolder.class.getSimpleName();

    public ImageView profileIv;
    public TextView nameTv, locationTv;
    public ImageView thumbUpIv;

    public CardHeaderHolder(View itemView) {
        super(itemView);
        profileIv = (ImageView)itemView.findViewById(R.id.header_profile);
        nameTv = (TextView)itemView.findViewById(R.id.header_name);
        locationTv = (TextView)itemView.findViewById(R.id.header_location);
        thumbUpIv = (ImageView)itemView.findViewById(R.id.header_support);
    }

    @Override
    public void bindData(final Context context, CardHeaderDelegate cardHeaderDelegate, RecyclerView.Adapter adapter, int position) {
        Comment comment = cardHeaderDelegate.getSource();
        final User user = comment.user;
        if (user != null) {
            DisplayManager.getInstance(context).display(user.avatar, profileIv);
            nameTv.setText(user.name);
            locationTv.setText(user.location);
            profileIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDispatcher.userActivity(context, user.userId);
                }
            });
        }
        thumbUpIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }
}

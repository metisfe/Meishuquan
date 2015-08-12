package com.metis.commentpart.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.utils.TimeUtils;
import com.metis.commentpart.R;
import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.CommentAttachment;

import java.util.List;

public class FlipperAdapter extends BaseAdapter {

    private List<Comment> mCommentList = null;
    private Context mContext = null;

    public FlipperAdapter (Context context, List<Comment> commentList) {
        mContext = context;
        mCommentList = commentList;
    }

    @Override
    public int getCount() {
        return mCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FlipperHolder holder = null;
        if (convertView == null) {
            holder = new FlipperHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_flipper_item, null);
            holder.profileIv = (ImageView)convertView.findViewById(R.id.flipper_profile);
            holder.flipperTv = (TextView)convertView.findViewById(R.id.flipper_content);
            holder.flipperVoiceIv = (ImageView)convertView.findViewById(R.id.flipper_voice);
            holder.flipperVoicePanel = (ViewGroup)convertView.findViewById(R.id.voice_panel);
            holder.flipperDurationTv = (TextView)convertView.findViewById(R.id.voice_duration);
            holder.flipperVoiceContainer = (ViewGroup)convertView.findViewById(R.id.voice_container);
            convertView.setTag(holder);
        } else {
            holder = (FlipperHolder)convertView.getTag();
        }
        final Comment comment = mCommentList.get(position);
        if (comment.commentType == Comment.COMMENT_TYPE_TEXT) {
            holder.flipperTv.setText(comment.content);
            holder.flipperTv.setVisibility(View.VISIBLE);
            holder.flipperVoicePanel.setVisibility(View.GONE);
        } else if (comment.commentType == Comment.COMMENT_TYPE_VOICE) {
            holder.flipperTv.setVisibility(View.GONE);
            holder.flipperVoicePanel.setVisibility(View.VISIBLE);
            CommentAttachment attachment = comment.imgOrVoiceUrl;
            final Resources resources = mContext.getResources();
            final int max = resources.getDimensionPixelSize(R.dimen.voice_max_width);
            final int min = resources.getDimensionPixelSize(R.dimen.voice_min_width);
            int width = min;
            if (attachment != null) {
                holder.flipperDurationTv.setText(TimeUtils.format(attachment.voiceLength));
                width = min + attachment.voiceLength / 100;
                width = Math.min(width, max);
            }
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.flipperVoiceContainer.getLayoutParams();
            if (params == null) {
                params = new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                params.width = width;
            }
            holder.flipperVoiceContainer.setLayoutParams(params);

        }
        User user = comment.user;
        if (user != null) {
            DisplayManager.getInstance(mContext).displayProfile(user.avatar, holder.profileIv);
        }
        return convertView;
    }

    private class FlipperHolder {
        public ImageView profileIv = null;
        public TextView flipperTv = null;
        public ImageView flipperVoiceIv = null;
        public ViewGroup flipperVoicePanel = null;
        public ViewGroup flipperVoiceContainer = null;
        public TextView flipperDurationTv = null;
    }
}
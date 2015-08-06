package com.metis.commentpart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.commentpart.R;
import com.metis.commentpart.module.Comment;

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
            convertView.setTag(holder);
        } else {
            holder = (FlipperHolder)convertView.getTag();
        }
        final Comment comment = mCommentList.get(position);
        holder.flipperTv.setText(comment.content);
        User user = comment.user;
        if (user != null) {
            DisplayManager.getInstance(mContext).display(user.avatar, holder.profileIv);
        }
        return convertView;
    }

    private class FlipperHolder {
        public ImageView profileIv = null;
        public TextView flipperTv = null;
    }
}
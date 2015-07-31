package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.commentpart.R;

/**
 * Created by Beak on 2015/7/31.
 */
public class TeacherSqHolder extends RecyclerView.ViewHolder {

    public ImageView profileIv = null, closeIv = null;
    public TextView nameTv = null;

    public TeacherSqHolder(View itemView) {
        super(itemView);
        profileIv = (ImageView)itemView.findViewById(R.id.item_profile);
        closeIv = (ImageView)itemView.findViewById(R.id.item_close);
        nameTv = (TextView)itemView.findViewById(R.id.item_name);
    }
}

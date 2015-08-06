package com.metis.commentpart;

import android.content.Context;
import android.content.Intent;

import com.metis.commentpart.activity.FilterActivity;
import com.metis.commentpart.activity.PublishStatusActivity;
import com.metis.commentpart.activity.ReplyActivity;
import com.metis.commentpart.activity.StatusDetailActivity;
import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.Status;

/**
 * Created by Beak on 2015/7/24.
 */
public class ActivityDispatcher {

    public static final String
            KEY_STATUS = "key_status",
            KEY_COMMENT = "key_comment",
            KEY_MODE = "key_mode",
            KEY_REPLY_TYPE = "key_reply_type";

    public static void statusDetail (Context context, Status status) {
        Intent it = new Intent (context, StatusDetailActivity.class);
        it.putExtra(KEY_STATUS, status);
        context.startActivity(it);
    }

    public static void filterActivity (Context context) {
        Intent it = new Intent(context, FilterActivity.class);
        context.startActivity(it);
    }

    public static void publishStatusActivity (Context context) {
        Intent it = new Intent (context, PublishStatusActivity.class);
        context.startActivity(it);
    }

    public static void replyActivity (Context context, Status status, int replyType) {
        Intent it = new Intent(context, ReplyActivity.class);
        it.putExtra(KEY_STATUS, status);
        it.putExtra(KEY_MODE, ReplyActivity.MODE_STATUS);
        it.putExtra(KEY_REPLY_TYPE, replyType);
        context.startActivity(it);
    }

    public static void replyActivity (Context context, Status status, Comment comment, int replyType) {
        Intent it = new Intent(context, ReplyActivity.class);
        it.putExtra(KEY_STATUS, status);
        it.putExtra(KEY_COMMENT, comment);
        it.putExtra(KEY_MODE, ReplyActivity.MODE_COMMENT);
        it.putExtra(KEY_REPLY_TYPE, replyType);
        context.startActivity(it);
    }

}

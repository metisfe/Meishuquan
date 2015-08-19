package com.metis.commentpart;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.metis.base.module.User;
import com.metis.base.widget.ImagePreviewable;
import com.metis.commentpart.activity.FilterActivity;
import com.metis.commentpart.activity.PublishStatusActivity;
import com.metis.commentpart.activity.StatusDetailActivity;
import com.metis.commentpart.activity.StatusImageActivity;
import com.metis.commentpart.module.Status;

import java.io.Serializable;

/**
 * Created by Beak on 2015/7/24.
 */
public class ActivityDispatcher {

    public static final String
            KEY_STATUS = "key_status",
            KEY_COMMENT = "key_comment",
            KEY_MODE = "key_mode",
            KEY_REPLY_TYPE = "key_reply_type",
            KEY_USER = "user";

    public static void statusDetail (Context context, Status status) {
        Intent it = new Intent (context, StatusDetailActivity.class);
        it.putExtra(KEY_STATUS, (Serializable) status);
        context.startActivity(it);
    }

    public static void statusDetailWithComment (Context context, Status status) {
        Intent it = new Intent(context, StatusDetailActivity.class);
        it.putExtra(KEY_STATUS, (Serializable) status);
        it.putExtra(KEY_MODE, StatusDetailActivity.MODE_WITH_COMMENT);
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

    public static void imagePreviewActivity (Context context, User user, ImagePreviewable[] imagePreviewables, int index) {
        try {
            Intent it = new Intent(context, StatusImageActivity.class);
            it.putExtra(KEY_USER, (Serializable)user);
            it.putExtra(com.metis.base.ActivityDispatcher.KEY_IMAGES, imagePreviewables);
            it.putExtra(com.metis.base.ActivityDispatcher.KEY_INDEX, index);
            it.addCategory(Intent.CATEGORY_DEFAULT);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "imagePreviewActivity exception", Toast.LENGTH_SHORT).show();
        }
    }

    public static void imagePreviewActivity (Context context, User user, ImagePreviewable[] imagePreviewables) {
        imagePreviewActivity(context, user, imagePreviewables, 0);
    }

    public static void imagePreviewActivity (Context context, User user, ImagePreviewable imagePreviewables) {
        ImagePreviewable[] array = new ImagePreviewable[1];
        array[0] = imagePreviewables;
        imagePreviewActivity(context, user, array);
    }


    /*public static void replyActivity (Context context, Status status, int replyType) {
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
    }*/

}

package com.metis.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.metis.base.activity.RegisterActivity;
import com.metis.base.activity.RoleChooseActivity;
import com.metis.base.module.User;
import com.metis.base.widget.ImagePreviewable;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Beak on 2015/7/16.
 */
public class ActivityDispatcher {
    public static final String
    ACTION_USER = "com.metis.meishuquan.action.user",
    ACTION_IMAGE_PREVIEW = "com.metis.meishuquan.action.image_preview",
    ACTION_LOGIN = "com.metis.meishuquan.action.login",
    ACTION_MAIN = "com.metis.meishuquan.action.main",
    ACTION_SHARE = "com.metis.meishuquan.action.share",
    ACTION_CIRCLE_SHARE = "com.metis.meishuquan.action.circle_share";

    public static final String
    KEY_USER_ID = "user_id",
    KEY_IMAGES = "images",
    KEY_INDEX = "index",
    KEY_STATUS = "status",
    KEY_MODE = "key_mode",
    KEY_USER = "key_user",
    KEY_TITLE = "title",
    KEY_TEXT = "text",
    KEY_IMAGE_URL = "image_url",
    KEY_URL = "url",
    KEY_ID = "id",
    KEY_REPLY_TYPE = "REPLY_TYPE",
    KEY_REPLY_ID = "REPLY_ID",
    TITLE = "TITLE",
    CONTENT = "CONTENT",
    IMAGEURL = "IMAGEURL",
    INPUT_CONTENT = "INPUT_CONTENT";

    public static final int REQUEST_CODE_LOGIN = 100;

    public static void userActivity (Context context, long userId) {
        try {
            Intent it = new Intent(ACTION_USER);
            it.putExtra(KEY_USER_ID, userId);
            it.addCategory(Intent.CATEGORY_DEFAULT);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "userActivity exception", Toast.LENGTH_SHORT).show();
        }
    }

    public static void imagePreviewActivity (Context context, ImagePreviewable[] imagePreviewables, int index) {
        try {
            Intent it = new Intent(ACTION_IMAGE_PREVIEW);
            it.putExtra(KEY_IMAGES, imagePreviewables);
            it.putExtra(KEY_INDEX, index);
            it.addCategory(Intent.CATEGORY_DEFAULT);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "imagePreviewActivity exception", Toast.LENGTH_SHORT).show();
        }
    }

    public static void imagePreviewActivity (Context context, ImagePreviewable[] imagePreviewables) {
        imagePreviewActivity(context, imagePreviewables, 0);
    }

    public static void imagePreviewActivity (Context context, ImagePreviewable imagePreviewables) {
        ImagePreviewable[] array = new ImagePreviewable[1];
        array[0] = imagePreviewables;
        imagePreviewActivity(context, array);
    }

    public static void getImage (Activity context, int requestCode) {
        Intent it = new Intent (Intent.ACTION_GET_CONTENT);
        it.setType("image/*");
        context.startActivityForResult(it, requestCode);
    }

    public static void captureImage (Activity activity, int requestCode, String path) {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
        activity.startActivityForResult(it, requestCode);
    }

    public static void loginActivity (Context context) {
        try {
            Intent it = new Intent ();
            it.putExtra(KEY_STATUS, false);
            it.setAction(ACTION_LOGIN);
            it.addCategory(Intent.CATEGORY_DEFAULT);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "loginActivityWhenAlreadyIn exception", Toast.LENGTH_SHORT).show();
        }
    }

    public static void loginActivityWhenAlreadyIn (Context context) {
        try {
            Intent it = new Intent ();
            it.putExtra(KEY_STATUS, true);
            it.setAction(ACTION_LOGIN);
            it.addCategory(Intent.CATEGORY_DEFAULT);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (context instanceof Activity) {
                ((Activity)context).startActivityForResult(it, REQUEST_CODE_LOGIN);
            } else {
                context.startActivity(it);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "loginActivityWhenAlreadyIn exception", Toast.LENGTH_SHORT).show();
        }
    }

    public static void resetPwdActivity (Context context) {
        Intent it = new Intent (context, RegisterActivity.class);
        it.putExtra(KEY_MODE, RegisterActivity.MODE_RESET_PWD);
        context.startActivity(it);
    }

    public static void mainActivity (Context context) {
        Intent it = new Intent(ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_DEFAULT);
        context.startActivity(it);
    }

    public static void userRoleActivity (Context context, User user, boolean isAlreadyIn) {
        Intent it = new Intent(context, RoleChooseActivity.class);
        it.putExtra(KEY_STATUS, isAlreadyIn);
        it.putExtra(KEY_USER, (Serializable) user);
        context.startActivity(it);
    }

    public static void shareActivity (Context context, long id, String title, String text, String imageUrl, String url) {
        try {
            Intent it = new Intent(ACTION_SHARE);
            it.addCategory(Intent.CATEGORY_DEFAULT);
            it.putExtra(KEY_ID, id);
            it.putExtra(KEY_TITLE, title);
            it.putExtra(KEY_TEXT, text);
            it.putExtra(KEY_IMAGE_URL, imageUrl);
            it.putExtra(KEY_URL, url);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /*KEY_REPLY_TYPE = "REPLY_TYPE",
    KEY_REPLY_ID = "REPLY_ID",
    TITLE = "TITLE",
    CONTENT = "CONTENT",
    IMAGEURL = "IMAGEURL",
    INPUT_CONTENT = "INPUT_CONTENT";*/

    public static void circleShareActivity (Context context, long id, String title, String text, String imageUrl, String url) {
        try {
            Intent it = new Intent(ACTION_CIRCLE_SHARE);
            it.addCategory(Intent.CATEGORY_DEFAULT);
            it.putExtra(KEY_REPLY_ID, id);
            it.putExtra(KEY_REPLY_TYPE, 2);
            it.putExtra(TITLE, title);
            it.putExtra(CONTENT, text);
            it.putExtra(IMAGEURL, imageUrl);
            /*it.putExtra(KEY_ID, id);
            it.putExtra(KEY_TITLE, title);
            it.putExtra(KEY_TEXT, text);
            it.putExtra(KEY_IMAGE_URL, imageUrl);
            it.putExtra(KEY_URL, url);*/
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

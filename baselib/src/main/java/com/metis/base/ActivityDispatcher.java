package com.metis.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.metis.base.widget.ImagePreviewable;

/**
 * Created by Beak on 2015/7/16.
 */
public class ActivityDispatcher {
    public static final String
    ACTION_USER = "com.metis.meishuquan.action.user",
    ACTION_IMAGE_PREVIEW = "com.metis.meishuquan.action.image_preview";

    public static final String
    KEY_USER_ID = "user_id",
    KEY_IMAGES = "images",
    KEY_INDEX = "index";

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
}

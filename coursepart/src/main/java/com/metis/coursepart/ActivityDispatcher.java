package com.metis.coursepart;

import android.content.Context;
import android.content.Intent;

import com.metis.coursepart.activity.CourseVideoDetailActivity;

/**
 * Created by Beak on 2015/7/10.
 */
public class ActivityDispatcher {

    public static final String
    KEY_ALBUM_ID = "album_id";

    public static void videoDetailActivity (Context context, long albumId) {
        Intent it = new Intent(context, CourseVideoDetailActivity.class);
        it.putExtra(KEY_ALBUM_ID, albumId);
        context.startActivity(it);
    }
}

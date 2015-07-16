package com.metis.coursepart;

import android.content.Context;
import android.content.Intent;

import com.metis.base.utils.Log;
import com.metis.coursepart.activity.CourseVideoDetailActivity;
import com.metis.coursepart.activity.GalleryItemDetailActivity;
import com.metis.coursepart.module.CourseAlbum;
import com.metis.coursepart.module.GalleryItem;

import java.util.List;

/**
 * Created by Beak on 2015/7/10.
 */
public class ActivityDispatcher {

    private static final String TAG = ActivityDispatcher.class.getSimpleName();

    public static final String
            KEY_COURSE_ALBUM = "course_album",
            KEY_ALBUM_ID = "album_id",
            KEY_GALLERY_ITEM_ID = "gallery_item_id";

    public static void videoDetailActivity (Context context, CourseAlbum album) {
        Intent it = new Intent(context, CourseVideoDetailActivity.class);
        it.putExtra(KEY_COURSE_ALBUM, album);
        context.startActivity(it);
    }

    public static void imageDetailActivity (Context context, long id) {
        Intent it = new Intent(context, GalleryItemDetailActivity.class);
        Log.v(TAG, "imageDetailActivity id=" + id);
        it.putExtra(KEY_GALLERY_ITEM_ID, id);
        context.startActivity(it);
    }
}

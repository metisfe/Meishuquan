package com.metis.coursepart;

import android.content.Context;
import android.content.Intent;

import com.metis.base.utils.Log;
import com.metis.base.widget.ImagePreviewable;
import com.metis.coursepart.activity.CourseDetailActivity;
import com.metis.coursepart.activity.FilterActivity;
import com.metis.coursepart.activity.GalleryItemDetailActivity;
import com.metis.coursepart.module.CourseAlbum;

/**
 * Created by Beak on 2015/7/10.
 */
public class ActivityDispatcher {

    private static final String TAG = ActivityDispatcher.class.getSimpleName();

    public static final String
            ACTION_VIDEO_FILTER = "com.metis.coursepart.action_video_filter",
            ACTION_GALLERY_FILTER = "com.metis.coursepart.action_gallery_filter",
            ACTION_GALLERY_DETAIL = "com.metis.coursepart.action_gallery_detail";

    public static final String
            KEY_COURSE_ALBUM = "course_album",
            KEY_ALBUM_ID = "album_id",
            KEY_TAG = "tag",
            KEY_GALLERY_ITEM_ID = "gallery_item_id",
            KEY_STATE_FILTER_ID = "state_filter_id";

    public static void videoDetailActivity (Context context, CourseAlbum album) {
        Intent it = new Intent(context, CourseDetailActivity.class);
        //Intent it = new Intent(context, CourseVideoDetailActivity.class);
        it.putExtra(KEY_COURSE_ALBUM, album);
        context.startActivity(it);
    }

    public static void imageDetailActivity (Context context, ImagePreviewable[] images, int index) {
        Intent it = new Intent(context, GalleryItemDetailActivity.class);
        it.putExtra(com.metis.base.ActivityDispatcher.KEY_IMAGES, images);
        it.putExtra(com.metis.base.ActivityDispatcher.KEY_INDEX, index);
        context.startActivity(it);

    }

    public static void imageDetailActivity (Context context, ImagePreviewable[] images) {
        imageDetailActivity(context, images, 0);
    }

    public static void imageDetailActivity (Context context, ImagePreviewable image) {
        ImagePreviewable[] array = new ImagePreviewable[1];
        array[0] = image;
        imageDetailActivity(context, array);
    }

    public static void filterActivityForVideo (Context context) {
        filterActivityForVideoWithState(context, 1);
    }

    public static void filterActivityForVideoWithState(Context context, long state) {
        Intent it = new Intent(context, FilterActivity.class);
        it.setAction(ACTION_VIDEO_FILTER);
        it.putExtra(KEY_STATE_FILTER_ID, state);
        context.startActivity(it);
        Log.v(TAG, context.getClass().getSimpleName() + " startActivity");
        //((Activity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void filterActivityForGallery (Context context) {
        filterActivityForGalleryWithState(context, 1);
    }

    public static void filterActivityForGalleryWithState(Context context, long state) {
        Intent it = new Intent(context, FilterActivity.class);
        it.setAction(ACTION_GALLERY_FILTER);
        it.putExtra(KEY_STATE_FILTER_ID, state);
        context.startActivity(it);
    }
}

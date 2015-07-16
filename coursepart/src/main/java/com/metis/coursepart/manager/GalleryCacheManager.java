package com.metis.coursepart.manager;

import android.content.Context;

import com.metis.base.manager.AbsManager;
import com.metis.coursepart.module.GalleryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/15.
 */
public class GalleryCacheManager extends AbsManager {

    private static GalleryCacheManager sManager = null;

    public static synchronized GalleryCacheManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new GalleryCacheManager(context.getApplicationContext());
        }
        return sManager;
    }

    private List<GalleryItem> mGalleryItemList = new ArrayList<GalleryItem>();

    private GalleryCacheManager(Context context) {
        super(context);
    }

    public List<GalleryItem> getGalleryItemList () {
        return mGalleryItemList;
    }

    public int getIndexById (long id) {
        final int length = mGalleryItemList.size();
        for (int i = 0; i < length; i++) {
            if (mGalleryItemList.get(i).picId == id) {
                return i;
            }
        }
        return -1;
    }

    public void clearGalleryItemList () {
        mGalleryItemList.clear();
    }

    public void addAll (List<GalleryItem> list) {
        mGalleryItemList.addAll(list);
    }

    public GalleryItem getGalleryItem (int index) {
        return mGalleryItemList.get(index);
    }

    public int size () {
        return mGalleryItemList.size();
    }
}

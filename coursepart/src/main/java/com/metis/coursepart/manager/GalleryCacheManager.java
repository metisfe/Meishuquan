package com.metis.coursepart.manager;

import android.content.Context;

import com.metis.base.manager.AbsManager;
import com.metis.base.utils.Log;
import com.metis.coursepart.module.GalleryItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Beak on 2015/7/15.
 */
public class GalleryCacheManager extends AbsManager {

    private static final String TAG = GalleryCacheManager.class.getSimpleName();

    private static GalleryCacheManager sManager = null;

    public static synchronized GalleryCacheManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new GalleryCacheManager(context.getApplicationContext());
        }
        return sManager;
    }
    private Map<String, List<GalleryItem>> mGalleryMap = new HashMap<String, List<GalleryItem>>();

    private GalleryCacheManager(Context context) {
        super(context);
    }

    public List<GalleryItem> getGalleryItemList (String tag) {
        return mGalleryMap.get(tag);
    }

    public int getIndexById (String tag, long id) {
        List<GalleryItem> list = getGalleryItemList(tag);
        if (list == null) {
            return -1;
        }
        final int length = list.size();
        for (int i = 0; i < length; i++) {
            if (list.get(i).picId == id) {
                return i;
            }
        }
        return -1;
    }

    public void clearGalleryItemList (String tag) {
        List<GalleryItem> list = getGalleryItemList(tag);
        if (list == null) {
            return;
        }
        list.clear();
    }

    public void addAll (String tag, List<GalleryItem> list) {
        List<GalleryItem> galleryItemList = getGalleryItemList(tag);
        if (galleryItemList == null) {
            galleryItemList = new ArrayList<GalleryItem>();
            mGalleryMap.put(tag, list);
        }
        galleryItemList.addAll(list);
    }

    public GalleryItem getGalleryItem (String tag, int index) {
        List<GalleryItem> galleryItemList = getGalleryItemList(tag);
        if (galleryItemList == null) {
            return null;
        }
        return galleryItemList.get(index);
    }

    public int size (String tag) {
        List<GalleryItem> galleryItemList = getGalleryItemList(tag);
        if (galleryItemList == null) {
            return 0;
        }
        return galleryItemList.size();
    }
}

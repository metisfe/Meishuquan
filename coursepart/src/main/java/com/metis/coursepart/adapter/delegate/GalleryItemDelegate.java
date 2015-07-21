package com.metis.coursepart.adapter.delegate;

import android.content.Context;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.coursepart.module.GalleryItem;

/**
 * Created by Beak on 2015/7/8.
 */
public class GalleryItemDelegate extends BaseDelegate<GalleryItem> {

    public int delegateWidth = -1, delegateHeight = -1;

    public String mTag = null;

    public GalleryItemDelegate(GalleryItem galleryItem) {
        this(null, galleryItem);
    }

    public GalleryItemDelegate (Context context, GalleryItem galleryItem) {
        super(galleryItem);
        if (context != null && galleryItem.width > 0 && galleryItem.height > 0) {
            autoComputeByScreen(context);
        }
    }

    public void setTag (String tag) {
        mTag = tag;
    }

    public String getTag () {
        return mTag;
    }

    @Override
    public int getDelegateType() {
        return CourseDelegateType.TYPE_GALLERY_ITEM.getType();
    }

    public void autoComputeByScreen (Context context) {
        autoComputeByScreen(context, getSource().width, getSource().height);
    }

    public void autoComputeByScreen (Context context, int orgWid, int orgHei) {
        final int screenWid = context.getResources().getDisplayMetrics().widthPixels;
        final int itemWid = screenWid / 2;
        int itemHei = (int)(orgHei * (float)itemWid/orgWid);
        setWidthAndHeight(itemWid, itemHei);
    }

    public void setWidthAndHeight (int delegateWid, int delegateHei) {
        delegateWidth = delegateWid;
        delegateHeight = delegateHei;
    }
}

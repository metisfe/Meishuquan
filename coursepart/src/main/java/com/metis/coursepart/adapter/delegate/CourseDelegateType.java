package com.metis.coursepart.adapter.delegate;

import android.support.annotation.LayoutRes;

import com.metis.base.widget.adapter.delegate.TypeLayoutProvider;
import com.metis.coursepart.R;

/**
 * Created by Beak on 2015/7/8.
 */
public enum CourseDelegateType {

    TYPE_GALLERY_ITEM (ID.ID_GALLERY_ITEM, R.layout.layout_gallery_item),
    TYPE_VIDEO_ITEM (ID.ID_ALBUM_CONTAINER, R.layout.layout_album_item_container),
    TYPE_VIDEO_ITEM_SMALL (ID.ID_ALBUM_ITEM_SMALL, R.layout.layout_album_item_small),
    TYPE_VIDEO_ITEM_SMALL_TITLE (ID.ID_ITEM_TITLE, R.layout.layout_item_title);

    static {
        for (CourseDelegateType courseDelegateType : values()) {
            TypeLayoutProvider.put(courseDelegateType.type, courseDelegateType.layoutResId);
        }
    }

    private int type;
    private @LayoutRes int layoutResId;
    CourseDelegateType(int type, @LayoutRes int layoutRes) {
        this.type = type;
        this.layoutResId = layoutRes;
    }

    public int getType() {
        return type;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public static final class ID {
        public static final int
                ID_GALLERY_ITEM = 100,
                ID_ALBUM_CONTAINER = 104,
                ID_ALBUM_ITEM_SMALL = 108,
                ID_ITEM_TITLE = 112;
    }
}

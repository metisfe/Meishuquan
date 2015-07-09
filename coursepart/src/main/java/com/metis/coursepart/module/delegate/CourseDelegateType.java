package com.metis.coursepart.module.delegate;

import android.support.annotation.LayoutRes;

import com.metis.base.widget.delegate.TypeLayoutProvider;
import com.metis.coursepart.R;

/**
 * Created by Beak on 2015/7/8.
 */
public enum CourseDelegateType {

    TYPE_GALLERY_ITEM (ID.ID_GALLERY_ITEM, R.layout.layout_gallery_item);

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
                ID_GALLERY_ITEM = 100;
    }
}

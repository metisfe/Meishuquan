package com.metis.base.widget.adapter;

import android.support.annotation.LayoutRes;

import com.metis.base.R;
import com.metis.base.widget.adapter.delegate.TypeLayoutProvider;

/**
 * Created by Beak on 2015/8/24.
 */
public enum MeDelegateType {

    TYPE_ME_HEADER (ID.ID_ME_HEADER, R.layout.layout_me_profile_card),
    TYPE_DOWNLOADING_ITEM (ID.ID_DOWNLOADING_ITEM, R.layout.layout_downloading_item);

    static {
        for (MeDelegateType courseDelegateType : values()) {
            TypeLayoutProvider.put(courseDelegateType.type, courseDelegateType.layoutResId);
        }
    }

    private int type;
    private @LayoutRes
    int layoutResId;
    MeDelegateType(int type, @LayoutRes int layoutRes) {
        this.type = type;
        this.layoutResId = layoutRes;
    }

    public int getType() {
        return type;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public static class ID {
        public static final int ID_ME_HEADER = 400, ID_DOWNLOADING_ITEM = 404;
    }
}

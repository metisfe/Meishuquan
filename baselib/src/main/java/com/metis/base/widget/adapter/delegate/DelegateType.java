package com.metis.base.widget.adapter.delegate;

import android.support.annotation.LayoutRes;

import com.metis.base.R;

/**
 * Created by Beak on 2015/7/8.
 */
public enum DelegateType  {

    TYPE_NONE (ID.ID_NONE, R.layout.layout_base_type_none),
    TYPE_FOOTER (ID.ID_FOOTER, R.layout.layout_load_more_footer),
    TYPE_DEBUG_STRING (ID.ID_DEBUG_STRING, R.layout.layout_debug_string),
    TYPE_lIST_DIVIDER (ID.ID_LIST_DIVIDER, R.layout.layout_list_divider_item);

    static {
        for (DelegateType type : values()) {
            TypeLayoutProvider.put(type.typeId, type.layoutResId);
        }
    }

    private int typeId;

    @LayoutRes
    private int layoutResId;

    DelegateType (int typeId, @LayoutRes int layoutResId) {
        this.typeId = typeId;
        this.layoutResId = layoutResId;
    }

    public int getType() {
        return typeId;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public static final class ID {
        public static final int
                ID_NONE = 0,
                ID_FOOTER = 1,
                ID_DEBUG_STRING = 3,
                ID_LIST_DIVIDER = 4;
    }
}

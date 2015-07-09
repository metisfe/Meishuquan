package com.metis.base.widget.delegate;

import android.support.annotation.LayoutRes;

import com.metis.base.R;

/**
 * Created by Beak on 2015/7/8.
 */
public enum DelegateType  {

    TYPE_NONE (ID.ID_NONE, R.layout.layout_base_type_none),
    TYPE_FOOTER (ID.ID_FOOTER, R.layout.layout_load_more_footer);

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
                ID_FOOTER = 1;
    }
}

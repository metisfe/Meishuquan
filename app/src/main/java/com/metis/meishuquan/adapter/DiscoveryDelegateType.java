package com.metis.meishuquan.adapter;

import android.support.annotation.LayoutRes;

import com.metis.base.widget.adapter.delegate.TypeLayoutProvider;
import com.metis.meishuquan.R;

/**
 * Created by Beak on 2015/8/24.
 */
public enum DiscoveryDelegateType {

    TYPE_DISCOVERY_COLLEGE (ID.ID_DISCOVERY_COLLEGE, R.layout.layout_exam_college_item),
    TYPE_DISCOVERY_ITEM (ID.ID_DISCOVERY_ITEM, R.layout.layout_icon_text);

    static {
        for (DiscoveryDelegateType courseDelegateType : values()) {
            TypeLayoutProvider.put(courseDelegateType.type, courseDelegateType.layoutResId);
        }
    }

    private int type;
    private @LayoutRes
    int layoutResId;
    DiscoveryDelegateType(int type, @LayoutRes int layoutRes) {
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
        public static final int ID_DISCOVERY_COLLEGE = 500,
        ID_DISCOVERY_ITEM = 504;
    }
}

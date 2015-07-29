package com.metis.commentpart.adapter.delegate;

import android.support.annotation.LayoutRes;

import com.metis.base.widget.adapter.delegate.TypeLayoutProvider;
import com.metis.commentpart.R;

/**
 * Created by Beak on 2015/7/8.
 */
public enum CommentDelegateType {

    TYPE_STATUS_ITEM (ID.ID_STATUS_ITEM, R.layout.layout_status_item);

    static {
        for (CommentDelegateType courseDelegateType : values()) {
            TypeLayoutProvider.put(courseDelegateType.type, courseDelegateType.layoutResId);
        }
    }

    private int type;
    private @LayoutRes int layoutResId;
    CommentDelegateType(int type, @LayoutRes int layoutRes) {
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
                ID_STATUS_ITEM = 200,
                ID_TEACHER_WITH_CHECK_BOX = 204;
    }
}

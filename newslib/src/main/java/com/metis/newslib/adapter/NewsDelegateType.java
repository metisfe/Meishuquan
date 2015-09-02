package com.metis.newslib.adapter;

import com.metis.base.widget.adapter.delegate.TypeLayoutProvider;
import com.metis.newslib.R;

/**
 * Created by Beak on 2015/9/2.
 */
public enum NewsDelegateType {

    TYPE_NEWS_BIG (ID.ID_NEWS_BIG, R.layout.layout_news_big),
    TYPE_NEWS_SMALL (ID.ID_NEWS_SMALL, R.layout.layout_news_small);

    static {
        for (NewsDelegateType type : values()) {
            TypeLayoutProvider.put(type.typeId, type.layoutResId);
        }
    }

    private int typeId;
    private int layoutResId;

    NewsDelegateType (int id, int resId) {
        typeId = id;
        layoutResId = resId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public void setLayoutResId(int layoutResId) {
        this.layoutResId = layoutResId;
    }

    public static class ID {
        public static final int
        ID_NEWS_BIG = 300,
        ID_NEWS_SMALL = 304;
    }
}

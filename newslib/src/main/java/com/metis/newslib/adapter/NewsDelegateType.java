package com.metis.newslib.adapter;

import com.metis.base.widget.adapter.delegate.TypeLayoutProvider;
import com.metis.newslib.R;

/**
 * Created by Beak on 2015/9/2.
 */
public enum NewsDelegateType {

    TYPE_NEWS_BIG (ID.ID_NEWS_BIG, R.layout.layout_news_big),
    TYPE_NEWS_SMALL (ID.ID_NEWS_SMALL, R.layout.layout_news_small),
    TYPE_NEWS_DETAILS_TITLE (ID.ID_NEWS_DETAIL_TITLE, R.layout.layout_news_details_title),
    TYPE_NEWS_DETAILS_IMG (ID.ID_NEWS_DETAIL_IMG, R.layout.layout_news_details_img),
    TYPE_NEWS_DETAILS_TXT (ID.ID_NEWS_DETAIL_TXT, R.layout.layout_news_details_txt),
    TYPE_NEWS_DETAILS_VDO (ID.ID_NEWS_DETAIL_VDO, R.layout.layout_news_details_video),
    TYPE_NEWS_RELATIVE_ITEM (ID.ID_NEWS_RELATIVE_ITEM, R.layout.layout_news_small),
    TYPE_NEWS_COMMENT_ITEM (ID.ID_NEWS_COMMENT_ITEM, R.layout.layout_news_comment_item);

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
        ID_NEWS_SMALL = 304,
        ID_NEWS_DETAIL_TITLE = 308,
        ID_NEWS_DETAIL_IMG = 312,
        ID_NEWS_DETAIL_TXT = 316,
        ID_NEWS_DETAIL_VDO = 318,
        ID_NEWS_RELATIVE_ITEM = 320,
        ID_NEWS_COMMENT_ITEM = 324;
    }
}

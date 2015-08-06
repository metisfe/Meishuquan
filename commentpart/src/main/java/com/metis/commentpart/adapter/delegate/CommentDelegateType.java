package com.metis.commentpart.adapter.delegate;

import android.support.annotation.LayoutRes;

import com.metis.base.widget.adapter.delegate.TypeLayoutProvider;
import com.metis.commentpart.R;

/**
 * Created by Beak on 2015/7/8.
 */
public enum CommentDelegateType {

    TYPE_STATUS_ITEM (ID.ID_STATUS_ITEM, R.layout.layout_status_item),
    TYPE_TEACHER_WITH_CHECK_BOX (ID.ID_TEACHER_WITH_CHECK_BOX, R.layout.layout_teacher_item_with_check),
    TYPE_TEACHER_WITH_BTN (ID.ID_TEACHER_WITH_BTN, R.layout.layout_teacher_item_with_btn),
    TYPE_TEACHER_CONTAINER (ID.ID_TEACHER_CONTAINER, R.layout.layout_teacher_container),
    TYPE_STATUS_DETAIL_TAB (ID.ID_STATUS_DETAIL_TAB, R.layout.layout_status_detail_tab),
    TYPE_COMMENT_CARD_HEADER (ID.ID_COMMENT_CARD_HEADER, R.layout.layout_comment_card_header),
    TYPE_COMMENT_CARD_FOOTER (ID.ID_COMMENT_CARD_FOOTER, R.layout.layout_comment_card_footer),
    TYPE_COMMENT_CARD_TEXT_T (ID.ID_COMMENT_CARD_TEXT_T, R.layout.layout_comment_card_text_t),
    TYPE_COMMENT_CARD_TEXT_S (ID.ID_COMMENT_CARD_TEXT_S, R.layout.layout_comment_card_text_s),
    TYPE_COMMENT_LIST_ITEM (ID.ID_COMMENT_LIST_ITEM, R.layout.layout_comment_list_item);

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
                ID_TEACHER_WITH_CHECK_BOX = 204,
                ID_TEACHER_WITH_BTN = 208,
                ID_TEACHER_CONTAINER = 212,
                ID_STATUS_DETAIL_TAB = 216,
                ID_COMMENT_CARD_HEADER = 220,
                ID_COMMENT_CARD_FOOTER = 224,
                ID_COMMENT_CARD_TEXT_T = 228,
                ID_COMMENT_CARD_TEXT_S = 232,
                ID_COMMENT_LIST_ITEM = 236;
    }
}

package com.metis.meishuquan.widget.delegate;

import android.support.annotation.LayoutRes;

public enum DelegateType {
    NONE (0, 0);
    /*SYSTEM_LIST_FOOTER (100, R.layout.layout_list_footer),
    STATUS_TEXT (200, R.layout.layout_status_text),
    STATUS_SINGLE_IMAGE (204, R.layout.layout_status_single_image),
    STATUS_MULTI_IMAGE(208, R.layout.layout_status_multi_image),
    STATUS_MULTI_IMAGE_GALLERY(210, R.layout.layout_status_gallery),
    STATUS_TEXT_REPOST (212, R.layout.layout_status_text_repost),
    STATUS_SINGLE_IMAGE_REPOST (216, R.layout.layout_status_single_image_repost),
    STATUS_MULTI_IMAGE_REPOST (220, R.layout.layout_status_multi_image_repost),
    COMMENT_ (300, R.layout.layout_comment);*/

    private int id;
    private @LayoutRes
    int layoutRes;

    DelegateType(int id, @LayoutRes int layoutRes) {
        this.id = id;
        this.layoutRes = layoutRes;
    }

    public int getId () {
        return id;
    }

    public int getLayoutResource () {
        return layoutRes;
    }

    public static DelegateType getDelegateTypeById (int id) {
        for (DelegateType t : DelegateType.values()) {
            if (t.getId() == id) {
                return t;
            }
        }
        throw new UnknownDelegateTypeException ();
    }

    public static class UnknownDelegateTypeException extends RuntimeException {

    }
}
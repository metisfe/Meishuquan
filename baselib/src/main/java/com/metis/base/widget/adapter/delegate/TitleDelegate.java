package com.metis.base.widget.adapter.delegate;

import com.metis.base.module.Title;

/**
 * Created by Beak on 2015/8/4.
 */
public class TitleDelegate extends BaseDelegate<Title> {

    public TitleDelegate(Title title) {
        super(title);
    }

    @Override
    public int getDelegateType() {
        return DelegateType.TYPE_ITEM_SMALL_TITLE.getType();
    }
}

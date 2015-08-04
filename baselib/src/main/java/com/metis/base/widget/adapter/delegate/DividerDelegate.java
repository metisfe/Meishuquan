package com.metis.base.widget.adapter.delegate;

/**
 * Created by Beak on 2015/7/31.
 */
public class DividerDelegate extends BaseDelegate<String> {

    public DividerDelegate(String s) {
        super(s);
    }

    @Override
    public int getDelegateType() {
        return DelegateType.TYPE_LIST_DIVIDER.getType();
    }
}

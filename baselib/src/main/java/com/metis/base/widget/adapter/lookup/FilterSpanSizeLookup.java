package com.metis.base.widget.adapter.lookup;

import android.support.v7.widget.GridLayoutManager;

/**
 * Created by gaoyunfei on 15/7/12.
 */
public class FilterSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    @Override
    public int getSpanSize(int position) {
        if (position == 0) {
            return 2;
        }
        return 1;
    }

}

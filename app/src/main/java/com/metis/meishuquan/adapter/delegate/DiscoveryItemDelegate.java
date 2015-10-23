package com.metis.meishuquan.adapter.delegate;

import android.view.View;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.meishuquan.adapter.DiscoveryDelegateType;
import com.metis.meishuquan.module.DiscoveryItem;

/**
 * Created by Beak on 2015/10/21.
 */
public class DiscoveryItemDelegate extends BaseDelegate<DiscoveryItem> {

    private View.OnClickListener mOnClickListener = null;

    private boolean isNative = false;

    private int mIconRes = 0;

    public DiscoveryItemDelegate(DiscoveryItem discoveryItem) {
        super(discoveryItem);
    }

    @Override
    public int getDelegateType() {
        return DiscoveryDelegateType.TYPE_DISCOVERY_ITEM.getType();
    }

    public void setOnClickListener (View.OnClickListener listener) {
        mOnClickListener = listener;
    }

    public View.OnClickListener getOnClickListener () {
        return mOnClickListener;
    }

    public boolean isNative() {
        return isNative;
    }

    public void setIsNative(boolean isNative) {
        this.isNative = isNative;
    }

    public int getIconRes() {
        return mIconRes;
    }

    public void setIconRes(int mIconRes) {
        this.mIconRes = mIconRes;
    }
}

package com.metis.meishuquan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.meishuquan.adapter.delegate.DiscoveryItemDelegate;
import com.metis.meishuquan.adapter.holder.DiscoveryItemHolder;

/**
 * Created by Beak on 2015/10/21.
 */
public class DiscoveryAdapter extends DelegateAdapter {

    public DiscoveryAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case DiscoveryDelegateType.ID.ID_DISCOVERY_ITEM:
                return new DiscoveryItemHolder (view);
        }
        return null;
    }

    public void clearExtendItems () {
        for (int i = 0; i < getItemCount(); i++) {
            AbsDelegate delegate = getDataItem(i);
            if (delegate instanceof DiscoveryItemDelegate) {
                if (!((DiscoveryItemDelegate) delegate).isNative()) {
                    removeDataItem(delegate);
                }
            }
        }
    }
}

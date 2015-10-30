package com.metis.base.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.base.widget.adapter.delegate.DownloadTaskDelegate;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.base.widget.adapter.holder.DownloadTaskHolder;

/**
 * Created by Beak on 2015/10/22.
 */
public class DownloadingAdapter extends DelegateAdapter {

    public DownloadingAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case MeDelegateType.ID.ID_DOWNLOADING_ITEM:
                return new DownloadTaskHolder(view);
        }
        return null;
    }

    public int getTaskIndexById (String id) {

        final int length = getItemCount();
        for (int i = 0; i < length; i++) {
            AbsDelegate delegate = getDataItem(i);
            if (delegate instanceof DownloadTaskDelegate) {
                if (((DownloadTaskDelegate) delegate).getSource().getId().equals(id)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void removeTaskItem (String id) {
        int index = getTaskIndexById(id);
        if (index < 0) {
            return;
        }
        removeDataItem(index);
        notifyDataSetChanged();
    }

}

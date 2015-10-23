package com.metis.base.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

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
}

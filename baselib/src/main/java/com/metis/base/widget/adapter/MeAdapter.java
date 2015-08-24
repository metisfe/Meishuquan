package com.metis.base.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.base.widget.adapter.holder.MeHeaderHolder;

/**
 * Created by Beak on 2015/8/24.
 */
public class MeAdapter extends DelegateAdapter {

    public MeAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case MeDelegateType.ID.ID_ME_HEADER:
                return new MeHeaderHolder(view);
        }
        throw new IllegalStateException("no layoutId found for viewType " + viewType);
    }
}

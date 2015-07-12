package com.metis.base.widget.adapter.debug;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.DelegateType;
import com.metis.base.widget.adapter.holder.AbsViewHolder;

/**
 * Created by gaoyunfei on 15/7/12.
 */
public class StringAdapter extends DelegateAdapter {

    public StringAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case DelegateType.ID.ID_DEBUG_STRING:
                return new StringHolder (view);
        }
        return null;
    }
}

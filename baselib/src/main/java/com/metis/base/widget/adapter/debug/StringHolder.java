package com.metis.base.widget.adapter.debug;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.metis.base.R;
import com.metis.base.widget.adapter.holder.AbsViewHolder;

/**
 * Created by gaoyunfei on 15/7/12.
 */
public class StringHolder extends AbsViewHolder<StringDelegate> {

    private TextView stringTv = null;

    public StringHolder(View itemView) {
        super(itemView);
        stringTv = (TextView)itemView.findViewById(R.id.debug_string);
    }

    @Override
    public void bindData(Context context, StringDelegate stringDelegate, RecyclerView.Adapter adapter, int position) {
        stringTv.setText(stringDelegate.getSource());
    }
}

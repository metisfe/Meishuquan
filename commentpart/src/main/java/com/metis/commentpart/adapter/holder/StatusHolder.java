package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterViewFlipper;

import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.ActivityDispatcher;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.FlipperAdapter;
import com.metis.commentpart.adapter.delegate.StatusDelegate;
import com.metis.commentpart.widget.ViewFlippable;

/**
 * Created by Beak on 2015/7/28.
 */
public class StatusHolder extends AbsViewHolder<StatusDelegate> implements ViewFlippable{

    public AdapterViewFlipper adapterViewFlipper = null;

    public StatusHolder(View itemView) {
        super(itemView);
        adapterViewFlipper = (AdapterViewFlipper)itemView.findViewById(R.id.status_flipper);
    }

    @Override
    public void bindData(final Context context, StatusDelegate statusDelegate, RecyclerView.Adapter adapter, int position) {
        String[] strings = statusDelegate.getSource();
        adapterViewFlipper.setAdapter(new FlipperAdapter(context, strings));
        adapterViewFlipper.startFlipping();

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.statusDetail(context);
            }
        });
    }

    @Override
    public AdapterViewFlipper getViewFlipper() {
        return adapterViewFlipper;
    }
}

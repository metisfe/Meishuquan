package com.metis.coursepart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.delegate.ItemTitleDelegate;

/**
 * Created by Beak on 2015/7/10.
 */
public class ItemTitleHolder extends AbsViewHolder<ItemTitleDelegate> {

    public TextView channelTv, moreBtn;

    public ItemTitleHolder(View itemView) {
        super(itemView);
        channelTv = (TextView)itemView.findViewById(R.id.title_text);
        moreBtn = (TextView)itemView.findViewById(R.id.title_sub_text);
    }

    @Override
    public void bindData(final Context context, final ItemTitleDelegate videoItemTitleDelegate, RecyclerView.Adapter adapter, int position) {
        channelTv.setText(videoItemTitleDelegate.getSource());
        if (position == 0) {
            itemView.setBackgroundResource(R.drawable.video_item_bg_nor_top);
        } else if (position == adapter.getItemCount() - 1) {
            itemView.setBackgroundResource(R.drawable.video_item_bg_nor_bottom);
        } else {
            itemView.setBackgroundResource(R.drawable.video_item_bg_nor);
        }
        if (videoItemTitleDelegate.isClickable()) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDispatcher.filterActivityWithState(context, videoItemTitleDelegate.getFilterId());
                }
            });
        }
    }
}

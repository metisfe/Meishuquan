package com.metis.meishuquan.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.ActivityDispatcher;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.delegate.DiscoveryItemDelegate;
import com.metis.meishuquan.module.DiscoveryItem;

/**
 * Created by Beak on 2015/10/21.
 */
public class DiscoveryItemHolder extends AbsViewHolder<DiscoveryItemDelegate> {

    private ImageView mIconIv = null;
    private TextView mTitleTv = null;

    public DiscoveryItemHolder(View itemView) {
        super(itemView);

        mIconIv = (ImageView)itemView.findViewById(R.id.icon_text_icon);
        mTitleTv = (TextView)itemView.findViewById(R.id.icon_text_text);
    }

    @Override
    public void bindData(final Context context, final DiscoveryItemDelegate discoveryItemDelegate, RecyclerView.Adapter adapter, int position) {
        final DiscoveryItem item = discoveryItemDelegate.getSource();
        if (discoveryItemDelegate.isNative()) {
            mIconIv.setImageResource(discoveryItemDelegate.getIconRes());
        } else {
            DisplayManager.getInstance(context).display(item.icon, mIconIv);
        }
        mTitleTv.setText(item.name);
        if (discoveryItemDelegate.getOnClickListener() != null) {
            itemView.setOnClickListener(discoveryItemDelegate.getOnClickListener());
        } else {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User me = AccountManager.getInstance(context).getMe();
                    String link = item.link;
                    if (me != null && item.linkArgs != null && item.linkArgs.contains("uid")) {
                        link = item.link + "?uid=" + me.userId;
                    }
                    ActivityDispatcher.innerBrowserActivity(context, link, item.allowShare);
                }
            });
        }
        itemView.setBackgroundResource(R.drawable.std_list_item_bg);
    }
}

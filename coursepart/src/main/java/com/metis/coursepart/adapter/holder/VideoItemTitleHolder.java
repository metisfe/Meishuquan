package com.metis.coursepart.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.metis.base.widget.holder.AbsViewHolder;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.delegate.VideoItemTitleDelegate;

/**
 * Created by Beak on 2015/7/10.
 */
public class VideoItemTitleHolder extends AbsViewHolder<VideoItemTitleDelegate> {

    public TextView channelTv, moreBtn;

    public VideoItemTitleHolder(View itemView) {
        super(itemView);
        channelTv = (TextView)itemView.findViewById(R.id.title_text);
        moreBtn = (TextView)itemView.findViewById(R.id.title_more);
    }

    @Override
    public void bindData(Context context, VideoItemTitleDelegate videoItemTitleDelegate) {
        channelTv.setText(videoItemTitleDelegate.getSource());
    }
}

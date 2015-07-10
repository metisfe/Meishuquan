package com.metis.coursepart.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.holder.AbsViewHolder;
import com.metis.coursepart.adapter.delegate.CourseDelegateType;
import com.metis.coursepart.adapter.holder.VideoItemSmallHolder;
import com.metis.coursepart.adapter.holder.VideoItemTitleHolder;

/**
 * Created by Beak on 2015/7/10.
 */
public class VideoSubAdapter extends DelegateAdapter {

    public VideoSubAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case CourseDelegateType.ID.ID_VIDEO_ITEM_SMALL:
                return new VideoItemSmallHolder(view);
            case CourseDelegateType.ID.ID_VIDEO_ITEM_SMALL_TITLE:
                return new VideoItemTitleHolder(view);
        }
        throw new Resources.NotFoundException("no AbsViewHolder for viewType=" + viewType);
    }
}

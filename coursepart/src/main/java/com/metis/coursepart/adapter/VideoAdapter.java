package com.metis.coursepart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.holder.AbsViewHolder;
import com.metis.coursepart.adapter.delegate.CourseDelegateType;
import com.metis.coursepart.adapter.holder.VideoItemHolder;

/**
 * Created by Beak on 2015/7/6.
 */
public class VideoAdapter extends DelegateAdapter {

    public VideoAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case CourseDelegateType.ID.ID_VIDEO_ITEM:
                return new VideoItemHolder(view);
        }
        return null;
    }
}

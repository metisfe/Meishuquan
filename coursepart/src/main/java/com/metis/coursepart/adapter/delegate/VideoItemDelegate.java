package com.metis.coursepart.adapter.delegate;

import com.metis.base.widget.delegate.BaseDelegate;
import com.metis.coursepart.adapter.VideoSubAdapter;
import com.metis.coursepart.module.CourseAlbum;

import java.util.List;

/**
 * Created by Beak on 2015/7/10.
 */
public class VideoItemDelegate extends BaseDelegate<List<CourseAlbum>> {

    private VideoSubAdapter mSubAdapter = null;
    private int mSubRvHeight = -1;

    public VideoItemDelegate(List<CourseAlbum> courseAlbums) {
        super(courseAlbums);
    }

    @Override
    public int getDelegateType() {
        return CourseDelegateType.TYPE_VIDEO_ITEM.getType();
    }

    public VideoSubAdapter getSubAdapter() {
        return mSubAdapter;
    }

    public void setSubAdapter(VideoSubAdapter mSubAdapter) {
        this.mSubAdapter = mSubAdapter;
    }

    public int getSubRecyclerViewHeight() {
        return mSubRvHeight;
    }

    public void setSubRecyclerViewHeight(int mSubRvHeight) {
        this.mSubRvHeight = mSubRvHeight;
    }
}

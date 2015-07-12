package com.metis.coursepart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.coursepart.adapter.AlbumAdapter;
import com.metis.coursepart.module.CourseAlbum;

import java.util.List;

/**
 * Created by Beak on 2015/7/10.
 */
public class AlbumContainerDelegate extends BaseDelegate<List<CourseAlbum>> {

    private AlbumAdapter mSubAdapter = null;
    private int mSubRvHeight = -1;

    public AlbumContainerDelegate(List<CourseAlbum> courseAlbums) {
        super(courseAlbums);
    }

    @Override
    public int getDelegateType() {
        return CourseDelegateType.TYPE_VIDEO_ITEM.getType();
    }

    public AlbumAdapter getSubAdapter() {
        return mSubAdapter;
    }

    public void setSubAdapter(AlbumAdapter mSubAdapter) {
        this.mSubAdapter = mSubAdapter;
    }

    public int getSubRecyclerViewHeight() {
        return mSubRvHeight;
    }

    public void setSubRecyclerViewHeight(int mSubRvHeight) {
        this.mSubRvHeight = mSubRvHeight;
    }
}

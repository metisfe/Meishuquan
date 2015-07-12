package com.metis.coursepart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.coursepart.module.CourseAlbum;

/**
 * Created by Beak on 2015/7/10.
 */
public class AlbumSmallDelegate extends BaseDelegate<CourseAlbum> {

    public AlbumSmallDelegate(CourseAlbum courseAlbum) {
        super(courseAlbum);
    }

    @Override
    public int getDelegateType() {
        return CourseDelegateType.TYPE_VIDEO_ITEM_SMALL.getType();
    }
}

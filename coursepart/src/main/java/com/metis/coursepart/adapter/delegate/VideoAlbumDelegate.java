package com.metis.coursepart.adapter.delegate;

import com.metis.base.widget.delegate.AbsDelegate;
import com.metis.base.widget.delegate.BaseDelegate;
import com.metis.coursepart.module.CourseAlbum;

/**
 * Created by Beak on 2015/7/10.
 */
public class VideoAlbumDelegate extends BaseDelegate<CourseAlbum> {

    public VideoAlbumDelegate(CourseAlbum courseAlbum) {
        super(courseAlbum);
    }

    @Override
    public int getDelegateType() {
        return CourseDelegateType.TYPE_VIDEO_ITEM_SMALL.getType();
    }
}

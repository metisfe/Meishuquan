package com.metis.coursepart.adapter.delegate;

import com.metis.base.widget.delegate.BaseDelegate;
import com.metis.coursepart.module.CourseAlbum;

import java.util.List;

/**
 * Created by Beak on 2015/7/10.
 */
public class VideoItemDelegate extends BaseDelegate<List<CourseAlbum>> {

    public VideoItemDelegate(List<CourseAlbum> courseAlbums) {
        super(courseAlbums);
    }

    @Override
    public int getDelegateType() {
        return 0;
    }
}

package com.metis.coursepart.adapter.delegate;

import com.metis.base.module.User;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.coursepart.module.ContentItem;
import com.metis.coursepart.module.CourseAlbum;

import java.util.List;

/**
 * Created by Beak on 2015/7/15.
 */
public class UserInDetailDelegate extends BaseDelegate<CourseAlbum> {

    private String mWebContent = null;

    private List<ContentItem> mContentItemList = null;

    public UserInDetailDelegate(CourseAlbum album) {
        super(album);
    }

    public String getWebContent() {
        return mWebContent;
    }

    public void setWebContent(String mWebContent) {
        this.mWebContent = mWebContent;
    }

    public List<ContentItem> getContentItemList() {
        return mContentItemList;
    }

    public void setContentItemList(List<ContentItem> mContentItemList) {
        this.mContentItemList = mContentItemList;
    }

    @Override
    public int getDelegateType() {
        return CourseDelegateType.TYPE_USER_IN_DETAIL.getType();
    }
}

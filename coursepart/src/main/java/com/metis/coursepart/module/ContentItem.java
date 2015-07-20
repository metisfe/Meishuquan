package com.metis.coursepart.module;

/**
 * Created by Beak on 2015/7/15.
 */
public class ContentItem {
    public String type;
    public ContentInnerItem data;

    public boolean isTxt () {
        return type.equals("TXT");
    }

}

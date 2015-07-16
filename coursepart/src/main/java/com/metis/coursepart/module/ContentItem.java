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

    public class ContentInnerItem {
        public String Content;
        public String ContentType;

        public String URL;
        public String ThumbnailsURL;
        public int Width;
        public int Height;
        public String Desc;
    }
}

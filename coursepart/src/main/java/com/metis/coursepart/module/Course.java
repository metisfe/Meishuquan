package com.metis.coursepart.module;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.base.utils.Log;

import java.util.List;

/**
 * Created by Beak on 2015/7/9.
 */
public class Course {

    private static final String TAG = Course.class.getSimpleName();

    public long subCourseId;
    public long mainCourseId;
    public String subCourseName;
    public String description;
    public boolean display;
    public int viewsCount;
    public int shareCount;
    public int commentCount;
    public boolean useState;
    public String videoPic;
    public String videoUrl;
    public String videoSize;
    public String videoTime;
    public String content;
    public String webContent;

    private List<ContentItem> mContentItemList = null;

    public List<ContentItem> getContentItemList () {
        Log.v(TAG, "getContentItemList content=" + content);
        if (mContentItemList == null) {
            if (content != null) {
                Gson gson = new Gson();
                mContentItemList = gson.fromJson(
                        content,
                        new TypeToken<List<ContentItem>>(){}.getType());
            }
        }
        return mContentItemList;
    }
}

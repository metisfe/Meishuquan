package com.metis.coursepart.module;

import com.metis.base.module.UserMark;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Beak on 2015/7/9.
 */
public class CourseAlbum implements Serializable{
    public String coursePic;
    public int courseId;
    public String title;
    public Author author;
    public StudioInfo studio;
    /*public Date createTime;
    public Date publishTime;*/
    public int viewCount;
    public String desc;
    public int commentCount;
    public UserMark userMark;
    private String channel;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}

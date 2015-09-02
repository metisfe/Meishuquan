package com.metis.newslib.module;

import com.metis.base.module.User;

import java.io.Serializable;

/**
 * Created by Beak on 2015/9/2.
 */
public class NewsDetails implements Serializable {
    public long newsId;
    public String title;
    public String subTitle;
    public String author;
    public NewsItem.NewsSource source;
    public boolean commentEnable;
    public String modifyTime;
    public String content;
    public String description;
    public String[] urls;
    public String commentDefaultText;
    public String thumbnail;
    public String videoPic;
}

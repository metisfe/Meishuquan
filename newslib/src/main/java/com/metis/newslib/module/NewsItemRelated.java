package com.metis.newslib.module;

import java.io.Serializable;

/**
 * Created by Beak on 2015/9/4.
 */
public class NewsItemRelated implements Serializable {
    public long newsId;
    public String title;
    public String modifyTime;
    public String source;
    public int pageViewCount;
}

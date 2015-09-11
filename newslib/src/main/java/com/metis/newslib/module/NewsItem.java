package com.metis.newslib.module;

import java.io.Serializable;

/**
 * Created by Beak on 2015/9/2.
 */
public class NewsItem implements Serializable {
    public String title;
    public String desc;
    public long newsId;
    public NewsSource source;
    public String createTime;
    public String imgUrl;
    public int pageViewCount;
    public int commentCount;

    public class NewsSource implements Serializable {
        public long id;
        public String title;
        public int goal;
    }
}

package com.metis.newslib.module;

import com.metis.base.module.User;
import com.metis.base.module.UserMark;

import java.io.Serializable;

public class NewsCommentItem implements Serializable {
        public long id;
        public User user;
        public String content;
        public String commentDateTime;
        public int replyCount;
        public int supportCount;
        public User replyUser;
        public UserMark userMark;
    }
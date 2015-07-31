package com.metis.commentpart.module;

import com.metis.base.module.User;
import com.metis.base.module.UserMark;

import java.util.List;

/**
 * Created by Beak on 2015/7/30.
 */
public class Comment {
    public long id;
    public User user;
    public int supportCount;
    public String content;
    public String commentDateTime;
    public int replyCount;
    public User replyUser;
    public long replyCid;
    public List<CommentAttachment> imgOrVoiceUrl;
    public int commentType;
    public UserMark userMark;
}

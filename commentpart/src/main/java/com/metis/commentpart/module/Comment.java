package com.metis.commentpart.module;

import com.metis.base.module.User;
import com.metis.base.module.UserMark;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Beak on 2015/7/30.
 */
public class Comment implements Serializable {

    public static final int COMMENT_TYPE_IMG = 1, COMMENT_TYPE_VOICE = 2, COMMENT_TYPE_TEXT = 3;

    public long id;
    public User user;
    public int supportCount;
    public String content;
    public String commentDateTime;
    public int replyCount;
    public User replyUser;
    public long replyCid;
    public CommentAttachment imgOrVoiceUrl;
    public int commentType;
    public UserMark userMark;

}

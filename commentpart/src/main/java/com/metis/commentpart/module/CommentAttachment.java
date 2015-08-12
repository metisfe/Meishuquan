package com.metis.commentpart.module;

import java.io.Serializable;

/**
 * Created by Beak on 2015/7/30.
 */
public class CommentAttachment implements Serializable{
    public long imgId;
    public long assessCommentID;
    public String originalImage;
    public String thumbnails;
    public String voiceUrl;
    public int thumbnailsHeight;
    public int thumbnailsWidth;
    public int voiceLength;
}

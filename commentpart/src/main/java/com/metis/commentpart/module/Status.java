package com.metis.commentpart.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.metis.base.module.ImageInfo;
import com.metis.base.module.Thumbnail;
import com.metis.base.module.User;
import com.metis.base.module.UserMark;
import com.metis.base.widget.ImagePreviewable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Beak on 2015/8/3.
 */
public class Status implements Serializable {
    public long id;
    public User user;
    public int commentCount;
    public ImageInfo img;
    public String desc;
    public int supportCount;
    public String createTime;
    public String updateTime;
    public UserMark userMark;
    public List<Comment> teacherCommentList;
    public ChannelItem channel;

}

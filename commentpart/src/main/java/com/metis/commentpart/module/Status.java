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
public class Status implements Serializable, ImagePreviewable {
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

    @Override
    public String getThumbnail() {
        if (img != null) {
            return img.imgThumbnailUrl;
        }
        return null;
    }

    @Override
    public String getUrl() {
        if (img != null) {
            return img.imgUrl;
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, 1);
        dest.writeParcelable(img, 2);
    }

    public static final Parcelable.Creator<Status> CREATOR
            = new Parcelable.Creator<Status>() {
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        public Status[] newArray(int size) {
            return new Status[size];
        }
    };

    private Status(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        img = in.readParcelable(ImageInfo.class.getClassLoader());
    }
}

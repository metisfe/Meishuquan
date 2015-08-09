package com.metis.base.module;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Beak on 2015/7/9.
 */
public class User implements Serializable, Parcelable{

    public static final int
            USER_ROLE_STUDENT = 1,
            USER_ROLE_TEACHER = 2,
            USER_ROLE_STUDIO = 3,
            USER_ROLE_PARENTS = 4;

    private long id;
    public long userId;
    public String name;
    public String remarkName;
    public String avatar;
    public String grade;
    public int identity;
    public int relation;
    public String account;
    //public String rongCloud;
    public String location;
    public int userRole; //1,teacher 2,student 3,studio 4,parents
    public String message;
    public String requestInfo;
    private String cookie;

    public User () {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(userId);
        dest.writeString(name);
        dest.writeString(remarkName);
        dest.writeString(avatar);
        dest.writeString(grade);
        dest.writeInt(identity);
        dest.writeInt(relation);
        dest.writeString(account);
        dest.writeString(location);
        dest.writeInt(userRole);
        dest.writeString(message);
        dest.writeString(requestInfo);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        userId = in.readLong();
        name = in.readString();
        remarkName = in.readString();
        avatar = in.readString();
        grade = in.readString();
        identity = in.readInt();
        relation = in.readInt();
        account = in.readString();
        location = in.readString();
        userRole = in.readInt();
        message = in.readString();
        requestInfo = in.readString();
    }

    public String getCookie() {
        return cookie;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof User)) {
            return false;
        }
        return userId == ((User) o).userId;
    }
}

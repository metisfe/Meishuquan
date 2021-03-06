package com.metis.base.module;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.metis.base.utils.Log;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by Beak on 2015/7/9.
 */
public class User implements Serializable, Parcelable{

    private static final String TAG = User.class.getSimpleName();

    public static final int
            USER_ROLE_STUDENT = 1,
            USER_ROLE_TEACHER = 2,
            USER_ROLE_STUDIO = 3,
            USER_ROLE_PARENTS = 4,
            USER_ROLE_FANS = 5;

    private long id;
    public long userId;
    public String name;
    public String remarkName;
    public String avatar;
    public String userAvatar;
    private String gender;
    public String grade;
    public int identity;
    public int relation;
    public String account;
    //public String rongCloud;
    public String location;
    public int userRole; //1,teacher 2,student 3,studio 4,parents
    public String message;
    public String requestInfo;
    public String province;
    public Province provinceEntity;
    public int fansNum;
    public int focusNum;
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
        dest.writeString(userAvatar);
        dest.writeString(gender);
        dest.writeString(grade);
        dest.writeInt(identity);
        dest.writeInt(relation);
        dest.writeString(account);
        dest.writeString(location);
        dest.writeInt(userRole);
        dest.writeString(message);
        dest.writeString(requestInfo);
        dest.writeString(cookie);
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
        userAvatar = in.readString();
        gender = in.readString();
        grade = in.readString();
        identity = in.readInt();
        relation = in.readInt();
        account = in.readString();
        location = in.readString();
        userRole = in.readInt();
        message = in.readString();
        requestInfo = in.readString();
        cookie = in.readString();
    }

    public String getAvailableAvatar () {
        Log.v(TAG, "getAvailableAvatar avatar=" + avatar + " userAvatar=" + userAvatar);
        String result = avatar;
        if (TextUtils.isEmpty(result)) {
            result = userAvatar;
        }
        if (result != null && result.startsWith("http:/") && !result.startsWith("http://")) {
            result = result.replace("http:/", "http://");
        }
        if (result == null) {
            result = "";
        }
        return result;
        /*if (!TextUtils.isEmpty(avatar)) {
            return avatar;
        }
        if (!TextUtils.isEmpty(userAvatar)) {
            return userAvatar;
        }
        return "";*/
    }

    public String getCookie() {
        return cookie;
    }

    public void setUserAvatar (String avatar) {
        userAvatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof User)) {
            return false;
        }
        return userId == ((User) o).userId;
    }

    public void mergeFrom (User user) {
        if (user == null) {
            return;
        }
        if (this.userId != user.userId) {
            return;
        }
        Field[] fields = getClass().getFields();
        final int length = fields.length;
        for (int i = 0; i < length; i++) {
            Field field = fields[i];
            try {
                Object value = field.get(user);
                if (value != null) {
                    Log.v(TAG, "mergeFrom key=" + field.getName() + " value=" + value);
                    field.set(this, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}

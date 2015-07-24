package com.metis.coursepart.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Beak on 2015/7/15.
 */
public class KeyWord implements Parcelable{
    public long keyWordId;
    public String keyWordName;
    public String description;
    public boolean enable;
    public String createTime;
    public int keyWordType;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(keyWordId);
        dest.writeString(keyWordName);
        dest.writeString(description);
        dest.writeString(createTime);
        dest.writeInt(keyWordType);
    }

    public static final Parcelable.Creator<KeyWord> CREATOR
            = new Parcelable.Creator<KeyWord>() {
        public KeyWord createFromParcel(Parcel in) {
            return new KeyWord(in);
        }

        public KeyWord[] newArray(int size) {
            return new KeyWord[size];
        }
    };

    private KeyWord(Parcel in) {
        keyWordId = in.readLong();
        keyWordName = in.readString();
        description = in.readString();
        createTime = in.readString();
        keyWordType = in.readInt();
    }
}

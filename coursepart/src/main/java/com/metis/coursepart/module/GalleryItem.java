package com.metis.coursepart.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.base.widget.ImagePreviewable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/8.
 */
public class GalleryItem implements ImagePreviewable{

    private static final String TAG = GalleryItem.class.getSimpleName();

    public long picId;
    public long galleryId;
    public String picName;
    public String descripiton;
    public String thumbnailUrl;
    public int width;
    public int height;
    public String originalUrl;
    public String hdUrl;
    public int viewCount;
    public int commentCount;
    public int shareCount;
    public boolean userState;
    public User studio;
    public long studioId;
    public boolean display;
    public int picType;
    public String publishTime;
    public String picKeyWordList;
    public String source;
    public KeyWord[] keyWordList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(picId);
        dest.writeLong(galleryId);
        dest.writeString(picName);
        dest.writeString(descripiton);
        dest.writeString(thumbnailUrl);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(originalUrl);
        dest.writeString(hdUrl);
        dest.writeInt(viewCount);
        dest.writeInt(commentCount);
        dest.writeInt(shareCount);
        dest.writeInt(userState ? 1 : 0);
        dest.writeParcelable(studio, flags);
        dest.writeLong(studioId);
        dest.writeInt(display ? 1 : 0);
        dest.writeInt(picType);
        dest.writeString(publishTime);
        dest.writeString(picKeyWordList);
        dest.writeString(source);
        dest.writeParcelableArray(keyWordList, flags);
    }

    public static final Parcelable.Creator<GalleryItem> CREATOR = new Parcelable.Creator<GalleryItem>()
    {
        public GalleryItem createFromParcel(Parcel in)
        {
            return new GalleryItem(in);
        }

        public GalleryItem[] newArray(int size)
        {
            return new GalleryItem[size];
        }
    };

    /*public long picId;
    public long galleryId;
    public String picName;
    public String descripiton;
    public String thumbnailUrl;
    public int width;
    public int height;
    public String originalUrl;
    public String hdUrl;
    public int viewCount;
    public int commentCount;
    public int shareCount;
    public boolean userState;
    public User studio;
    public long studioId;
    public boolean display;
    public int picType;
    public String publishTime;
    public String picKeyWordList;
    public String source;
    public KeyWord[] keyWordList;*/

    private GalleryItem (Parcel in) {
        picId = in.readLong();
        galleryId = in.readLong();
        picName = in.readString();
        descripiton = in.readString();
        thumbnailUrl = in.readString();
        width = in.readInt();
        height = in.readInt();
        originalUrl = in.readString();
        hdUrl = in.readString();
        viewCount = in.readInt();
        commentCount = in.readInt();
        shareCount = in.readInt();
        userState = in.readInt() > 0;
        studio = in.readParcelable(User.class.getClassLoader());
        studioId = in.readLong();
        display = in.readInt() > 0;
        picType = in.readInt();
        publishTime = in.readString();
        picKeyWordList = in.readString();
        source = in.readString();
        Parcelable[] parcelables = in.readParcelableArray(KeyWord.class.getClassLoader());
        if (parcelables != null) {
            final int length = parcelables.length;
            keyWordList = new KeyWord[length];
            for (int i = 0; i < length; i++) {
                Parcelable parcelable = parcelables[i];
                if (parcelable != null) {
                    if (parcelable instanceof KeyWord) {
                        keyWordList[i] = (KeyWord)parcelable;
                    }

                }
            }
            //keyWordList = KeyWord.CREATOR.newArray(parcelables.length);
        }
    }

    public GalleryItem () {}

    @Override
    public String getThumbnail() {
        return thumbnailUrl;
    }

    @Override
    public String getUrl() {
        return originalUrl;
    }
}

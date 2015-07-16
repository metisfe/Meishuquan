package com.metis.coursepart.module;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/8.
 */
public class GalleryItem implements Parcelable{
    /*public String url = null;
    public int width, height;
    public List<String> tags = null;
    public String source;
    public int count;*/

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
    public StudioInfo studio;
    public long studioId;
    public boolean display;
    public int picType;
    public String publishTime;
    public String picKeyWordList;
    public List<KeyWord> keyWordList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        /*dest.writeString(url);
        dest.writeString(source);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeInt(count);
        dest.writeStringList(tags);*/
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

    private GalleryItem (Parcel in) {
        /*url = in.readString();
        source = in.readString();
        width = in.readInt();
        height = in.readInt();
        count = in.readInt();
        if (tags == null) {
            tags = new ArrayList<String>();
        }
        in.readStringList(tags);*/
    }

    public GalleryItem () {}
}

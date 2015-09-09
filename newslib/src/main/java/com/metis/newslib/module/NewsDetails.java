package com.metis.newslib.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.base.module.User;
import com.metis.base.module.UserMark;
import com.metis.base.widget.ImagePreviewable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Beak on 2015/9/2.
 */
public class NewsDetails implements Serializable {
    public long newsId;
    public String title;
    public String subTitle;
    public String author;
    public NewsItem.NewsSource source;
    public boolean commentEnable;
    public String modifyTime;
    public String content;
    public String description;
    public Url[] urls;
    public String commentDefaultText;
    public String thumbnail;
    public String videoPic;
    public User user;
    public List<NewsItemRelated> relatedNewsList;
    public UserMark userMark;

    private List<Item> mItemList = null;

    public List<Item> getItemList () {
        if (mItemList == null) {
            mItemList = new Gson().fromJson(
                    content, new TypeToken<List<Item>>(){}.getType());
        }
        return mItemList;
    }

    public static class Item implements Serializable {
        public String type;
        public ItemData data;

        public boolean isTxt () {
            return "txt".equalsIgnoreCase(type);
        }

        public boolean isImg () {
            return "img".equalsIgnoreCase(type);
        }

        public boolean isVideo () {
            return "VOIDE".equalsIgnoreCase(type);
        }
    }

    public static class ItemData implements ImagePreviewable, Serializable {
        public String URL;
        public String ThumbnailsURL;
        public int Width, Height;
        public String Content;
        public String ContentType;

        @Override
        public String getThumbnail() {
            return ThumbnailsURL;
        }

        @Override
        public String getUrl() {
            return URL;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(URL);
            dest.writeString(ThumbnailsURL);
            dest.writeInt(Width);
            dest.writeInt(Height);
        }

        public static final Parcelable.Creator<ItemData> CREATOR = new Parcelable.Creator<ItemData>()
        {
            public ItemData createFromParcel(Parcel in)
            {
                return new ItemData(in);
            }

            public ItemData[] newArray(int size)
            {
                return new ItemData[size];
            }
        };

        private ItemData(Parcel in)
        {
            URL = in.readString();
            ThumbnailsURL = in.readString();
            Width = in.readInt();
            Height = in.readInt();
        }
    }

    public static class Url implements Serializable {
        public long newsUploadID;
        public int upType;
        public long newsid;
        public String dir;
        public String ext;
        public String time;
        public int width, height;
        public String description;
        public String newShowContent;
    }
}

package com.metis.base.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.metis.base.widget.ImagePreviewable;

import java.io.Serializable;

/**
 * Created by Beak on 2015/8/3.
 */
public class ImageInfo implements Serializable, ImagePreviewable {

    private long id;

    public String imgUrl;
    public String imgThumbnailUrl;
    public int imgWidth;
    public int imgHeight;

    public ImageInfo () {}

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgThumbnailUrl() {
        return imgThumbnailUrl;
    }

    public void setImgThumbnailUrl(String imgThumbnailUrl) {
        this.imgThumbnailUrl = imgThumbnailUrl;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }


    public static final Parcelable.Creator<ImageInfo> CREATOR = new Parcelable.Creator<ImageInfo>()
    {
        public ImageInfo createFromParcel(Parcel in)
        {
            return new ImageInfo(in);
        }

        public ImageInfo[] newArray(int size)
        {
            return new ImageInfo[size];
        }
    };

    private ImageInfo(Parcel in) {
        imgThumbnailUrl = in.readString();
        imgUrl = in.readString();
        imgWidth = in.readInt();
        imgHeight = in.readInt();
    }

    @Override
    public String getThumbnail() {
        return imgThumbnailUrl;
    }

    @Override
    public String getUrl() {
        return imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgThumbnailUrl);
        dest.writeString(imgUrl);
        dest.writeInt(imgWidth);
        dest.writeInt(imgHeight);
    }
}

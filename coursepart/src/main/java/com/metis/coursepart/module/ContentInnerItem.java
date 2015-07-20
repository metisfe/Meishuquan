package com.metis.coursepart.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.metis.base.widget.ImagePreviewable;

public class ContentInnerItem implements ImagePreviewable {
        public String Content;
        public String ContentType;

        public String URL;
        public String ThumbnailsURL;
        public int Width;
        public int Height;
        public String Desc;

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
            dest.writeString(Content);
            dest.writeString(ContentType);

            dest.writeString(URL);
            dest.writeString(ThumbnailsURL);
            dest.writeInt(Width);
            dest.writeInt(Height);
            dest.writeString(Desc);
        }

        public static final Parcelable.Creator<ContentInnerItem> CREATOR = new Parcelable.Creator<ContentInnerItem>() {
            public ContentInnerItem createFromParcel(Parcel in)
            {
                return new ContentInnerItem(in);
            }

            public ContentInnerItem[] newArray(int size)
            {
                return new ContentInnerItem[size];
            }
        };

        private ContentInnerItem(Parcel in) {
            Content = in.readString();
            ContentType = in.readString();

            URL = in.readString();
            ThumbnailsURL = in.readString();
            Width = in.readInt();
            Height = in.readInt();
            Desc = in.readString();

        }

        public ContentInnerItem () {}
    }
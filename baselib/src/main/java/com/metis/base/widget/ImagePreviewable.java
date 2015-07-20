package com.metis.base.widget;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Beak on 2015/7/20.
 */
public interface ImagePreviewable extends Parcelable {
    public String getThumbnail ();
    public String getUrl ();
}

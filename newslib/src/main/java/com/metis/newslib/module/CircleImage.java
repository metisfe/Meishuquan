package com.metis.newslib.module;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jiaxh on 4/18/2015.
 */
public class CircleImage {
    @SerializedName("id")
    public int Id;

    @SerializedName("originalImage")
    public String OriginalImage;

    @SerializedName("thumbnails")
    public String Thumbnails ;

    @SerializedName("voiceUrl")
    public String VoiceUrl ;

    @SerializedName("thumbnailsHeight")
    public int ThumbnailsHeight ;

    @SerializedName("thumbnailsWidth")
    public int ThumbnailsWidth ;

    @SerializedName("circleId")
    public int CircleId ;
}

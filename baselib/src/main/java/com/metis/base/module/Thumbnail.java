package com.metis.base.module;

import java.io.Serializable;

/**
 * Created by Beak on 2015/7/30.
 */
public class Thumbnail implements Serializable {
    public String originalImage;
    public String thumbnails;
    public int thumbnailsHeight;
    public int thumbnailsWidth;

    public String getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(String originalImage) {
        this.originalImage = originalImage;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public int getThumbnailsHeight() {
        return thumbnailsHeight;
    }

    public void setThumbnailsHeight(int thumbnailsHeight) {
        this.thumbnailsHeight = thumbnailsHeight;
    }

    public int getThumbnailsWidth() {
        return thumbnailsWidth;
    }

    public void setThumbnailsWidth(int thumbnailsWidth) {
        this.thumbnailsWidth = thumbnailsWidth;
    }

    public ImageInfo toImageInfo () {
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setImgUrl(originalImage);
        imageInfo.setImgThumbnailUrl(thumbnails);
        imageInfo.setImgWidth(thumbnailsWidth);
        imageInfo.setImgHeight(thumbnailsHeight);
        return imageInfo;
    }
}

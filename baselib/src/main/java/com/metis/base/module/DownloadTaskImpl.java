package com.metis.base.module;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.bokecc.sdk.mobile.download.Downloader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DownloadTaskImpl {

    private String id, groupId, sourceUrl, targetPath, name, description;
    private @State int state = Downloader.WAIT;
    private int progress;
    private long position, length;
    private int speed = 0;

    @IntDef({Downloader.WAIT, Downloader.DOWNLOAD, Downloader.PAUSING, Downloader.PAUSE, Downloader.FINISH})
    @Retention(RetentionPolicy.SOURCE)
    public static @interface State{};

    public @NonNull String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
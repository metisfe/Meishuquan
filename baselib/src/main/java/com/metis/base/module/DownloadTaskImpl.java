package com.metis.base.module;

import android.support.annotation.IntDef;

import com.bokecc.sdk.mobile.download.Downloader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DownloadTaskImpl {

        private String id, groupId, sourceUrl, targetPath, name, description;
        private @State int state;
        private float prgoress;
        private long position, length;

        @IntDef({Downloader.WAIT, Downloader.DOWNLOAD, Downloader.PAUSING, Downloader.PAUSE, Downloader.FINISH})
        @Retention(RetentionPolicy.SOURCE)
        public static @interface State{};

        public String getId() {
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

        public float getPrgoress() {
            return prgoress;
        }

        public void setPrgoress(float prgoress) {
            this.prgoress = prgoress;
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
    }
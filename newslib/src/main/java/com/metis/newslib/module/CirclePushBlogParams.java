package com.metis.newslib.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/5/10.
 */
public class CirclePushBlogParams implements Serializable {

    private String Region = "";
    private String Content = "";
    private List<Integer> UserIds = null;//@谁合集
    private List<CircleImage> Images = null;
    private String Device = "";
    private int RelayId = 0;//非转发圈子类型:新闻，活动等
    private int Type = 0;
    private int lastCircleId = 0;//转发圈子类型，为日志的ID

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public List<Integer> getUserIds() {
        if (UserIds == null) {
            UserIds = new ArrayList<Integer>();
        }
        return UserIds;
    }

    public void setUserIds(List<Integer> userIds) {
        UserIds = userIds;
    }

    public List<CircleImage> getImages() {
        if (Images == null) {
            Images = new ArrayList<CircleImage>();
        }
        return Images;
    }

    public void setImages(List<CircleImage> images) {
        Images = images;
    }

    public String getDevice() {
        return Device;
    }

    public void setDevice(String device) {
        Device = device;
    }

    public int getRelayId() {
        return RelayId;
    }

    public void setRelayId(int relayId) {
        RelayId = relayId;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getLastCircleId() {
        return lastCircleId;
    }

    public void setLastCircleId(int lastCircleId) {
        this.lastCircleId = lastCircleId;
    }
}

package com.metis.coursepart.module;

import java.io.Serializable;

/**
 * Created by Beak on 2015/7/3.
 */
public class Channel implements Serializable {
    public int channelId;
    public String channelName;
    public boolean isAllowReset;
    public int orderNum;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public boolean isAllowReset() {
        return isAllowReset;
    }

    public void setIsAllowReset(boolean isAllowReset) {
        this.isAllowReset = isAllowReset;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}

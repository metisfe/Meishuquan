package com.metis.newslib.module;

import java.io.Serializable;

/**
 * Created by Beak on 2015/9/2.
 */
public class ChannelItem implements Serializable {
    public long channelId;
    public String channelName;
    public boolean isAllowReset;
    public int orderNum;
}

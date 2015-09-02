package com.metis.newslib.module;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Beak on 2015/9/2.
 */
public class ChannelList implements Serializable {
    public List<ChannelItem> selectedChannels;
    public List<ChannelItem> unSelectedChannels;
}

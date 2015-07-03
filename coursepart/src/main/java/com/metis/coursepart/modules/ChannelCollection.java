package com.metis.coursepart.modules;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/3.
 */
public class ChannelCollection implements Serializable{

    ArrayList<Channel> selectedChannels;
    ArrayList<Channel> unSelectedChannels;

    public List<Channel> getSelectedChannels() {
        return selectedChannels;
    }

    public void setSelectedChannels(ArrayList<Channel> selectedChannels) {
        this.selectedChannels = selectedChannels;
    }

    public List<Channel> getUnSelectedChannels() {
        return unSelectedChannels;
    }

    public void setUnSelectedChannels(ArrayList<Channel> unSelectedChannels) {
        this.unSelectedChannels = unSelectedChannels;
    }
}

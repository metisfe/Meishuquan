package com.metis.meishuquan.module;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Beak on 2015/10/21.
 */
public class DiscoveryItem implements Serializable {
    public long id;
    public String name;
    public String icon;
    public String link;
    public List<String> linkArgs;
    public int order;
    public boolean isShow;
    public boolean allowShare;
}

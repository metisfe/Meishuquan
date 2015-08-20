package com.metis.base.module;

import java.io.Serializable;

/**
 * Created by Beak on 2015/8/20.
 */
public class Province implements Serializable {
    public long codeid;
    public String cityName;
    public boolean hotCity;
    public int studioCount;
    public long parentid;
}

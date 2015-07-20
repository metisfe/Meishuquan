package com.metis.base.module;

import java.io.Serializable;

/**
 * Created by Beak on 2015/7/9.
 */
public class UserMark implements Serializable {
    public boolean isFavorite;
    public boolean isSupport;
    public boolean isOpposition;
    public boolean isCanDel;
    public boolean isReplyed;
    public boolean isAttention;
    public boolean mutualAttention;
}

package com.metis.base.module;

import java.io.Serializable;

/**
 * Created by Beak on 2015/10/10.
 */
public class SimpleProvince implements Serializable {
    public int provinceId;
    public String name;

    @Override
    public String toString() {
        return name;
    }
}

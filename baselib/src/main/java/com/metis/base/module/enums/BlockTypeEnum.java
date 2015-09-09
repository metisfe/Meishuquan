package com.metis.base.module.enums;

/**
 * Created by wangjin on 15/4/16.
 */
public enum BlockTypeEnum {
    TOPLINE(0), COURSE(2);
    private final int val;

    private BlockTypeEnum(int val) {
        this.val = val;
    }

    public int getVal() {
        return this.val;
    }
}

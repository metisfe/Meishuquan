package com.metis.msnetworklib.contract;

import android.text.TextUtils;

/**
 * Created by wudi on 3/17/2015.
 */
public class OptionSettings {
    @com.google.gson.annotations.SerializedName("status")
    public String status;

    @com.google.gson.annotations.SerializedName("message")
    public String message;

    @com.google.gson.annotations.SerializedName("errorCode")
    public String errorCode;

    public boolean isSuccess() {
        if (!TextUtils.isEmpty(status) && status.equals("0"))
            return true;
        return false;
    }
}

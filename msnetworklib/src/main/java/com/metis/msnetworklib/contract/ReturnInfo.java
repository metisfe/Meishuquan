package com.metis.msnetworklib.contract;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wudi on 3/17/2015.
 */
public class ReturnInfo<E> {
    @SerializedName("option")
    private OptionSettings option;

    @SerializedName("data")
    private E data;

    public String getStatus() {
        if (option != null && option.status != null) {
            return option.status;
        }
        return "";
    }

    public String getErrorCode() {
        if (option != null && option.errorCode != null) {
            return option.errorCode;
        }
        return "";
    }

    public String getMessage() {
        if (option != null && option.message != null) {
            return option.message;
        }
        return "";
    }

    public boolean isSuccess(){
        return option != null && option.isSuccess();
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public OptionSettings getOption() {
        return option;
    }

    public void setOption(OptionSettings option) {
        this.option = option;
    }

}

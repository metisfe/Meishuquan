package com.metis.base.manager;

import com.metis.msnetworklib.contract.ReturnInfo;

/**
 * Created by Beak on 2015/7/9.
 */
public interface RequestCallback<T> {
    public void callback (ReturnInfo<T> returnInfo, String callbackId);
}

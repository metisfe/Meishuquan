package com.metis.msnetworklib.framework.runner;

import java.io.BufferedInputStream;

/**
 * Created by wudi on 3/15/2015.
 */
public interface WebRunnerListener<T>
{
    public void onCompleted(AsyncResult<T> result);

    public T onHandleData(BufferedInputStream is, RunnerProgress progress);

    public T getObjectFromCache(String url);
}
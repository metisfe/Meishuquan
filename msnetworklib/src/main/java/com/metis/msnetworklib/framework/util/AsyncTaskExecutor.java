package com.metis.msnetworklib.framework.util;

import android.os.AsyncTask;

/**
 * Created by wudi on 3/15/2015.
 */
public interface AsyncTaskExecutor
{

    public <T> void execute(AsyncTask<T, ?, ?> task, T... args);
}

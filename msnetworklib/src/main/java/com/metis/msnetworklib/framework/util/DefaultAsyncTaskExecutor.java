package com.metis.msnetworklib.framework.util;

import android.os.AsyncTask;

/**
 * Created by wudi on 3/15/2015.
 */
public class DefaultAsyncTaskExecutor implements AsyncTaskExecutor
{

    @Override
    public <T> void execute(AsyncTask<T, ?, ?> task, T... args)
    {
        task.execute(args);
    }
}

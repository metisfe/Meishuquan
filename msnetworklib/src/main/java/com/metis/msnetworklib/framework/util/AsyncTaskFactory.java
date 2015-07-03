package com.metis.msnetworklib.framework.util;

import android.os.Build;

/**
 * Created by wudi on 3/15/2015.
 */
public class AsyncTaskFactory extends PlatformSupportFactory<AsyncTaskExecutor>
{
    public AsyncTaskFactory()
    {
        super(AsyncTaskExecutor.class, new DefaultAsyncTaskExecutor());
        this.addImplementationClass(Build.VERSION_CODES.HONEYCOMB, "com.microsoft.scp.bingscore.system.HoneycombAsyncTaskExecutor");
    }
}
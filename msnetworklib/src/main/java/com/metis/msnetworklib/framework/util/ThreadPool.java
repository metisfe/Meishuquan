package com.metis.msnetworklib.framework.util;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by wudi on 3/15/2015.
 */
public class ThreadPool
{
    public static ThreadPool APIThread = new ThreadPool(6, Thread.NORM_PRIORITY + 3);

    private ExecutorService executor;

    private ThreadPool(int number, int prority)
    {
        final int p = prority;
        this.executor = Executors.newFixedThreadPool(number, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setPriority(p);
                return t;
            }
        });
    }

    public void run(Runnable runnable)
    {
        this.executor.execute(runnable);
    }

    /*public static Executor getUIExcutor() {
        return new Executor() {
            @Override
            public void execute(Runnable command) {
                MainApplication.Handler.post(command);
            }
        };
    }*/

    public static Executor getExecutor() {
        return APIThread.executor;
    }

}

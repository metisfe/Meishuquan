package com.metis.msnetworklib.framework.runner;

import android.text.TextUtils;

import com.metis.msnetworklib.framework.util.AsyncTaskFactory;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by wudi on 3/15/2015.
 */
public class RunnerManager
{
    private static final int MAX_NORMAL_PRIORITY_LOADING_RUNNER = 4;
    private static final int MAX_HIGH_PRIORITY_LOADING_RUNNER = 4;

    private static RunnerManager instance;

    private Stack<WebRunner<?>> blockedRunners;
    private List<WebRunner<?>> loadingRunners;

    private RunnerManager()
    {
        this.blockedRunners = new Stack<WebRunner<?>>();
        this.loadingRunners = new ArrayList<WebRunner<?>>();
    }

    public static RunnerManager getInstance()
    {
        if (instance == null)
        {
            instance = new RunnerManager();
        }
        return instance;
    }

    public void run(WebRunner<?> runner)
    {
        run(runner, false);
    }

    public void run(WebRunner<?> runner, boolean isHighPriority)
    {
        Assert.assertNotNull(runner);

        final WebRunner<?> r = runner;
        runner.setRunnerListener(new RunnerListener()
        {

            @Override
            public void onCompleted()
            {
                if (instance == null)
                {
                    return;
                }

                if (loadingRunners != null)
                {
                    loadingRunners.remove(r);
                }

                notifyRunner();
            }

        });

        removeBlockedRunner(r);

        boolean isStartLoading = r.getPriority() <= Thread.NORM_PRIORITY ? this.getNormalPriorityLoadingSize() < MAX_NORMAL_PRIORITY_LOADING_RUNNER : this
                .getHighPriorityLoadingSize() < MAX_HIGH_PRIORITY_LOADING_RUNNER;

        if (isStartLoading)
        {
            AsyncTaskFactory factory = new AsyncTaskFactory();
            factory.build().execute(r);
            synchronized (this.loadingRunners)
            {
                this.loadingRunners.add(r);
            }
        }
        else
        {
            synchronized (this.blockedRunners)
            {
                this.blockedRunners.add(r);
            }
        }

        notifyRunner();
    }

    public RunnerProgress getProgress(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            return null;
        }

        synchronized (this.loadingRunners)
        {
            for (WebRunner<?> runner : this.loadingRunners)
            {
                if (url.equals(runner.getUrl()))
                {
                    return runner.getProgress();
                }
            }
        }

        return null;
    }

    private WebRunner<?> getHighPriorityRunner()
    {
        if (getHighPriorityBlockedSize() == 0)
        {
            return null;
        }
        int size = this.blockedRunners.size();
        for (int i = size - 1; i >= 0; i--)
        {
            WebRunner<?> runner = this.blockedRunners.get(i);
            if (runner.getPriority() > Thread.NORM_PRIORITY && runner.isShouldRunNow())
            {
                return runner;
            }
        }
        for (int i = size - 1; i >= 0; i--)
        {
            WebRunner<?> runner = this.blockedRunners.get(i);
            if (runner.getPriority() > Thread.NORM_PRIORITY)
            {
                return runner;
            }
        }
        return null;
    }

    private WebRunner<?> getNowPriorityRunner()
    {
        if (getNormalPriorityBlockedSize() == 0)
        {
            return null;
        }
        int size = this.blockedRunners.size();
        for (int i = size - 1; i >= 0; i--)
        {
            WebRunner<?> runner = this.blockedRunners.get(i);
            if (runner.getPriority() <= Thread.NORM_PRIORITY && runner.isShouldRunNow())
            {
                return runner;
            }
        }
        for (int i = size - 1; i >= 0; i--)
        {
            WebRunner<?> runner = this.blockedRunners.get(i);
            if (runner.getPriority() <= Thread.NORM_PRIORITY)
            {
                return runner;
            }
        }
        return null;
    }

    private WebRunner<?> getShouldRemoveRunner()
    {
        if (this.blockedRunners == null || this.blockedRunners.size() == 0)
        {
            return null;
        }
        int size = this.blockedRunners.size();
        for (int i = size - 1; i >= 0; i--)
        {
            WebRunner<?> runner = this.blockedRunners.get(i);
            if (runner.isNeedRemove())
            {
                return runner;
            }
        }
        return null;
    }

    // call this method when network is up from down
    public void notifyRunner()
    {
        if (instance == null)
        {
            return;
        }

        synchronized (this.blockedRunners)
        {
            WebRunner<?> removeRunner = getShouldRemoveRunner();
            while (removeRunner != null)
            {
                this.blockedRunners.remove(removeRunner);
                removeRunner = getShouldRemoveRunner();
            }
        }

        synchronized (this.blockedRunners)
        {
            WebRunner<?> highRunner = getHighPriorityRunner();
            while (highRunner != null && this.getHighPriorityLoadingSize() < MAX_HIGH_PRIORITY_LOADING_RUNNER)
            {
                AsyncTaskFactory factory = new AsyncTaskFactory();
                factory.build().execute(highRunner);
                this.blockedRunners.remove(highRunner);
                synchronized (this.loadingRunners)
                {
                    this.loadingRunners.add(highRunner);
                }
                highRunner = getHighPriorityRunner();
            }
        }

        synchronized (this.blockedRunners)
        {
            WebRunner<?> normalRunner = getNowPriorityRunner();
            while (normalRunner != null && this.getNormalPriorityLoadingSize() < MAX_NORMAL_PRIORITY_LOADING_RUNNER)
            {
                AsyncTaskFactory factory = new AsyncTaskFactory();
                factory.build().execute(normalRunner);
                this.blockedRunners.remove(normalRunner);
                synchronized (this.loadingRunners)
                {
                    this.loadingRunners.add(normalRunner);
                }
                normalRunner = getNowPriorityRunner();
            }
        }

    }

    public void onDestroy()
    {
        synchronized (this.blockedRunners)
        {
            this.blockedRunners.clear();
        }

        synchronized (this.loadingRunners)
        {
            this.loadingRunners.clear();
        }

        instance = null;
    }

    private int getNormalPriorityBlockedSize()
    {
        synchronized (this.blockedRunners)
        {
            int size = 0;
            for (WebRunner<?> runner : this.blockedRunners)
            {
                if (runner.getPriority() <= Thread.NORM_PRIORITY)
                {
                    size++;
                }
            }
            return size;
        }
    }

    private int getHighPriorityBlockedSize()
    {
        synchronized (this.blockedRunners)
        {
            int size = 0;
            for (WebRunner<?> runner : this.blockedRunners)
            {
                if (runner.getPriority() > Thread.NORM_PRIORITY)
                {
                    size++;
                }
            }
            return size;
        }
    }

    private int getNormalPriorityLoadingSize()
    {
        synchronized (this.loadingRunners)
        {
            int size = 0;
            for (WebRunner<?> runner : this.loadingRunners)
            {
                if (runner.getPriority() <= Thread.NORM_PRIORITY)
                {
                    size++;
                }
            }
            return size;
        }
    }

    private int getHighPriorityLoadingSize()
    {
        synchronized (this.loadingRunners)
        {
            int size = 0;
            for (WebRunner<?> runner : this.loadingRunners)
            {
                if (runner.getPriority() > Thread.NORM_PRIORITY)
                {
                    size++;
                }
            }
            return size;
        }
    }

    public void removeBlockedRunner(WebRunner<?> newRunner)
    {
        synchronized (this.blockedRunners)
        {
            WebRunner<?> removeRunner = null;
            for (WebRunner<?> runner : this.blockedRunners)
            {
                if (runner.isBlockedRunnerNeedRemove(newRunner))
                {
                    removeRunner = runner;
                    break;
                }
            }

            if (removeRunner != null)
            {
                this.blockedRunners.remove(removeRunner);
            }
        }
    }
}
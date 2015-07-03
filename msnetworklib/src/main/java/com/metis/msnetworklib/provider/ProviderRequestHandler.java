package com.metis.msnetworklib.provider;

/**
 * Created by wudi on 3/15/2015.
 */

public abstract class ProviderRequestHandler<T>
{
    public T Result;
    public Object MetaData;
    public ProviderPreference Preference;
    public ProviderRequestStatus RequestStatus;
    public String ErrorMessage;
    private boolean cancelled = false;
    private Object synchronizeObject = new Object();

    public void cancel()
    {
        synchronized(synchronizeObject)
        {
            this.cancelled = true;
        }
    }

    public boolean isCancelled()
    {
        synchronized(synchronizeObject)
        {
            return this.cancelled;
        }
    }

    public void loadComplete()
    {
        if (this.isCancelled())
        {
            return;
        }

        this.onLoadCompleted();
    }

    public boolean isSucceeded()
    {
        return this.RequestStatus.ordinal() <= ProviderRequestStatus.Load_Succeeded.ordinal();
    }

    public void loadComplete(T result, ProviderRequestStatus requestStatus)
    {
        this.Result = result;
        this.RequestStatus = requestStatus;

        this.loadComplete();
    }

    protected abstract void onLoadCompleted();
}
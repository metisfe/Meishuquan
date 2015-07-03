package com.metis.msnetworklib.framework.runner;

/**
 * Created by wudi on 3/15/2015.
 */
public class AsyncResult<T> {
    private T result;
    private int statusCode;
    private Exception exception;
    private Object tag;

    public AsyncResult()
    {
    }

    public AsyncResult(T result)
    {
        this.result = result;
    }

    public T getResult()
    {
        return result;
    }

    public Object getTag()
    {
        return this.tag;
    }

    public void setTag(Object tag)
    {
        this.tag = tag;
    }

    public void setResult(T result)
    {
        this.result = result;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public Exception getException()
    {
        return exception;
    }

    public void setException(Exception exception)
    {
        this.exception = exception;
    }

}

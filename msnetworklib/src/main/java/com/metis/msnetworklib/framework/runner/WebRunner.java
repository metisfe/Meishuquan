package com.metis.msnetworklib.framework.runner;

import android.util.Log;

import com.metis.msnetworklib.framework.network.HttpManager;
import com.metis.msnetworklib.framework.network.HttpMethod;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created by wudi on 3/15/2015.
 */
public class WebRunner<T> extends RunnerBase<Void, Integer, AsyncResult<T>>
{
    private String url;
    private HttpMethod method;
    private boolean isNeedCache;
    private boolean isNeedLoadFromServer;
    protected int priority;
    private RunnerProgress progress = new RunnerProgress();
    private WebRunnerListener<T> webRunnerListener;

    public WebRunner()
    {

    }

    public WebRunner(String url)
    {
        this(url, HttpMethod.GET);
    }

    public WebRunner(String url, HttpMethod method)
    {
        this(url, method, false);
    }

    public WebRunner(String url, HttpMethod method, boolean isNeedCache)
    {
        this(url, method, isNeedCache, true);
    }

    public WebRunner(String url, HttpMethod method, boolean isNeedCache, boolean isNeedLoadFromServer)
    {
        this(url, method, isNeedCache, isNeedLoadFromServer, Thread.NORM_PRIORITY);
    }

    public WebRunner(String url, HttpMethod method, boolean isNeedCache, boolean isNeedLoadFromServer, int priority)
    {
        this.url = url;
        this.method = method;
        this.isNeedCache = isNeedCache;
        this.isNeedLoadFromServer = isNeedLoadFromServer;
        this.priority = priority;
    }

    public RunnerProgress getProgress()
    {
        return progress;
    }

    public void setProgress(RunnerProgress progress)
    {
        this.progress = progress;
    }

    public String getUrl()
    {
        return url;
    }

    public boolean isNeedCache()
    {
        return isNeedCache;
    }

    public int getPriority()
    {
        return priority;
    }

    public WebRunnerListener<T> getWebRunnerListener()
    {
        return webRunnerListener;
    }

    public void setWebRunnerListener(WebRunnerListener<T> webRunnerListener)
    {
        this.webRunnerListener = webRunnerListener;
    }

    @Override
    protected AsyncResult<T> doInBackground(Void... params)
    {
        AsyncResult<T> result = new AsyncResult<T>();

        if (this.webRunnerListener != null)
        {
            T obj = this.webRunnerListener.getObjectFromCache(this.url);
            if (obj != null)
            {
                result.setResult(obj);
                return result;
            }
        }

        if (!this.isNeedLoadFromServer)
        {
            return result;
        }

        HttpResponse httpResponse = null;
        try
        {
            if (this.method == HttpMethod.GET)
            {
                httpResponse = HttpManager.getHttpResponse(this.url);
            }
            else if (this.method == HttpMethod.POST)
            {
                httpResponse = HttpManager.postHttpResponse(this.url);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("WebRunner", "failed to access url : " + this.url);
            result.setException(e);
            return result;
        }
        StatusLine status = httpResponse.getStatusLine();
        int statusCode = status.getStatusCode();

        result.setStatusCode(statusCode);
        if (statusCode != 200)
        {
            Log.e("WebRunner", "failed to access url : " + this.url + "; response statusCode= " + statusCode);
            return result;
        }

        Header[] clHeaders = httpResponse.getHeaders("Content-Length");
        if (clHeaders != null && clHeaders.length > 0)
        {
            Header header = clHeaders[0];
            progress.totalSize = Long.parseLong(header.getValue());
        }

        HttpEntity entity = httpResponse.getEntity();
        if (entity == null)
        {
            Log.e("WebRunner", "loaded url : " + this.url + "; Entity is null");
            return result;
        }

        BufferedInputStream bis = null;
        try
        {
            bis = new BufferedInputStream(entity.getContent());

            if (this.webRunnerListener != null)
            {
                T obj = this.webRunnerListener.onHandleData(bis, progress);
                result.setResult(obj);
            }

            return result;
        }
        catch (IOException e)
        {
            result.setException(e);
        }
        finally
        {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                }
            }
            try
            {
                entity.consumeContent();
            }
            catch (IOException e)
            {
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(AsyncResult<T> result)
    {
        super.onPostExecute(result);

        if (this.webRunnerListener != null)
        {
            this.webRunnerListener.onCompleted(result);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values)
    {
    }

    public boolean isBlockedRunnerNeedRemove(WebRunner<?> blockedRunner)
    {
        return this == blockedRunner || this.getUrl().equals(blockedRunner.getUrl());
    }

    public boolean isNeedRemove()
    {
        return false;
    }

    public boolean isShouldRunNow()
    {
        return true;
    }
}
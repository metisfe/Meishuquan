package com.metis.msnetworklib.framework.runner;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by wudi on 3/15/2015.
 */
public class RunnerBase<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
{
    private RunnerListener runnerListener;

    public RunnerListener getRunnerListener()
    {
        return runnerListener;
    }

    public void setRunnerListener(RunnerListener runnerListener)
    {
        this.runnerListener = runnerListener;
    }

    @Override
    protected Result doInBackground(Params... params)
    {
        return null;
    }

    @Override
    protected void onPostExecute(Result result)
    {
        super.onPostExecute(result);

        if (this.isCancelled())
        {
            Log.d("TaskRunnerBase", "onPostExecute cancelled");
            return;
        }

        if (this.runnerListener != null)
        {
            this.runnerListener.onCompleted();
        }
    }

    @Override
    protected void onProgressUpdate(Progress... values)
    {
        super.onProgressUpdate(values);

        if (this.isCancelled())
        {
            return;
        }
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected void onCancelled()
    {

        Log.d("TaskRunnerBase", "onCancelled() cancelled");

        if (this.runnerListener != null)
        {
            this.runnerListener.onCompleted();
        }
    }
}
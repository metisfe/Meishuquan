package com.metis.msnetworklib.framework.runner;

/**
 * Created by wudi on 3/15/2015.
 */
public class ModelWebRunner<T> implements Runnable
{
    public ModelWebRunner()
    {
    }

    protected AsyncResult<T> doInBackground(Void... params)
    {
        return null;
    }

    protected void onPostExecute(AsyncResult<T> result)
    {
    }

    @Override
    public void run()
    {
        final AsyncResult<T> result = doInBackground();
        /*MainApplication.Handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                onPostExecute(result);
            }
        });*/
    }
}
package com.metis.base.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.metis.base.manager.DownloadManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DownloadService extends Service {

    private static final int TIME_OUT = 30 * 1000;

    private Map<String, HttpHandler<File>> mIdHandlerMap = new HashMap<String, HttpHandler<File>>();

    private LocalBroadcastManager mBroadcastManager = null;

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra(DownloadManager.KEY_ACTION, 0);
        switch (action) {
            case DownloadManager.ACTION_START:
                startTask(intent);
                break;
            case DownloadManager.ACTION_CANCEL:
                stopTask(intent);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void stopTask (Intent it) {
        final String id = it.getStringExtra(DownloadManager.KEY_ID);
        final String url = it.getStringExtra(DownloadManager.KEY_TARGET_URL);
        final String path = it.getStringExtra(DownloadManager.KEY_TARGET_PATH);
        final String name = it.getStringExtra(DownloadManager.KEY_NAME);
        final HttpUtils utils = new HttpUtils(TIME_OUT);
        final Intent stateIt = new Intent(DownloadManager.ACTION_DOWNLOAD_STATE_CHANGE);
        stateIt.putExtra(DownloadManager.KEY_ID, id);
        stateIt.putExtra(DownloadManager.KEY_TARGET_URL, url);
        stateIt.putExtra(DownloadManager.KEY_TARGET_PATH, path);
        stateIt.putExtra(DownloadManager.KEY_NAME, name);
        stateIt.putExtra(DownloadManager.KEY_STATE, DownloadManager.STATE_IDLE);
        if (mIdHandlerMap.containsKey(id)) {
            HttpHandler<File> httpHandler = mIdHandlerMap.get(id);
            if (httpHandler != null) {
                httpHandler.cancel(true);
                stateIt.putExtra(DownloadManager.KEY_STATE, DownloadManager.ACTION_CANCEL);
                mBroadcastManager.sendBroadcast(stateIt);
            }
        }
    }

    private void startTask (Intent it) {

        final String id = it.getStringExtra(DownloadManager.KEY_ID);
        final String url = it.getStringExtra(DownloadManager.KEY_TARGET_URL);
        final String path = it.getStringExtra(DownloadManager.KEY_TARGET_PATH);
        final String name = it.getStringExtra(DownloadManager.KEY_NAME);
        final HttpUtils utils = new HttpUtils(TIME_OUT);
        final Intent stateIt = new Intent(DownloadManager.ACTION_DOWNLOAD_STATE_CHANGE);
        stateIt.putExtra(DownloadManager.KEY_ID, id);
        stateIt.putExtra(DownloadManager.KEY_TARGET_URL, url);
        stateIt.putExtra(DownloadManager.KEY_TARGET_PATH, path);
        stateIt.putExtra(DownloadManager.KEY_NAME, name);
        stateIt.putExtra(DownloadManager.KEY_STATE, DownloadManager.STATE_IDLE);

        final Intent progressIt = new Intent(DownloadManager.ACTION_DOWNLOAD_PROGRESS_CHANGE);
        progressIt.putExtra(DownloadManager.KEY_ID, id);
        progressIt.putExtra(DownloadManager.KEY_TARGET_URL, url);
        progressIt.putExtra(DownloadManager.KEY_TARGET_PATH, path);
        progressIt.putExtra(DownloadManager.KEY_NAME, name);
        progressIt.putExtra(DownloadManager.KEY_STATE, DownloadManager.STATE_RUNNING);

        stateIt.putExtra(DownloadManager.KEY_STATE, DownloadManager.STATE_RUNNING);
        mBroadcastManager.sendBroadcast(stateIt);

        Toast.makeText(this, "startTask " + path, Toast.LENGTH_SHORT).show();

        HttpHandler<File> handler = utils.download(url, path, true, true, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                stateIt.putExtra(DownloadManager.KEY_STATE, DownloadManager.STATE_SUCCESS);
                mBroadcastManager.sendBroadcast(stateIt);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);

                progressIt.putExtra(DownloadManager.KEY_CURRENT, current);
                progressIt.putExtra(DownloadManager.KEY_TOTAL, total);
                mBroadcastManager.sendBroadcast(progressIt);
            }

            @Override
            public void onCancelled() {
                super.onCancelled();

                stateIt.putExtra(DownloadManager.KEY_STATE, DownloadManager.STATE_CANCELED);
                mBroadcastManager.sendBroadcast(stateIt);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stateIt.putExtra(DownloadManager.KEY_STATE, DownloadManager.STATE_FAILED);
                mBroadcastManager.sendBroadcast(stateIt);
            }
        });
        mIdHandlerMap.put(id, handler);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }
}

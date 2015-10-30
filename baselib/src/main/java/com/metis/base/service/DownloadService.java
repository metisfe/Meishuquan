package com.metis.base.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bokecc.sdk.mobile.download.DownloadListener;
import com.bokecc.sdk.mobile.download.Downloader;
import com.bokecc.sdk.mobile.exception.DreamwinException;
import com.metis.base.module.DownloadTaskImpl;
import com.metis.base.utils.Log;

import java.io.File;

/**
 * Created by Beak on 2015/10/22.
 */
public class DownloadService extends Service implements DownloadListener{

    private static final String TAG = DownloadService.class.getSimpleName();

    // 配置API KEY
    public final static String API_KEY = "iKardUvkyz2uSNkXoNo6l4pGJKPmIER8";

    // 配置帐户ID
    public final static String USERID = "6E7D1C1E1C09DB4D";

    private DownloadBinder binder = new DownloadBinder();

    private OnDownloadListener mDownloadListener = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void handleProcess(long l, long l1, String s) {
        Log.v(TAG, "download execute handleProcess l=" + l + " l1=" + l1 + " s=" + s);
        if (mDownloadListener != null) {
            int percent = (int)(l * 100 / l1);
            mDownloadListener.onProgressUpdate(s, l, l1, percent);
        }
    }

    @Override
    public void handleException(DreamwinException e, int i) {
        Log.v(TAG, "download execute handleException e=" + e.getMessage() + " i=" + i);
        if (mDownloadListener != null) {
            mDownloadListener.onException(e, i);
        }
    }

    @Override
    public void handleStatus(String s, int i) {
        Log.v(TAG, "download execute handleStatus i=" + i + " s=" + s);
        if (mDownloadListener != null) {
            mDownloadListener.onStateChanged(s, i);
        }
    }

    @Override
    public void handleCancel(String s) {
        Log.v(TAG, "download execute handleCancel s=" + s);
        if (mDownloadListener != null) {
            mDownloadListener.onCancel(s);
        }
    }

    public class DownloadBinder extends Binder {
        public DownloadService getService () {
            return DownloadService.this;
        }
    }

    private Downloader mDownloader = null;
    private DownloadTaskImpl mCurrentTask = null;

    public void download (DownloadTaskImpl task) {
        mCurrentTask = task;
        download(task.getId(), new File(task.getTargetPath()));
    }

    public void pause (String id) {
        if (mCurrentTask == null) {
            return;
        }
        if (!mCurrentTask.getId().equals(id)) {
            return;
        }
        mDownloader.pause();
    }

    private void download (String videoId, File targetFile) {
        if (mDownloader == null) {
            mDownloader = new Downloader(targetFile, videoId, USERID, API_KEY);
        }
        mDownloader.setDownloadListener(this);
        mDownloader.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDownloader.setDownloadListener(null);
        mDownloadListener = null;
    }

    public void setOnDownloadListener (OnDownloadListener listener) {
        mDownloadListener = listener;
    }

    public interface OnDownloadListener {
        public void onProgressUpdate (String id, long current, long total, int percent);
        public void onStateChanged (String id, int state);
        public void onCancel (String id);
        public void onException (DreamwinException e, int i);
    }
}

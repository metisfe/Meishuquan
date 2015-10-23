package com.metis.base.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.bokecc.sdk.mobile.download.DownloadListener;
import com.bokecc.sdk.mobile.download.Downloader;
import com.bokecc.sdk.mobile.download.OnProcessDefinitionListener;
import com.bokecc.sdk.mobile.exception.DreamwinException;
import com.metis.base.manager.DownloadManager;
import com.metis.base.module.DownloadTaskImpl;
import com.metis.base.utils.Log;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Beak on 2015/10/22.
 */
public class DownloadService extends Service {

    private static final String TAG = DownloadManager.class.getSimpleName();

    private DownloadBinder binder = new DownloadBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class DownloadBinder extends Binder {
        public DownloadService getService () {
            return DownloadService.this;
        }
    }

    private Downloader mDownloader = null;
    private DownloadListener mDownloadListener = null;

    public void download (DownloadTaskImpl task, String targetPath) {
        Toast.makeText(this, "download " + task.getName(), Toast.LENGTH_SHORT).show();
        if (mDownloader == null) {
            mDownloader = new Downloader(new File(targetPath), task.getId(), DownloadManager.USERID, DownloadManager.API_KEY);
            mDownloader.setDownloadListener(mDownloadListener);
            mDownloader.setOnProcessDefinitionListener(new OnProcessDefinitionListener() {
                @Override
                public void onProcessDefinition(HashMap<Integer, String> hashMap) {
                    Log.v(TAG, TAG + " onProcessDefinition ");
                }

                @Override
                public void onProcessException(DreamwinException e) {
                    Log.v(TAG, TAG + " onProcessException " + e.getMessage());
                }
            });
            mDownloader.start();
        }
        /*if (mDownloader == null) {
            mDownloader = new Downloader();
        }
        mDownloader*/
    }

    public void setDownloadListener(DownloadListener mDownloadListener) {
        this.mDownloadListener = mDownloadListener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDownloadListener = null;
    }
}

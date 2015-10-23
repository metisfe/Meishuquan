package com.metis.base.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.util.ArrayMap;

import com.bokecc.sdk.mobile.download.DownloadListener;
import com.bokecc.sdk.mobile.download.Downloader;
import com.bokecc.sdk.mobile.exception.DreamwinException;
import com.metis.base.module.DownloadTaskImpl;
import com.metis.base.service.DownloadService;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/10/22.
 */
public class DownloadManager extends AbsManager implements DownloadListener {

    // 配置API KEY
    public final static String API_KEY = "iKardUvkyz2uSNkXoNo6l4pGJKPmIER8";

    // 配置帐户ID
    public final static String USERID = "6E7D1C1E1C09DB4D";

    //private List<DownloadTaskImpl> mDownloadTaskPool = new ArrayList<DownloadTaskImpl>();

    private ArrayMap<String, DownloadTaskImpl> mDownloadTaskPool = new ArrayMap<>();

    private List<OnDownloadListener> mDownloadListenerList = new ArrayList<OnDownloadListener>();

    private static DownloadManager sManager = null;

    public static synchronized DownloadManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new DownloadManager(context.getApplicationContext());
        }
        return sManager;
    }

    private DownloadService mService = null;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder)service;
            mService = binder.getService();
            DownloadTaskImpl task = getNextWaitingTask();
            if (task != null) {
                mService.download(task, CacheManager.getInstance(getContext()).getMyVideoCacheDir().getAbsolutePath() + File.separator + task.getId());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    private DownloadManager(Context context) {
        super(context);
    }

    public ArrayMap<String, DownloadTaskImpl> getDownloadingList () {
        return mDownloadTaskPool;
    }

    public void putTask (DownloadTaskImpl task) {
        if (mDownloadTaskPool.containsKey(task)) {
            return;
        }
        mDownloadTaskPool.put(task.getId(), task);
        /*if (mService == null) {
            getContext().bindService(new Intent(getContext(), DownloadService.class), mConnection, Context.BIND_AUTO_CREATE);
        } else {
            mService.download(task, task.getTargetPath());
        }*/
        if (!mDownloadListenerList.isEmpty()) {
            final int length = mDownloadListenerList.size();
            for (int i = 0; i < length; i++) {
                mDownloadListenerList.get(i).onNewTaskAdded(task);
            }
        }
    }

    public DownloadTaskImpl getTask (String taskId) {
        if (mDownloadTaskPool.isEmpty()) {
            return null;
        }
        final int length = mDownloadTaskPool.size();
        for (int i = 0; i < length; i++) {
            DownloadTaskImpl task = mDownloadTaskPool.get(i);
            if (task.getId().equals(taskId)) {
                return task;
            }
        }
        return null;
    }

    public void removeTask (DownloadTaskImpl task) {
        if (mDownloadTaskPool.containsKey(task.getId())) {
            mDownloadTaskPool.remove(task);
        }
    }

    public void removeTask (String taskId) {
        removeTask(getTask(taskId));
    }

    public DownloadTaskImpl getNextWaitingTask () {
        if (mDownloadTaskPool.isEmpty()) {
            return null;
        }
        final int length = mDownloadTaskPool.size();
        for (int i = 0; i < length; i++) {
            DownloadTaskImpl task = mDownloadTaskPool.valueAt(i);
            if (task.getState() == Downloader.WAIT) {
                return task;
            }
        }
        return null;
    }

    public void registerOnDownloadListener (OnDownloadListener listener) {
        if (mDownloadListenerList.contains(listener)) {
            return;
        }
        mDownloadListenerList.add(listener);
    }

    public void unregisterOnDownloadListener (OnDownloadListener listener) {
        if (mDownloadListenerList.contains(listener)) {
            mDownloadListenerList.remove(listener);
        }
    }

    @Override
    public void handleProcess(long l, long l1, String s) {

        if (mDownloadListenerList.isEmpty()) {
            return;
        }
        DownloadTaskImpl task = getTask(s);
        final int length = mDownloadListenerList.size();
        for (int i = 0; i < length; i++) {
            mDownloadListenerList.get(i).onTaskProgressChanged(task);
        }
    }

    @Override
    public void handleException(DreamwinException e, int i) {

    }

    @Override
    public void handleStatus(String s, int i) {

    }

    @Override
    public void handleCancel(String s) {

    }

    public static interface OnDownloadListener {
        public void onNewTaskAdded (DownloadTaskImpl task);
        public void onTaskStateChanged (DownloadTaskImpl task);
        public void onTaskProgressChanged (DownloadTaskImpl task);
    }

}

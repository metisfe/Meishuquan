package com.metis.base.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.widget.Toast;

import com.bokecc.sdk.mobile.download.Downloader;
import com.bokecc.sdk.mobile.exception.DreamwinException;
import com.metis.base.module.DownloadTaskImpl;
import com.metis.base.service.DownloadService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/10/29.
 */
public class DownloadManager extends AbsManager implements DownloadService.OnDownloadListener {

    private static final int WHAT_PROGRESS = 1, WHAT_STATUS = 2, WHAT_EXCEPTION = 3, WHAT_CANCEL = 4;
    private static final String KEY_ID = "id", KEY_CURRENT = "current", KEY_TOTAL = "total", KEY_PERCENT = "percent", KEY_SPEED = "speed";

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
            if (service instanceof DownloadService.DownloadBinder) {
                mService = ((DownloadService.DownloadBinder) service).getService();
                mService.setOnDownloadListener(DownloadManager.this);
                Toast.makeText(getContext(), "serviceConnected", Toast.LENGTH_SHORT).show();
                startTask(getWaitingTask());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private ArrayMap<String, DownloadTaskImpl> mTaskQueue = new ArrayMap<>();

    private List<OnDownloadCallback> mDownloadCallbackList = new ArrayList<>();
    private List<OnTaskQueueChangeCallback> mQueueCallbackList = new ArrayList<>();

    private static class IncomingHandler extends Handler {
        private final WeakReference<DownloadManager> mService;

        IncomingHandler(DownloadManager service) {
            mService = new WeakReference<DownloadManager>(service);
        }
        @Override
        public void handleMessage(Message msg)
        {
            DownloadManager service = mService.get();
            if (service != null) {
                service.handleMessage(msg);
            }
        }
    }

    private IncomingHandler mHandler = null;

    private long mLastUpdateTime = 0;
    private long mLastCurrent = 0;
    private Bundle mUpdateBundle = new Bundle();

    private DownloadManager(Context context) {
        super(context);
        mHandler = new IncomingHandler(this);
    }

    public boolean isInDownloadingQueue (String id) {
        return mTaskQueue.containsKey(id);
    }

    public void addTask (DownloadTaskImpl task) {
        if (isInDownloadingQueue(task.getId())) {
            return;
        }
        mTaskQueue.put(task.getId(), task);
        if (!mQueueCallbackList.isEmpty()) {
            final int length = mQueueCallbackList.size();
            for (int i = 0; i < length; i++) {
                mQueueCallbackList.get(i).onTaskAdd(task);
            }
        }
        if (hasWaitingTask()) {
            if (mService == null) {
                getContext().bindService(new Intent(getContext(), DownloadService.class), mConnection, Context.BIND_AUTO_CREATE);
            } else {
                startTask(getWaitingTask());
                //mService.download();
            }
        }
    }

    public DownloadTaskImpl removeTask (String id) {
        DownloadTaskImpl task = mTaskQueue.remove(id);
        performRemoveCallback(task);
        return task;
    }

    public void pauseTask (DownloadTaskImpl task) {

    }

    public void pauseTask (String id) {
        if (mService != null) {
            mService.pause(id);
        }
    }

    public void removeTask (DownloadTaskImpl task) {
        mTaskQueue.remove(task.getId());
        performRemoveCallback(task);
    }

    private void performRemoveCallback (DownloadTaskImpl task) {
        final int length = mQueueCallbackList.size();
        for (int i = 0; i < length; i++) {
            mQueueCallbackList.get(i).onTaskRemove(task);
        }
    }

    private void startTask (DownloadTaskImpl task) {
        if (task == null) {
            return;
        }
        mService.download(task);
    }

    public boolean hasWaitingTask () {
        final int size = mTaskQueue.size();
        if (size == 0) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            DownloadTaskImpl task = mTaskQueue.valueAt(i);
            if (task.getState() == Downloader.WAIT) {
                return true;
            }
        }
        return false;
    }

    public DownloadTaskImpl getWaitingTask () {
        final int length = mTaskQueue.size();
        if (length == 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            DownloadTaskImpl task = mTaskQueue.valueAt(i);
            if (task.getState() == Downloader.WAIT) {
                return task;
            }
        }
        return null;
    }

    public DownloadTaskImpl getTaskById (String id) {
        if (mTaskQueue.containsKey(id)) {
            return mTaskQueue.get(id);
        }
        return null;
    }

    private void handleMessage (Message msg) {
        String id = null;
        switch (msg.what) {
            case WHAT_PROGRESS:
                Bundle bundle = msg.getData();
                id = bundle.getString(KEY_ID);
                long current = bundle.getLong(KEY_CURRENT);
                long total = bundle.getLong(KEY_TOTAL);
                int percent = bundle.getInt(KEY_PERCENT);
                int speed = bundle.getInt(KEY_SPEED);
                DownloadTaskImpl task = getTaskById(id);
                if (task != null) {
                    task.setPosition(current);
                    task.setLength(total);
                    task.setProgress(percent);
                    task.setSpeed(speed);
                }
                if (!mDownloadCallbackList.isEmpty()) {
                    final int length = mDownloadCallbackList.size();
                    for (int i = 0; i < length; i++) {
                        mDownloadCallbackList.get(i).onTaskProgress(id, current, total, percent);
                    }
                }
                break;
            case WHAT_STATUS:
                int state = msg.arg1;
                id = (String)msg.obj;
                switch (state) {
                    case Downloader.FINISH:
                        removeTask(id);
                        break;
                }
                if (!mDownloadCallbackList.isEmpty()) {
                    final int length = mDownloadCallbackList.size();
                    for (int i = 0; i < length; i++) {
                        OnDownloadCallback callback = mDownloadCallbackList.get(i);
                        if (state == Downloader.DOWNLOAD) {
                            callback.onTaskStart(id);
                        } else if (state == Downloader.FINISH) {
                            callback.onTaskFinish(id);
                        } else if (state == Downloader.PAUSE) {
                            callback.onTaskPause(id);
                        } else if (state == Downloader.PAUSING) {
                            callback.onTaskPausing(id);
                        } else if (state == Downloader.WAIT) {
                            callback.onTaskWait(id);
                        }
                    }
                }
                break;
            case WHAT_CANCEL:
                break;
            case WHAT_EXCEPTION:
                break;
        }
    }

    @Override
    public void onProgressUpdate(String id, long current, long total, int percent) {
        long now = System.currentTimeMillis();
        long delay = now - mLastUpdateTime;
        //limit the update duration
        if (delay > 1000) {
            Message msg = mHandler.obtainMessage();
            msg.what = WHAT_PROGRESS;
            mUpdateBundle.putString(KEY_ID, id);
            mUpdateBundle.putLong(KEY_CURRENT, current);
            mUpdateBundle.putLong(KEY_TOTAL, total);
            mUpdateBundle.putInt(KEY_PERCENT, percent);
            int speed = (int)((current - mLastCurrent) / delay * 1000);
            mUpdateBundle.putInt(KEY_SPEED, speed);
            msg.setData(mUpdateBundle);
            mHandler.sendMessage(msg);
            mLastUpdateTime = now;
            mLastCurrent = current;
        }

    }

    @Override
    public void onStateChanged(String id, int state) {
        Message msg = mHandler.obtainMessage();
        msg.what = WHAT_STATUS;
        msg.arg1 = state;
        msg.obj = id;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onCancel(String id) {
        if (!mDownloadCallbackList.isEmpty()) {
            final int length = mDownloadCallbackList.size();
            for (int i = 0; i < length; i++) {
                mDownloadCallbackList.get(i).onTaskCancel(id);
            }
        }
    }

    @Override
    public void onException(DreamwinException e, int a) {
        if (!mDownloadCallbackList.isEmpty()) {
            final int length = mDownloadCallbackList.size();
            for (int i = 0; i < length; i++) {
                mDownloadCallbackList.get(i).onTaskFailed();
            }
        }
    }

    public void registerOnDownloadCallback (OnDownloadCallback callback) {
        if (callback == null) {
            return;
        }
        if (mDownloadCallbackList.contains(callback)) {
            return;
        }
        mDownloadCallbackList.add(callback);
    }

    public void unregisterOnDownloadCallback (OnDownloadCallback callback) {
        if (callback == null) {
            return;
        }
        if (!mDownloadCallbackList.contains(callback)) {
            return;
        }
        mDownloadCallbackList.remove(callback);
    }

    public void registerOnTaskQueueChangeCallback (OnTaskQueueChangeCallback callback) {
        if (callback == null) {
            return;
        }
        if (mQueueCallbackList.contains(callback)) {
            return;
        }
        mQueueCallbackList.add(callback);
    }

    public void unregisterOnTaskQueueChangeCallback (OnTaskQueueChangeCallback callback) {
        if (callback == null) {
            return;
        }
        if (!mQueueCallbackList.contains(callback)) {
            return;
        }
        mQueueCallbackList.remove(callback);
    }

    public interface OnTaskQueueChangeCallback {
        public void onTaskAdd (DownloadTaskImpl task);
        public void onTaskRemove (DownloadTaskImpl task);
    }

    public interface OnDownloadCallback {
        public void onTaskWait (String id);
        public void onTaskStart (String id);
        public void onTaskPausing (String id);
        public void onTaskPause(String id);
        public void onTaskProgress (String id, long current, long total, int percent);
        public void onTaskFinish (String id);
        public void onTaskFailed ();
        public void onTaskCancel (String id);
    }
}

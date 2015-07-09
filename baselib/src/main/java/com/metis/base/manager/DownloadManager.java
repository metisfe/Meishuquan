package com.metis.base.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.metis.base.Debug;
import com.metis.base.service.DownloadService;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Beak on 2015/7/7.
 */
public class DownloadManager extends AbsManager {

    private static final String TAG = DownloadManager.class.getSimpleName();

    public static final String
            ACTION_DOWNLOAD_STATE_CHANGE = "action_download_state_change",
            ACTION_DOWNLOAD_PROGRESS_CHANGE = "action_download_progress_change";

    public static final String
            KEY_STATE = "state",
            KEY_ID = "id",
            KEY_TARGET_URL = "targetUrl",
            KEY_TARGET_PATH = "targetPath",
            KEY_NAME = "name",
            KEY_ACTION = "action",
            KEY_CURRENT = "progress",
            KEY_TOTAL = "total";

    public static final int STATE_IDLE = 0, STATE_RUNNING = 1, STATE_SUCCESS = 2, STATE_FAILED = 3, STATE_CANCELED = 4;
    public static final int ACTION_START = 1, ACTION_CANCEL = 2;

    private static DownloadManager sManager = null;
    public static synchronized DownloadManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new DownloadManager(context.getApplicationContext());
        }
        return sManager;
    }

    private IntentFilter mStateFilter = new IntentFilter(ACTION_DOWNLOAD_STATE_CHANGE);
    private IntentFilter mProgressFilter = new IntentFilter(ACTION_DOWNLOAD_PROGRESS_CHANGE);
    private LocalBroadcastManager mBroadcastManager = null;
    private List<TaskWrapper> mTaskWrapperList = null;

    private IntentFilter mNetFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    private BroadcastReceiver mNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo activeInfo = manager.getActiveNetworkInfo();
            if (wifiInfo == null || !wifiInfo.isConnected()) {
                stopAll();
            }
        }
    };

    private BroadcastReceiver mChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String id = intent.getStringExtra(KEY_ID);
            TaskWrapper wrapper = getTaskWrapper(id);
            if (action.equals(ACTION_DOWNLOAD_STATE_CHANGE)) {
                int state = intent.getIntExtra(KEY_STATE, STATE_IDLE);
                if (Debug.DEBUG) {
                    Log.v(TAG, "onReceive" + id + " STATE_CHANGE " + state);
                }

                if (wrapper != null) {
                    wrapper.setState(state);
                }
                switch (state) {
                    case STATE_IDLE:
                        break;
                    case STATE_RUNNING:
                        break;
                    case STATE_FAILED:
                    case STATE_SUCCESS:
                        TaskWrapper firstIdleWrapper = getFirstIdleTaskWrapper();
                        if (firstIdleWrapper != null) {
                            startTask(firstIdleWrapper);
                        }
                        break;
                }
            } else if (action.equals(ACTION_DOWNLOAD_PROGRESS_CHANGE)) {
                long current = intent.getLongExtra(KEY_CURRENT, 0);
                long total = intent.getLongExtra(KEY_TOTAL, 100);
                if (Debug.DEBUG) {
                    Log.v(TAG, "onReceive " + id + "PROGRESS_CHANGE " + current + " " + total);
                }

                if (wrapper != null) {
                    wrapper.setCurrent(current);
                    wrapper.setTotal(total);
                }
            }
        }
    };

    private DownloadManager(Context context) {
        super(context);
        mTaskWrapperList = new ArrayList<TaskWrapper>();
        mBroadcastManager = LocalBroadcastManager.getInstance(context);
        mBroadcastManager.registerReceiver(mChangeReceiver, mStateFilter);
        mBroadcastManager.registerReceiver(mChangeReceiver, mProgressFilter);

        context.registerReceiver(mNetReceiver, mNetFilter);
        //ConnectivityManager m = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        /*NetworkRequest request = new NetworkRequest.Builder().build();
        m.registerNetworkCallback(request, new ConnectivityManager.NetworkCallback() {

        });*/
    }

    private void excuteService(int action, TaskWrapper taskWrapper) {
        if (action == ACTION_START || action == ACTION_CANCEL) {
            Task task = taskWrapper.task;
            Intent it = new Intent(getContext(), DownloadService.class);
            it.putExtra(KEY_ACTION, action);
            it.putExtra(KEY_ID, task.getId());
            it.putExtra(KEY_NAME, task.getName());
            it.putExtra(KEY_TARGET_URL, task.getTargetUrl());
            it.putExtra(KEY_TARGET_PATH, taskWrapper.getTargetPath());
            getContext().startService(it);
        } else {
            throw new IllegalArgumentException("no action found, choose DownloadManager.ACTION_START or DownloadManager.ACTION_CANCEL");
        }
    }

    public void startTask (String id) {
        TaskWrapper wrapper = getTaskWrapper(id);
        if (wrapper != null) {
            startTask(wrapper);
        }
    }

    public void startTask (TaskWrapper taskWrapper) {
        excuteService(ACTION_START, taskWrapper);
    }

    public void stopTask (TaskWrapper taskWrapper) {
        excuteService(ACTION_CANCEL, taskWrapper);
    }

    public void stopTask (String id) {
        TaskWrapper wrapper = getTaskWrapper(id);
        if (wrapper != null) {
            stopTask(wrapper);
        }
    }

    public void stopAll () {
        final int length = mTaskWrapperList.size();
        Toast.makeText(getContext(), "stopAll ", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < length; i++) {
            TaskWrapper wrapper = mTaskWrapperList.get(i);
            if (wrapper.getState() == DownloadManager.STATE_RUNNING) {
                stopTask(wrapper);
            }
        }
    }

    public synchronized void addTask (Task task) {
        TaskWrapper wrapper = new TaskWrapper(task);
        if (mTaskWrapperList.contains(wrapper)) {
            return;
        }
        String downloadPath = null;
        if (TextUtils.isEmpty(task.targetPath)) {
            downloadPath = CacheDirManager.getInstance(getContext()).getCacheFolder(task.taskType.name) + File.separator + task.getName();
        } else {
            downloadPath = task.targetPath;
        }
        wrapper.setTargetPath(downloadPath);
        mTaskWrapperList.add(wrapper);
        if (getRunningTaskCount() <= 0) {
            excuteService(ACTION_START, wrapper);
        }
    }

    public synchronized void removeTask (Task task) {
        TaskWrapper wrapper = new TaskWrapper(task);
        if (mTaskWrapperList.contains(wrapper)) {
            mTaskWrapperList.remove(wrapper);
        }
    }

    public synchronized void removeTask (String id) {
        int index = -1;
        final int length = mTaskWrapperList.size();
        for (int i = 0; i < length; i++) {
            Task task = mTaskWrapperList.get(i).task;
            if (task.id.equals(id)) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            mTaskWrapperList.remove(index);
        }
    }

    public TaskWrapper getTaskWrapper (String id) {
        final int length = mTaskWrapperList.size();
        for (int i = 0; i < length; i++) {
            TaskWrapper wrapper = mTaskWrapperList.get(i);
            if (wrapper.task.id.equals(id)) {
                return wrapper;
            }
        }
        return null;
    }

    public List<TaskWrapper> getTaskWrapperList () {
        return mTaskWrapperList;
    }

    public int getRunningTaskCount () {
        final int length = mTaskWrapperList.size();
        int count = 0;
        for (int i = 0; i < length; i++) {
            TaskWrapper wrapper = mTaskWrapperList.get(i);
            if (wrapper.getState() == STATE_RUNNING) {
                count += 1;
            }
        }
        return count;
    }

    public TaskWrapper getFirstIdleTaskWrapper () {
        final int length = mTaskWrapperList.size();
        for (int i = 0; i < length; i++) {
            TaskWrapper wrapper = mTaskWrapperList.get (i);
            if (wrapper.getState() == STATE_IDLE) {
                return wrapper;
            }
        }
        return null;
    }

    public void recycle () {
        mBroadcastManager.unregisterReceiver(mChangeReceiver);
        this.getContext().unregisterReceiver(mNetReceiver);
    }

    public static class TaskWrapper implements Serializable {

        private int state = STATE_IDLE;
        private long current;
        private long total;
        private Task task;
        private String targetPath;

        private TaskWrapper (Task task) {
            if (task == null) {
                throw new NullPointerException("the argument task can not be null");
            }
            this.task = task;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public long getCurrent() {
            return current;
        }

        public void setCurrent(long current) {
            this.current = current;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public Task getTask() {
            return task;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public String getTargetPath() {
            return targetPath;
        }

        public void setTargetPath(String targetPath) {
            this.targetPath = targetPath;
        }

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof TaskWrapper) {
                return ((TaskWrapper) o).task.equals(task);
            }
            return false;
        }
    }

    public static class Task implements Serializable {

        private String name;
        private String targetUrl;
        private String id;
        private String thumb;
        private String targetPath;
        private TaskType taskType = TaskType.FILE;

        public Task (String name, String targetUrl) {
            this (System.currentTimeMillis() + "_" + UUID.randomUUID(), name, targetUrl, "", null, TaskType.FILE);
        }

        public Task (String id, String name, String targetUrl, String thumb, String targetPath, TaskType taskType) {
            if (targetUrl == null) {
                throw new NullPointerException("targetUrl can not be null");
            }
            this.name = name;
            this.targetUrl = targetUrl;
            this.id = id;
            this.thumb = thumb;
            this.targetPath = targetPath;
            this.taskType = taskType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTargetUrl() {
            return targetUrl;
        }

        public void setTargetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getTargetPath() {
            return targetPath;
        }

        public void setTargetPath(String targetPath) {
            this.targetPath = targetPath;
        }

        public TaskType getTaskType() {
            return taskType;
        }

        public void setTaskType(TaskType taskType) {
            this.taskType = taskType;
        }

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof Task) {
                return ((Task) o).targetUrl.equals(targetUrl);
            }
            return false;
        }
    }

    public static enum TaskType {

        FILE ("file"),
        VIDEO ("video"),
        IMAGE ("image");

        public String name;
        TaskType (String name) {
            this.name = name;
        }
    }

}

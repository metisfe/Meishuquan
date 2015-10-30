package com.metis.base.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.bokecc.sdk.mobile.download.Downloader;
import com.metis.base.R;
import com.metis.base.manager.CacheManager;
import com.metis.base.manager.DownloadManager;
import com.metis.base.module.DownloadTaskImpl;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.DownloadingAdapter;
import com.metis.base.widget.adapter.delegate.DownloadTaskDelegate;

import java.io.File;

public class DownloadingActivity extends TitleBarActivity {

    private static final String TAG = DownloadingActivity.class.getSimpleName();

    private RecyclerView mDownloadingRv = null;

    private DownloadingAdapter mDownloadingAdapter = null;

    private Button mAddBtn = null;

    private int mProgress = 0;

    private boolean isInEditMode = false;

    private DownloadManager.OnTaskQueueChangeCallback mQueueCallback = new DownloadManager.OnTaskQueueChangeCallback() {
        @Override
        public void onTaskAdd(DownloadTaskImpl task) {
            mDownloadingAdapter.addDataItem(new DownloadTaskDelegate(task));
            mDownloadingAdapter.notifyDataSetChanged();
        }

        @Override
        public void onTaskRemove(DownloadTaskImpl task) {
            mDownloadingAdapter.removeTaskItem(task.getId());
        }
    };
    private DownloadManager.OnDownloadCallback mDownloadCallback = new DownloadManager.OnDownloadCallback() {
        @Override
        public void onTaskWait(String id) {

        }

        @Override
        public void onTaskStart(String id) {

        }

        @Override
        public void onTaskPausing(String id) {

        }

        @Override
        public void onTaskPause(String id) {

        }

        @Override
        public void onTaskProgress(String id, long current, long total, int percent) {
            mDownloadingAdapter.notifyDataSetChanged();
        }

        @Override
        public void onTaskFinish(String id) {

        }

        @Override
        public void onTaskFailed() {

        }

        @Override
        public void onTaskCancel(String id) {

        }
    };
    private DownloadManager mDownloadManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloading);

        mDownloadManager = DownloadManager.getInstance(this);
        mDownloadManager.registerOnDownloadCallback(mDownloadCallback);
        mDownloadManager.registerOnTaskQueueChangeCallback(mQueueCallback);

        mAddBtn = (Button)findViewById(R.id.downloading_add_task);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(CacheManager.getInstance(DownloadingActivity.this).getMyVideoCacheDir(), "1");
                Log.v(TAG, "download to " + file.getAbsolutePath());
                DownloadTaskImpl task = new DownloadTaskImpl();
                task.setState(Downloader.WAIT);
                task.setTargetPath(file.getAbsolutePath());
                task.setId("2C4349EB23345F859C33DC5901307461");
                mDownloadManager.addTask(task);
            }
        });

        mDownloadingRv = (RecyclerView)findViewById(R.id.downloading_recycler_view);
        mDownloadingRv.setLayoutManager(new LinearLayoutManager(this));

        mDownloadingAdapter = new DownloadingAdapter(this);

        mDownloadingRv.setAdapter(mDownloadingAdapter);

        getTitleBar().setTitleRight(R.string.text_edit_mode);
        getTitleBar().setOnRightBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInEditMode = !isInEditMode;
                if (isInEditMode) {
                    getTitleBar().setTitleRight(R.string.cancel);
                } else {
                    getTitleBar().setTitleRight(R.string.text_edit_mode);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDownloadManager.unregisterOnDownloadCallback(mDownloadCallback);
        mDownloadManager.unregisterOnTaskQueueChangeCallback(mQueueCallback);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

}

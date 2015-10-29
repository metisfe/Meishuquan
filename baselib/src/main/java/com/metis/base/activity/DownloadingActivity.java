package com.metis.base.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.bokecc.sdk.mobile.download.Downloader;
import com.metis.base.R;
import com.metis.base.manager.DownloadManager;
import com.metis.base.module.DownloadTaskImpl;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.DownloadingAdapter;
import com.metis.base.widget.adapter.delegate.DownloadTaskDelegate;

public class DownloadingActivity extends TitleBarActivity implements DownloadManager.OnDownloadListener{

    private static final String TAG = DownloadingActivity.class.getSimpleName();

    private RecyclerView mDownloadingRv = null;

    private DownloadingAdapter mDownloadingAdapter = null;

    private Button mAddBtn = null;

    private int mProgress = 0;

    private boolean isInEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloading);

        mAddBtn = (Button)findViewById(R.id.downloading_add_task);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTaskImpl task = new DownloadTaskImpl();
                task.setId("2C4349EB23345F859C33DC5901307461");
                task.setName("一个视频而已");
                task.setState(Downloader.WAIT);
                task.setProgress(mProgress += 5);
                DownloadManager.getInstance(DownloadingActivity.this).putTask(task);
            }
        });

        mDownloadingRv = (RecyclerView)findViewById(R.id.downloading_recycler_view);
        mDownloadingRv.setLayoutManager(new LinearLayoutManager(this));

        mDownloadingAdapter = new DownloadingAdapter(this);

        mDownloadingRv.setAdapter(mDownloadingAdapter);

        DownloadManager.getInstance(this).registerOnDownloadListener(this);

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
        DownloadManager.getInstance(this).unregisterOnDownloadListener(this);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public void onNewTaskAdded(DownloadTaskImpl task) {
        mDownloadingAdapter.addDataItem(new DownloadTaskDelegate(task));
        mDownloadingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskStateChanged(DownloadTaskImpl task) {

    }

    @Override
    public void onTaskProgressChanged(DownloadTaskImpl task) {
        Log.v(TAG, "task onTaskProgressChanged=" + task.getProgress());
    }
}

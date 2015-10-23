package com.metis.base.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.metis.base.R;
import com.metis.base.manager.DownloadManager;
import com.metis.base.module.DownloadTaskImpl;
import com.metis.base.widget.adapter.DownloadingAdapter;
import com.metis.base.widget.adapter.delegate.DownloadTaskDelegate;

public class DownloadingActivity extends TitleBarActivity implements DownloadManager.OnDownloadListener{

    private RecyclerView mDownloadingRv = null;

    private DownloadingAdapter mDownloadingAdapter = null;

    private Button mAddBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloading);

        mAddBtn = (Button)findViewById(R.id.downloading_add_task);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTaskImpl task = new DownloadTaskImpl();
                task.setName("一个视频而已");
                task.setPrgoress(34);
                DownloadManager.getInstance(DownloadingActivity.this).putTask(task);
            }
        });

        mDownloadingRv = (RecyclerView)findViewById(R.id.downloading_recycler_view);
        mDownloadingRv.setLayoutManager(new LinearLayoutManager(this));

        mDownloadingAdapter = new DownloadingAdapter(this);

        mDownloadingRv.setAdapter(mDownloadingAdapter);

        DownloadManager.getInstance(this).registerOnDownloadListener(this);
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

    }
}

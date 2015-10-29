package com.metis.base.widget.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.metis.base.R;
import com.metis.base.manager.DownloadManager;
import com.metis.base.module.DownloadTaskImpl;
import com.metis.base.widget.adapter.delegate.DownloadTaskDelegate;

/**
 * Created by Beak on 2015/10/22.
 */
public class DownloadTaskHolder extends AbsViewHolder<DownloadTaskDelegate> {

    private TextView mTitleTv, mSpeedTv, mProgressTv;
    private ProgressBar mPb = null;

    public DownloadTaskHolder(View itemView) {
        super(itemView);

        mTitleTv = (TextView)itemView.findViewById(R.id.downloading_item_name);
        //mSpeedTv = (TextView)itemView.findViewById(R.id.downloading_item_speed);
        mProgressTv = (TextView)itemView.findViewById(R.id.downloading_item_progress_in_text);
        mPb = (ProgressBar)itemView.findViewById(R.id.downloading_item_progress);
    }

    @Override
    public void bindData(Context context, DownloadTaskDelegate downloadTaskDelegate, RecyclerView.Adapter adapter, int position) {
        DownloadTaskImpl task = downloadTaskDelegate.getSource();
        mTitleTv.setText(task.getName());
        mPb.setProgress(downloadTaskDelegate.getSource().getProgress());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

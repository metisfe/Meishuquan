package com.metis.commentpart.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.metis.base.activity.TitleBarActivity;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.StatusAdapter;


public class StatusDetailActivity extends TitleBarActivity {

    private RecyclerView mDetailRv = null;

    private StatusAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);

        mDetailRv = (RecyclerView)findViewById(R.id.detail_recycler_view);
        mDetailRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StatusAdapter(this);
        mDetailRv.setAdapter(mAdapter);
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.title_activity_comment_detail);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }
}

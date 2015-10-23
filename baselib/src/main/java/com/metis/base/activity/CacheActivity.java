package com.metis.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.metis.base.R;

public class CacheActivity extends TitleBarActivity implements View.OnClickListener {

    private RelativeLayout mDownloadingLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);

        mDownloadingLayout = (RelativeLayout)findViewById(R.id.cache_downloading_layout);

        mDownloadingLayout.setOnClickListener(this);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.title_activity_cache);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == mDownloadingLayout.getId()) {
            startActivity(new Intent(this, DownloadingActivity.class));
        }
    }
}

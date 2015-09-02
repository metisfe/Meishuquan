package com.metis.newslib.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.metis.base.activity.BaseActivity;
import com.metis.base.manager.RequestCallback;
import com.metis.base.utils.Log;
import com.metis.msnetworklib.contract.ReturnInfo;
import com.metis.newslib.ActivityDispatcher;
import com.metis.newslib.R;
import com.metis.newslib.manager.NewsManager;
import com.metis.newslib.module.NewsDetails;

public class NewsDetailActivity extends BaseActivity {

    private static final String TAG = NewsDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        long newsId = getIntent().getLongExtra(ActivityDispatcher.NEWS_ID, 0);

        NewsManager.getInstance(this).getNewsInfoById(newsId, new RequestCallback<NewsDetails>() {
            @Override
            public void callback(ReturnInfo<NewsDetails> returnInfo, String callbackId) {
                if (returnInfo.isSuccess()) {
                    Log.v(TAG, "onPostCreate " + returnInfo.getData().content);
                }
            }
        });
    }
}

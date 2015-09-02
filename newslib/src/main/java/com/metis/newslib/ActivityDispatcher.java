package com.metis.newslib;

import android.content.Context;
import android.content.Intent;

import com.metis.newslib.activity.NewsDetailActivity;

/**
 * Created by Beak on 2015/9/2.
 */
public class ActivityDispatcher {

    public static final String NEWS_ID = "news_id";

    public static void newsDetailActivity (Context context, long newsId) {
        Intent it = new Intent(context, NewsDetailActivity.class);
        it.putExtra(NEWS_ID, newsId);
        context.startActivity(it);
    }
}

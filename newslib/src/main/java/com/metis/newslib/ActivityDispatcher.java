package com.metis.newslib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.metis.newslib.activity.NewsDetailActivity;
import com.metis.newslib.activity.NewsReplyActivity;
import com.metis.newslib.module.NewsCommentItem;
import com.metis.newslib.module.NewsDetails;

/**
 * Created by Beak on 2015/9/2.
 */
public class ActivityDispatcher {

    public static final String
            KEY_NEWS_ID = "news_id",
            KEY_NEWS_DETAIL = "news_detail",
            KEY_NEWS_COMMENT_ITEM = "news_comment_item";

    public static final int REQUEST_CODE_REPLY = 10086;

    public static void newsDetailActivity (Context context, long newsId) {
        Intent it = new Intent(context, NewsDetailActivity.class);
        it.putExtra(KEY_NEWS_ID, newsId);
        context.startActivity(it);
    }

    public static void replyActivity (Activity activity, NewsDetails details, NewsCommentItem item) {
        Intent it = new Intent(activity, NewsReplyActivity.class);
        it.putExtra(KEY_NEWS_DETAIL, details);
        it.putExtra(KEY_NEWS_COMMENT_ITEM, item);
        activity.startActivityForResult(it, REQUEST_CODE_REPLY);
    }
}

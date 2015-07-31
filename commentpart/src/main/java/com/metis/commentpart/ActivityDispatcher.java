package com.metis.commentpart;

import android.content.Context;
import android.content.Intent;

import com.metis.commentpart.activity.FilterActivity;
import com.metis.commentpart.activity.PublishStatusActivity;
import com.metis.commentpart.activity.StatusDetailActivity;

/**
 * Created by Beak on 2015/7/24.
 */
public class ActivityDispatcher {

    public static void statusDetail (Context context) {
        Intent it = new Intent (context, StatusDetailActivity.class);
        context.startActivity(it);
    }

    public static void filterActivity (Context context) {
        Intent it = new Intent(context, FilterActivity.class);
        context.startActivity(it);
    }

    public static void publishStatusActivity (Context context) {
        Intent it = new Intent (context, PublishStatusActivity.class);
        context.startActivity(it);
    }

}

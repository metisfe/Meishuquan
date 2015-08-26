package com.metis.meishuquan;

import android.content.Context;
import android.content.Intent;

import com.metis.meishuquan.activity.LoginActivity;
import com.metis.meishuquan.activity.RegisterActivity;

/**
 * Created by Beak on 2015/8/21.
 */
public class ActivityDispatcher {

    public static final String KEY_MODE = "key_mode";

    public static void resetPwdActivity (Context context) {
        Intent it = new Intent (context, RegisterActivity.class);
        it.putExtra(KEY_MODE, RegisterActivity.MODE_RESET_PWD);
        context.startActivity(it);
    }

    public static void loginActivity (Context context) {
        Intent it = new Intent (context, LoginActivity.class);
        context.startActivity(it);
    }
}

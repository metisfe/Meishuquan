package com.metis.meishuquan.activity;

import android.content.Intent;
import android.os.Bundle;

import com.metis.base.ActivityDispatcher;
import com.metis.base.activity.BaseActivity;
import com.metis.base.activity.debug.DebugActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.User;
import com.metis.meishuquan.R;
import com.metis.msnetworklib.contract.ReturnInfo;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;


public class HelloActivity extends BaseActivity {

    private boolean isToDebugActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        final AccountManager.LoginInfo info = AccountManager.getInstance(this).readUserLoginInfo();

        if (Wechat.NAME.equals(info.getLoginType())
                || SinaWeibo.NAME.equals(info.getLoginType())
                || QQ.NAME.equals(info.getLoginType())) {
            Platform platform = ShareSDK.getPlatform(HelloActivity.this, info.getLoginType());
            if (platform != null && platform.isValid()) {
                PlatformDb db = platform.getDb();
                AccountManager.getInstance(HelloActivity.this).authLogin(db.getUserId(), AccountManager.getTypeId(info.getLoginType()), new RequestCallback<User>() {
                    @Override
                    public void callback(ReturnInfo<User> returnInfo, String callbackId) {
                        if (isToDebugActivity) {
                            startActivity(new Intent(HelloActivity.this, DebugActivity.class));
                            return;
                        }
                        if (returnInfo.isSuccess()) {
                            ActivityDispatcher.mainActivity(HelloActivity.this);
                        } else {
                            ActivityDispatcher.loginActivity(HelloActivity.this);
                        }
                        finish();
                    }
                });
            } else {
                delayStartActivity();
            }
        } else {
            delayStartActivity();
        }

        //startActivity(new Intent(HelloActivity.this, LoginActivity.class/*RoleChooseActivity.class*/));


    }

    private void delayStartActivity () {
        if (isToDebugActivity) {
            startActivity(new Intent(HelloActivity.this, DebugActivity.class));
            return;
        }
        this.getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityDispatcher.loginActivity(HelloActivity.this);
                finish();
            }
        }, 3000);
    }

}

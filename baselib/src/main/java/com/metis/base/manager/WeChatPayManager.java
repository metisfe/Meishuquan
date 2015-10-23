package com.metis.base.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Beak on 2015/10/16.
 */
public class WeChatPayManager extends AbsManager {

    private static final String TAG = WeChatPayManager.class.getSimpleName();

    private static final String API_ID = "wx144663d4ae48cdcf";

    private static WeChatPayManager sManager = null;

    public static synchronized WeChatPayManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new WeChatPayManager(context.getApplicationContext());
        }
        return sManager;
    }

    private IWXAPI mApi = null;

    private BroadcastReceiver mAppReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();
        }
    };

    private WeChatPayManager(Context context) {
        super(context);

        mApi = WXAPIFactory.createWXAPI(context, API_ID, false);
    }

    public void registerToWechat () {
        getContext().registerReceiver(
                mAppReceiver,
                new IntentFilter("com.tencent.mm.plugin.openapi.Intent.ACTION_REFRESH_WXAPP"),
                "com.tencent.mm.plugin.permission.SEND", null);
        mApi.registerApp(API_ID);

    }

    public void unregisterFromWechat () {
        getContext().unregisterReceiver(mAppReceiver);
    }
}

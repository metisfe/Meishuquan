package com.metis.base.manager;

import android.content.Context;

import com.metis.base.utils.Log;
import com.mob.tools.utils.UIHandler;

import java.util.HashMap;
import java.util.Set;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Beak on 2015/8/26.
 */
public class ShareManager extends AbsManager{

    private static final String TAG = ShareManager.class.getSimpleName();

    private static ShareManager sManager = null;

    public static synchronized ShareManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new ShareManager(context.getApplicationContext());
        }
        return sManager;
    }

    private ShareManager(Context context) {
        super(context);
        ShareSDK.initSDK(context);
    }

    public void loginAccess (Platform platform, PlatformActionListener listener) {
        if (platform == null) {
            return;
        }
        if (platform.isValid()) {
            //TODO
            listener.onComplete(platform, 0, null);
            PlatformDb db = platform.getDb();
            String userId = db.getUserId();
            String profile = db.getUserIcon();
            if (userId != null) {
                //UIHandler.sendEmptyMessage(Platform.)
                //TODO
                return;
            }
        }
        platform.setPlatformActionListener(listener);
        platform.SSOSetting(false);
        platform.showUser(null);
        //platform.authorize();
    }

    /*public void loginAccessWeChat (Context context, PlatformActionListener listener) {

        Platform wechat= ShareSDK.getPlatform(context, Wechat.NAME);
        boolean valid = wechat.isValid();
        Log.v(TAG, "loginAccessWeChat " + valid + " " + wechat.getId());
        wechat.setPlatformActionListener(listener);
        wechat.authorize();
    }*/

    /*@Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        StringBuilder stringBuilder = new StringBuilder("onComplete:");
        if (hashMap != null) {
            Set<String> set = hashMap.keySet();
            for (String key : set) {
                stringBuilder.append(key + " " + hashMap.get(key) + "\n");
            }
        }

        Log.v(TAG, stringBuilder.toString());
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.e(TAG, "onError " + platform.getName() + " i=" + i);
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {

    }*/
}

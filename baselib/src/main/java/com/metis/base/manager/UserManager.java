package com.metis.base.manager;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.metis.base.framework.NetProxy;
import com.metis.base.module.SimpleProvince;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.List;

/**
 * Created by Beak on 2015/10/10.
 */
public class UserManager extends AbsManager {

    private static final String TAG = UserManager.class.getSimpleName();

    private static String
            URL_CENTER = "v1.1/UserCenter/GetUser?userId={userId}",
            URL_PROVINCE = "v1.1/UserCenter/Province";

    private static UserManager sManager = null;

    public static synchronized UserManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new UserManager(context.getApplicationContext());
        }
        return sManager;
    }

    private UserManager(Context context) {
        super(context);
    }

    public void getUserInfo (long userId, final RequestCallback<User> callback) {
        String request = URL_CENTER.replace("{userId}", userId + "");
        NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<User> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<User>>(){}.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    public void getProvince (final RequestCallback<List<SimpleProvince>> callback) {
        NetProxy.getInstance(getContext()).doGetRequest(URL_PROVINCE, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<List<SimpleProvince>> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<List<SimpleProvince>>>(){}.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }
}

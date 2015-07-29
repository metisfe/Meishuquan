package com.metis.base.manager;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.metis.base.framework.NetProxy;
import com.metis.base.module.User;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Beak on 2015/7/24.
 */
public class AccountManager extends AbsManager {

    private static AccountManager sManager = null;

    public synchronized static AccountManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new AccountManager(context.getApplicationContext());
        }
        return sManager;
    }

    private static final String
            LOGIN = "v1.1/UserCenter/Login",
            REGISTER = "v1.1/UserCenter/Register",//获取验证码;/*?phone={phone}&code={code}&pwd={pwd}&roleId={roleId}*/
            REQUEST_CODE = "v1.1/UserCenter/RegisterCode?phone={phone}&operation={operation}";

    private User mMe = null;

    private AccountManager(Context context) {
        super(context);
    }

    public User getMe () {
        return mMe;
    }

    public String login (String account, String pwd, final RequestCallback<User> callback) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("account", account);
        map.put("pwd", pwd);
        return NetProxy.getInstance(getContext()).doPostRequest(LOGIN, map, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<User> returnInfo = getGson().fromJson(
                        result,
                        new TypeToken<ReturnInfo<User>>(){}.getType());
                if (returnInfo.isSuccess()) {
                    mMe = returnInfo.getData();
                }
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    /**
     *
     * @param phone
     * @param codeType 1 or 2
     */
    public void getRequestCode (String phone, int codeType) {
        String request = REQUEST_CODE
                .replace("{phone}", phone)
                .replace("{operation}", codeType + "");
        NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {

            }
        });
    }

    public void register (String phone, String code, String pwd, int roleId) {
        String request = REGISTER;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("code", code);
        map.put("pwd", pwd);
        map.put("roleId", roleId + "");
        NetProxy.getInstance(getContext()).doPostRequest(request, map, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {

            }
        });
    }
}

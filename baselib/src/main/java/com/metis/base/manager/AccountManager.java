package com.metis.base.manager;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.metis.base.ActivityDispatcher;
import com.metis.base.framework.NetProxy;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
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
            REQUEST_CODE = "v1.1/UserCenter/RegisterCode?phone={phone}&operation={operation}",
            URL_ATTENTION = "v1.1/Circle/FocusUserForGroup?userId={userId}&groupId={groupId}&session={session}",
            URL_CANCEL_ATTENTION = "v1.1/Circle/CancelAttention?userId={userId}&session={session}",
            MOMENTSGROUPS = "v1.1/Circle/MyDiscussions?userid={userid}&type={type}&session={session}";//朋友圈分组信息;

    private User mMe = null;

    private AccountManager(Context context) {
        super(context);
    }

    public User getMe () {
        return mMe;
    }

    public String getCookies () {
        if (mMe != null) {
            return mMe.getCookie();
        }
        ActivityDispatcher.loginActivity(getContext());
        return "";
    }

    public String login (String account, String pwd, final RequestCallback<User> callback) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("account", account);
        map.put("pwd", pwd);
        return NetProxy.getInstance(getContext()).doPostRequest(LOGIN, map, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<User> returnInfo = null;
                try {
                    returnInfo = getGson().fromJson(
                            result,
                            new TypeToken<ReturnInfo<User>>(){}.getType());
                } catch (IllegalStateException e) {
                    String errorMsg = e.getMessage();
                }

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
    public void getRequestCode (String phone, RequestCodeTypeEnum codeType, final RequestCallback<Integer> callback) {
        String request = REQUEST_CODE
                .replace("{phone}", phone)
                .replace("{operation}", codeType.getVal() + "");
        NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<Integer> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<Integer>>(){}.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
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

    public void attention (long userId, long groupId, final RequestCallback callback) {
        if (mMe == null) {
            //TODO
            return;
        }
        String request = URL_ATTENTION
                .replace("{userId}", userId + "")
                .replace("{groupId}", groupId + "")
                .replace("{session}", mMe.getCookie());
        NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo>() {
                }.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    public void cancelAttention (long userId, final RequestCallback callback) {
        if (mMe == null) {
            //TODO
            return;
        }
        String request = URL_CANCEL_ATTENTION
                .replace("{userId}", userId + "")
                .replace("{session}", mMe.getCookie());
        NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo>() {
                }.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    public void getAttentionGroup () {
        if (mMe == null) {
            return;
        }
        String request = MOMENTSGROUPS
                .replace("{userid}", mMe.userId + "")
                .replace("{type}", 1 + "")
                .replace("{session}", mMe.getCookie());
        NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {

            }
        });
    }

    public enum RequestCodeTypeEnum {
        REGISTER(1), RESET_PWD(2);
        private final int val;

        private RequestCodeTypeEnum(int val) {
            this.val = val;
        }

        public int getVal() {
            return this.val;
        }
    }
}

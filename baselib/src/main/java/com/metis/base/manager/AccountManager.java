package com.metis.base.manager;

import android.content.Context;
import android.support.v4.view.ViewCompat;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.metis.base.ActivityDispatcher;
import com.metis.base.framework.NetProxy;
import com.metis.base.module.User;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

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

    public static final int AUTH_TYPE_WE_CHAT = 0, AUTH_TYPE_SINA = 1, AUTH_TYPE_QQ = 2;

    private static final String
            LOGIN = "v1.1/UserCenter/Login",
            REGISTER = "v1.1/UserCenter/Register",//获取验证码;/*?phone={phone}&code={code}&pwd={pwd}&roleId={roleId}*/
            REQUEST_CODE = "v1.1/UserCenter/RegisterCode?phone={phone}&operation={operation}",
            URL_ATTENTION = "v1.1/Circle/FocusUserForGroup?userId={userId}&groupId={groupId}&session={session}",
            URL_CANCEL_ATTENTION = "v1.1/Circle/CancelAttention?userId={userId}&session={session}",
            FORGET_PWD = "v1.1/UserCenter/ForgetPassword?account={account}&code={code}&newPwd={newPwd}&type={type}",
            URL_UPDATE_USER_INFO = "v1.1/UserCenter/UpdateUserInfo?param={param}&session={session}",
            URL_AUTH_LOGIN = "v1.1/UserCenter/LoginByAuthorize",
            MOMENTSGROUPS = "v1.1/Circle/MyDiscussions?userid={userid}&type={type}&session={session}";//朋友圈分组信息

    private static final String SMS_KEY = "9f2e7cad8207", SMS_SECRET = "0dec3be14cc54f4f8efd468a1b4397a7";

    private boolean isSmsSdkInit = false;

    private User mMe = null;

    private List<OnUserChangeListener> mUserChangeListenerList = new ArrayList<OnUserChangeListener>();

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
        ActivityDispatcher.loginActivityWhenAlreadyIn(getContext());
        return "";
    }

    private void smsSdkInit () {
        if (!isSmsSdkInit) {
            SMSSDK.initSDK(getContext(), SMS_KEY, SMS_SECRET);
            isSmsSdkInit = true;
        }
    }

    public void registerEventHandler (EventHandler handler) {
        smsSdkInit();
        SMSSDK.registerEventHandler(handler);
    }

    public void askForSms (String phoneNum) {
        smsSdkInit();
        //SMSSDK.getSupportedCountries();
        SMSSDK.getVerificationCode(86 + "", phoneNum);
    }

    public void submitVerificationCode(String phone, String code) {
        SMSSDK.submitVerificationCode(86 + "", phone, code);
    }

    public void unregisterEventHandler (EventHandler handler) {
        smsSdkInit();
        SMSSDK.unregisterEventHandler(handler);
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
                if (!mUserChangeListenerList.isEmpty()) {
                    final int length = mUserChangeListenerList.size();
                    for (int i = 0; i < length; i++) {
                        OnUserChangeListener userChangeListener = mUserChangeListenerList.get(i);
                        userChangeListener.onUserChanged(mMe, returnInfo.isSuccess());
                    }
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
                ReturnInfo<Integer> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<Integer>>() {
                }.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    public void resetPwd (String account, String code, String newPwd, final RequestCallback callback) {
        final String request = FORGET_PWD
                .replace("{account}", account)
                .replace("{code}", code)
                .replace("{newPwd}", newPwd)
                .replace("{type}", 1 + "");
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

    public void register (String phone, String code, String pwd, int roleId, final RequestCallback<User> callback) {
        String request = REGISTER;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("code", code);
        map.put("pwd", pwd);
        map.put("roleId", roleId + "");
        NetProxy.getInstance(getContext()).doPostRequest(request, map, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<User> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<User>>() {
                }.getType());

                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    /*
    1.1/UserCenter/LoginByAuthorize?openid={openid}&typeid={typeid}
    openid：第三方唯一ID
    typeid：0 微信，1 新浪微博，2 QQ
     */
    public void authLogin (String openId, int typeId, final RequestCallback<User> callback) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("openid", openId);
        map.put("typeid", typeId + "");
        /*String request = URL_AUTH_LOGIN
                .replace("{openid}", openId)
                .replace("{typeid}", typeId + "");*/
        NetProxy.getInstance(getContext()).doPostRequest(
                URL_AUTH_LOGIN, map, new NetProxy.OnResponseListener() {
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
                        if (!mUserChangeListenerList.isEmpty()) {
                            final int length = mUserChangeListenerList.size();
                            for (int i = 0; i < length; i++) {
                                OnUserChangeListener userChangeListener = mUserChangeListenerList.get(i);
                                userChangeListener.onUserChanged(mMe, returnInfo.isSuccess());
                            }
                        }
                    }
                }
        );
    }

    public void attention (long userId, long groupId, final RequestCallback callback) {
        if (mMe == null) {
            //TODO
            com.metis.base.ActivityDispatcher.loginActivityWhenAlreadyIn(getContext());
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
            com.metis.base.ActivityDispatcher.loginActivityWhenAlreadyIn(getContext());
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

    public void updateUserInfo (Map<String, String> map, final RequestCallback callback) {
        if (mMe == null) {
            //TODO
            return;
        }
        JsonObject json = new JsonObject();
        json.addProperty("userId", mMe.userId);
        Set<String> set = map.keySet();
        for (String key : set) {
            String value = map.get(key);
            value = URLEncoder.encode(value);
                /*if (!Patterns.WEB_URL.matcher(value).matches()) {
                    value = URLEncoder.encode(value);
                }*/
            json.addProperty(key, value);
        }
        String request = URL_UPDATE_USER_INFO
                .replace("{param}", json.toString())
                .replace("{session}", mMe.getCookie());
        NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo>(){}.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
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

    public void registerOnUserChangeListener (OnUserChangeListener listener) {
        if (mUserChangeListenerList.contains(listener)) {
            return;
        }
        mUserChangeListenerList.add(listener);
    }

    public void unregisterOnUserChangeListener (OnUserChangeListener listener) {
        if (!mUserChangeListenerList.contains(listener)) {
            return;
        }
        mUserChangeListenerList.remove(listener);
    }

    public static interface OnUserChangeListener {
        public void onUserChanged (User user, boolean onLine);
    }
}

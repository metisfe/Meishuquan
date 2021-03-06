package com.metis.base.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Patterns;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.metis.base.ActivityDispatcher;
import com.metis.base.framework.NetProxy;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.msnetworklib.contract.OptionSettings;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Beak on 2015/7/24.
 */
public class AccountManager extends AbsManager {

    private static final String TAG = AccountManager.class.getSimpleName();

    private static AccountManager sManager = null;

    public synchronized static AccountManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new AccountManager(context.getApplicationContext());
        }
        return sManager;
    }

    public static final int AUTH_TYPE_WE_CHAT = 1, AUTH_TYPE_SINA = 2, AUTH_TYPE_QQ = 3, AUTH_TYPE_NONE = 0;

    private static final String
            LOGIN = "v1.1/UserCenter/Login",
            REGISTER = "v1.1/UserCenter/Register",//获取验证码;/*?phone={phone}&code={code}&pwd={pwd}&roleId={roleId}*/
            REQUEST_CODE = "v1.1/UserCenter/RegisterCode?phone={phone}&operation={operation}",
            URL_ATTENTION = "v1.1/Circle/FocusUserForGroup?userId={userId}&groupId={groupId}&session={session}",
            URL_CANCEL_ATTENTION = "v1.1/Circle/CancelAttention?userId={userId}&session={session}",
            FORGET_PWD = "v1.1/UserCenter/ForgetPassword?account={account}&code={code}&newPwd={newPwd}&type={type}",
            URL_UPDATE_USER_INFO = "v1.1/UserCenter/UpdateUserInfo?param={param}&session={session}",
            URL_UPDATE_USER_INFO_POST = "v1.1/UserCenter/UpdateUserInfoPost?session={session}",
            URL_AUTH_LOGIN = "v1.1/UserCenter/LoginByAuthorize",
            CHECK_LOGIN_STATE = "v1.1/Default/Start?session=",//校验账号状态
            MOMENTSGROUPS = "v1.1/Circle/MyDiscussions?userid={userid}&type={type}&session={session}";//朋友圈分组信息

    private static final String SMS_KEY = "9f2e7cad8207", SMS_SECRET = "0dec3be14cc54f4f8efd468a1b4397a7";

    private boolean isSmsSdkInit = false;

    private User mMe = null;
    private List<OnUserChangeListener> mUserChangeListenerList = new ArrayList<OnUserChangeListener>();

    private File mMeFile = null;

    private AccountManager(Context context) {
        super(context);
        ShareSDK.initSDK(context);
        mMeFile = new File(getContext().getExternalCacheDir(), "me");
        readMe();
    }

    private void readMe () {
        try {
            FileInputStream fis = new FileInputStream(mMeFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            mMe = (User)ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void commitUser () {
        if (mMe == null) {
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(mMeFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mMe);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearUser () {
        if (mMeFile.exists()) {
            mMeFile.delete();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("me", mMe);
        /*List<Fragment> fragments = getSupportFragmentManager().getFragments();
        final int length = fragments.size();
        for (int i = 0; i < length; i++) {
            FragmentUtils.removeFragment(getSupportFragmentManager(), fragments.get(i));
        }
        mCurrentFragment = null;*/
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mMe = (User)savedInstanceState.getSerializable("me");
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

    public void saveUserLoginInfo (LoginInfo info) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginType", info.loginType);
        editor.commit();
    }

    public LoginInfo readUserLoginInfo() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
        LoginInfo info = new LoginInfo(sharedPreferences.getString("loginType", "none"));
        return info;
    }

    public void clearUserLoginInfo () {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static int getTypeId (String loginTypeStr) {
        if (Wechat.NAME.equals(loginTypeStr)) {
            return 0;
        } else if (SinaWeibo.NAME.equals(loginTypeStr)) {
            return 1;
        } else if (QQ.NAME.equals(loginTypeStr)) {
            return 2;
        }
        return -1;
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

    public void checkLoginState (final RequestCallback callback) {
        if (mMe == null) {
            ReturnInfo returnInfo = new ReturnInfo();
            OptionSettings optionSettings = new OptionSettings();
            optionSettings.status = "-10086";
            optionSettings.message = "mMe is null";
            optionSettings.errorCode = "-10086";
            returnInfo.setOption(optionSettings);
            callback.callback(returnInfo, "");
            return;
        }
        String request = CHECK_LOGIN_STATE + mMe.getCookie();
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
                    LoginInfo info = new LoginInfo(0);
                    saveUserLoginInfo(info);
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

    public void logout () {
        mMe = null;
        ShareManager.getInstance(getContext()).loginQuit(null);
        clearUserLoginInfo();
        clearUser();
        final int length = mUserChangeListenerList.size();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                mUserChangeListenerList.get(i).onUserLogout();
            }
        }
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
                ReturnInfo<User> returnInfo = null;
                try {
                    returnInfo = getGson().fromJson(
                            result,
                            new TypeToken<ReturnInfo<User>>() {
                            }.getType());
                } catch (IllegalStateException e) {
                    String errorMsg = e.getMessage();
                }

                if (returnInfo.isSuccess()) {
                    mMe = returnInfo.getData();
                    LoginInfo info = new LoginInfo(0);
                    saveUserLoginInfo(info);
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

    /*
    1.1/UserCenter/LoginByAuthorize?openid={openid}&typeid={typeid}
    openid：第三方唯一ID
    typeid：1 微信，2 新浪微博，3 QQ
     */
    public void authLogin (String openId, final int typeId, final RequestCallback<User> callback) {
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
                                    new TypeToken<ReturnInfo<User>>() {
                                    }.getType());
                        } catch (IllegalStateException e) {
                            String errorMsg = e.getMessage();
                        }

                        if (returnInfo.isSuccess()) {
                            mMe = returnInfo.getData();
                            LoginInfo info = new LoginInfo(typeId);
                            saveUserLoginInfo(info);
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

    public void updateUserRole (int userRole, final RequestCallback callback) {
        if (mMe == null) {
            //TODO
            return;
        }
        mMe.userRole = userRole;
        Map<String, String> map = new HashMap<String, String>();
        map.put("userRole", userRole + "");
        updateUserInfo(map, callback);
    }

    /*public void updateUserInfoPost (User user, final RequestCallback callback) {
        if (mMe == null) {
            return;
        }

        NetProxy.getInstance(getContext()).doPostRequest(URL_UPDATE_USER_INFO_POST.replace("{session}", mMe.getCookie()), user, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo>() {
                }.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }*/

    public void updateUserInfoPost (Map<String, String> map, final RequestCallback callback) {
        if (mMe == null) {
            return;
        }
        map.put("userId", mMe.userId + "");

        JsonObject json = new JsonObject();
        Set<String> set = map.keySet();
        for (String key : set) {
            String value = map.get(key);
            json.addProperty(key, value);
            /*if (!Patterns.WEB_URL.matcher(value).matches()) {
                value = URLEncoder.encode(value);
            }
            try {
                json.addProperty(key, URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("param", json.toString());
        NetProxy.getInstance(getContext()).doPostRequest(URL_UPDATE_USER_INFO_POST.replace("{session}", mMe.getCookie()), params, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, final String requestId) {
                ReturnInfo returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo>() {
                }.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
                final int length = mUserChangeListenerList.size();
                if (length > 0) {
                    UserManager.getInstance(getContext()).getUserInfo(mMe.userId, new RequestCallback<User>() {
                        @Override
                        public void callback(ReturnInfo<User> returnInfo, String callbackId) {
                            if (returnInfo.isSuccess()) {
                                mMe.mergeFrom(returnInfo.getData());
                                for (int i = 0 ;i < length; i++) {
                                    mUserChangeListenerList.get(i).onUserInfoChanged(mMe);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateUserInfo (Map<String, String> map, final RequestCallback callback) {
        if (mMe == null) {
            //TODO
            return;
        }
        JsonObject json = new JsonObject();
        json.addProperty("userId", mMe.userId);
        Set<String> set = map.keySet();
        for (String key : set) {
            String value = map.get(key);
            if (!Patterns.WEB_URL.matcher(value).matches()) {
                value = URLEncoder.encode(value);
            }
            try {
                json.addProperty(key, URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        //builder.setCharAt(builder.length() - 1, '}');

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
        public void onUserInfoChanged (User user);
        public void onUserLogout();
    }

    public class LoginInfo {
        private String loginType = "";

        public LoginInfo (int typeId) {
            switch (typeId) {
                case AUTH_TYPE_NONE:
                    loginType = "none";

                    break;
                case AUTH_TYPE_WE_CHAT:
                    loginType = Wechat.NAME;

                    break;
                case AUTH_TYPE_SINA:
                    loginType = SinaWeibo.NAME;

                    break;
                case AUTH_TYPE_QQ:
                    loginType = QQ.NAME;
                    break;
            }
        }

        public LoginInfo (String login) {
            loginType = login;
        }

        public String getLoginType() {
            return loginType;
        }

        public void setLoginType(String loginType) {
            this.loginType = loginType;
        }
    }
}

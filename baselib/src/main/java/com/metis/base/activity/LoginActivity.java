package com.metis.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.base.ActivityDispatcher;
import com.metis.base.R;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.manager.ShareManager;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.base.utils.SystemUtils;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends TitleBarActivity implements PlatformActionListener, View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private TextView mLoginBtn = null;
    private EditText mAccountEt = null;
    private EditText mPwdEt = null;
    private TextView mPwdFindTv = null;
    private View mWeChatView, mSinaView, mQqView;

    private boolean isAlreadyIn = false;

    /*private RequestCallback<User> mAuthLoginCallback = new RequestCallback<User>() {
        @Override
        public void callback(ReturnInfo<User> returnInfo, String callbackId) {
            if (returnInfo.isSuccess()) {
                Log.v(TAG, "authLogin.user=" + returnInfo.getData().name);
            }
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        isAlreadyIn = getIntent ().getBooleanExtra(com.metis.base.ActivityDispatcher.KEY_STATUS, false);

        getTitleBar().setOnRightBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, RegisterActivity.class);
                it.putExtra(ActivityDispatcher.KEY_STATUS, isAlreadyIn);
                startActivity(it);
            }
        });

        mLoginBtn = (TextView)findViewById(R.id.login_btn);
        mAccountEt = (EditText)findViewById(R.id.login_account);
        mPwdEt = (EditText)findViewById(R.id.login_pwd);
        mPwdFindTv = (TextView)findViewById(R.id.login_find_pwd);

        mWeChatView = findViewById(R.id.login_auth_we_chat);
        mSinaView = findViewById(R.id.login_auth_sina);
        mQqView = findViewById(R.id.login_auth_qq);

        mLoginBtn.setOnClickListener(this);
        mWeChatView.setOnClickListener(this);
        mSinaView.setOnClickListener(this);
        mQqView.setOnClickListener(this);
    }

    @Override
    public CharSequence getTitleRight() {
        return getString(R.string.title_activity_register);
    }

    @Override
    public void onClick (View view) {
        final int id = view.getId();
        if (id == mLoginBtn.getId()) {
            String account = mAccountEt.getText().toString();
            if (TextUtils.isEmpty(account)) {
                Toast.makeText(this, R.string.toast_login_empty_account, Toast.LENGTH_SHORT).show();
                mAccountEt.requestFocus();
                SystemUtils.showIME(this, mAccountEt);
                return;
            }
            String pwd = mPwdEt.getText().toString();
            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(this, R.string.toast_login_empty_pwd, Toast.LENGTH_SHORT).show();
                mPwdEt.requestFocus();
                SystemUtils.showIME(this, mPwdEt);
                return;
            }
            SystemUtils.hideIME(this, mPwdEt);
            mLoginBtn.setEnabled(false);
            AccountManager.getInstance(this).login(account, pwd, new RequestCallback<User>() {
                @Override
                public void callback(ReturnInfo<User> returnInfo, String callbackId) {
                    dismissProgressDialog();
                    if (returnInfo.isSuccess()) {
                        User me = returnInfo.getData();
                        if (isAlreadyIn) {
                            if (me.userRole == 0) {
                                ActivityDispatcher.userRoleActivity(LoginActivity.this, me, isAlreadyIn);
                            }
                        } else {
                            if (me.userRole == 0) {
                                ActivityDispatcher.userRoleActivity(LoginActivity.this, me, isAlreadyIn);
                            } else {
                                ActivityDispatcher.mainActivity(LoginActivity.this);
                            }
                        }
                        Intent data = new Intent();
                        setResult(RESULT_OK, data);
                        finish();
                        /*if (me.userRole == 0) {
                            it = new Intent(LoginActivity.this, RoleChooseActivity.class);
                        } else {
                            it = new Intent(LoginActivity.this, MainActivity.class);
                        }*/

                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.toast_login_failed, returnInfo.getMessage()), Toast.LENGTH_SHORT).show();
                        mLoginBtn.setEnabled(true);
                    }
                }
            });
        } else if (id == mPwdFindTv.getId()) {
            ActivityDispatcher.resetPwdActivity(this);
        } else if (id == mWeChatView.getId()) {
            ShareManager.getInstance(this).loginAccess(ShareSDK.getPlatform(this, Wechat.NAME), this);
        } else if (id == mSinaView.getId()) {
            /*ActivityDispatcher.mainActivity(this);
            finish();*/
            ShareManager.getInstance(this).loginAccess(ShareSDK.getPlatform(this, SinaWeibo.NAME), this);
        } else if (id == mQqView.getId()) {
            ShareManager.getInstance(this).loginAccess(ShareSDK.getPlatform(this, QQ.NAME), this);
        }
        showProgressDialog(R.string.text_please_wait, false);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        dismissProgressDialog();
        if (platform.isValid()) {
            PlatformDb db = platform.getDb();
            final String userId = db.getUserId();
            final String profile = db.getUserIcon();
            final String name = db.getUserName();
            Log.v(TAG, "loginAccess name=" + name + " userId=" + userId + " profile=" + profile + " platform.name=" + platform.getName());
            final String platformName = platform.getName();
            RequestCallback<User> authLoginCallback = new RequestCallback<User>() {
                @Override
                public void callback(ReturnInfo<User> returnInfo, String callbackId) {
                    if (returnInfo.isSuccess()) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("userNickName", name);
                        map.put("userAvatar", profile);
                        AccountManager.getInstance(LoginActivity.this).updateUserInfo(map, null);
                        User me = returnInfo.getData();
                        if (me.userRole == 0) {
                            ActivityDispatcher.userRoleActivity(LoginActivity.this, me, isAlreadyIn);
                        } else {
                            if (isAlreadyIn) {
                                Intent data = new Intent();
                                setResult(RESULT_OK, data);
                            } else {
                                ActivityDispatcher.mainActivity(LoginActivity.this);
                            }
                        }
                        finish();
                    }

                }
            };
            if (Wechat.NAME.equals(platformName)) {
                AccountManager.getInstance(this).authLogin(userId, AccountManager.AUTH_TYPE_WE_CHAT, authLoginCallback);
            } else if (SinaWeibo.NAME.equals(platformName)) {
                AccountManager.getInstance(this).authLogin(userId, AccountManager.AUTH_TYPE_SINA, authLoginCallback);
            } else if (QQ.NAME.equals(platformName)) {
                AccountManager.getInstance(this).authLogin(userId, AccountManager.AUTH_TYPE_QQ, authLoginCallback);
            }
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        dismissProgressDialog();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        dismissProgressDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "onActivityResult " + data, Toast.LENGTH_SHORT).show();
    }
}

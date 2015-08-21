package com.metis.meishuquan.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.base.activity.TitleBarActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.User;
import com.metis.base.utils.SystemUtils;
import com.metis.meishuquan.R;
import com.metis.msnetworklib.contract.ReturnInfo;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class LoginActivity extends TitleBarActivity {

    @InjectView(R.id.login_btn)
    TextView mLoginBtn = null;
    @InjectView(R.id.login_account)
    EditText mAccountEt = null;
    @InjectView(R.id.login_pwd)
    EditText mPwdEt = null;
    @InjectView(R.id.login_find_pwd)
    TextView mPwdFindTv = null;
    @InjectViews({R.id.login_auth_we_chat, R.id.login_auth_sina, R.id.login_auth_qq})
    View[] mAuthBtns = new View[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getTitleBar().setOnRightBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(it);
            }
        });

        ButterKnife.inject(this);

    }

    @Override
    public CharSequence getTitleRight() {
        return getString(R.string.title_activity_register);
    }

    @OnClick({R.id.login_btn, R.id.login_find_pwd, R.id.login_auth_we_chat, R.id.login_auth_sina, R.id.login_auth_qq})
    protected void onClick (View view) {
        final int id = view.getId();
        switch (id) {
            case R.id.login_btn:
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
                        if (returnInfo.isSuccess()) {
                            Intent it = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(it);
                            finish();
                        } else {
                            mLoginBtn.setEnabled(true);
                        }
                    }
                });
                break;
            case R.id.login_find_pwd:
                break;
            case R.id.login_auth_we_chat:
                break;
            case R.id.login_auth_sina:
                break;
            case R.id.login_auth_qq:
                break;
        }
    }
}

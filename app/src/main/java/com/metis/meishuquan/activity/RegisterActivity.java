package com.metis.meishuquan.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.SystemClock;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.base.activity.TitleBarActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.utils.Log;
import com.metis.base.utils.SystemUtils;
import com.metis.base.utils.TimeUtils;
import com.metis.meishuquan.ActivityDispatcher;
import com.metis.meishuquan.R;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterActivity extends TitleBarActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    public static final int MODE_REGISTER = 0, MODE_RESET_PWD = 1;

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @InjectView(R.id.register_btn)
    TextView mRegisterBtn;
    @InjectView(R.id.register_get_code)
    TextView mGetCodeBtn;
    @InjectView(R.id.register_phone)
    EditText mPhoneEt;
    @InjectView(R.id.register_code)
    EditText mCodeEt;
    @InjectView(R.id.register_pwd)
    EditText mPwdEt;
    @InjectView(R.id.register_pwd_confirm)
    EditText mPwdConfirmEt;
    @InjectView(R.id.register_agreement_container)
    RelativeLayout mAgreeContainer;
    @InjectView(R.id.register_agreement)
    CheckBox mAgreeCb;
    @InjectView(R.id.register_agreement_title)
    TextView mAgreeTitleTv;

    private String mAgreementStr = null;

    private String mCode = null;
    private long mStartTime = 0;
    private DecimalFormat mFormat = new DecimalFormat("00");
    private Runnable mTimerRunnable = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            long delay = now - mStartTime;
            long seconds = (TimeUtils.MINUTE_LONG - delay) / 1000;
            mGetCodeBtn.setText(mFormat.format(seconds));
            if (delay < TimeUtils.MINUTE_LONG) {
                mGetCodeBtn.postDelayed(this, 1000);
            } else {
                mGetCodeBtn.setText(R.string.text_get_register_code);
                mGetCodeBtn.setEnabled(true);
            }
        }
    };

    private boolean mSmsReceiverRegistered = false;
    private BroadcastReceiver mSmsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            SmsMessage msg = null;
            if (null != bundle) {
                Object[] smsObj = (Object[]) bundle.get("pdus");
                for (Object object : smsObj) {
                    msg = SmsMessage.createFromPdu((byte[]) object);
                    if (msg == null) {
                        continue;
                    }
                    String content = msg.getMessageBody();
                    if (mCode != null && content != null) {
                        int index = content.indexOf(mCode);
                        if (index >= 0) {
                            unregisterSmsReceiver();
                            mGetCodeBtn.removeCallbacks(mTimerRunnable);
                            mGetCodeBtn.setEnabled(true);
                            mGetCodeBtn.setText(R.string.text_get_register_code);
                            mCodeEt.setText(mCode);
                            return;
                        }
                    }
                }
            }
        }

    };

    private int mMode = MODE_REGISTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mMode = getIntent().getIntExtra(ActivityDispatcher.KEY_MODE, MODE_REGISTER);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.inject(this);

        if (isResetMode()) {
            mRegisterBtn.setText(R.string.title_activity_reset_pwd);
        }
        mAgreeContainer.setVisibility(isResetMode() ? View.GONE : View.VISIBLE);
        mAgreeTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAgreementStr == null) {
                    AssetManager assetManager = getAssets();
                    try {
                        InputStream is = assetManager.open("agreement");
                        InputStreamReader reader = new InputStreamReader(is);
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        String line = null;
                        StringBuilder builder = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            builder.append(line + "\n");
                        }
                        mAgreementStr = builder.toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                final AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                        .setMessage(mAgreementStr)
                        .setPositiveButton(R.string.btn_agree, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
    }

    private boolean isResetMode () {
        return mMode == MODE_RESET_PWD;
    }

    @Override
    public CharSequence getTitleCenter() {
        if (isResetMode()) {
            return getString(R.string.title_activity_reset_pwd);
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterSmsReceiver();
    }

    private void registerSmsReceiver () {
        if (mSmsReceiverRegistered) {
            return;
        }
        IntentFilter filter = new IntentFilter(SMS_RECEIVED_ACTION);
        registerReceiver(mSmsReceiver, filter);
        mSmsReceiverRegistered = true;
    }

    private void unregisterSmsReceiver () {
        if (!mSmsReceiverRegistered) {
            return;
        }
        unregisterReceiver(mSmsReceiver);
        mSmsReceiverRegistered = false;
    }

    @OnClick({R.id.register_btn, R.id.register_get_code})
    protected void onClick (View v) {
        final String phone = mPhoneEt.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, R.string.toast_login_empty_account, Toast.LENGTH_SHORT).show();
            mPhoneEt.requestFocus();
            SystemUtils.showIME(this, mPhoneEt);
            return;
        }
        final int id = v.getId();
        switch (id) {
            case R.id.register_btn:
                final String code = mCodeEt.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(this, R.string.toast_register_empty_code, Toast.LENGTH_SHORT).show();
                    mCodeEt.requestFocus();
                    SystemUtils.showIME(this, mCodeEt);
                    return;
                }
                if (!code.equals(mCode)) {
                    Toast.makeText(this, R.string.toast_register_error_code, Toast.LENGTH_SHORT).show();
                    mCodeEt.setText("");
                    mCodeEt.requestFocus();
                    SystemUtils.showIME(this, mCodeEt);
                    return;
                }
                final String pwd = mPwdEt.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(this, R.string.toast_login_empty_pwd, Toast.LENGTH_SHORT).show();
                    mPwdEt.requestFocus();
                    SystemUtils.showIME(this, mPwdEt);
                    return;
                }
                final String pwdConfirm = mPwdConfirmEt.getText().toString();
                if (TextUtils.isEmpty(pwdConfirm)) {
                    Toast.makeText(this, R.string.toast_register_empty_pwd_confirm, Toast.LENGTH_SHORT).show();
                    mPwdConfirmEt.requestFocus();
                    SystemUtils.showIME(this, mPwdConfirmEt);
                    return;
                }
                if (!pwd.equals(pwdConfirm)) {
                    Toast.makeText(this, R.string.toast_register_empty_pwd_cannot_confirm, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mAgreeCb.isChecked()) {
                    Toast.makeText(this, R.string.toast_register_agreement_not_checked, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isResetMode()) {
                    AccountManager.getInstance(this).resetPwd(phone, code, pwd, new RequestCallback() {
                        @Override
                        public void callback(ReturnInfo returnInfo, String callbackId) {
                            if (returnInfo.isSuccess()) {
                                Toast.makeText(RegisterActivity.this, R.string.toast_reset_pwd_success, Toast.LENGTH_SHORT).show();
                                ActivityDispatcher.loginActivity(RegisterActivity.this);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, getString(R.string.toast_reset_pwd_failed, returnInfo.getMessage()), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //TODO
                } else {
                    //TODO
                }
                //AccountManager.getInstance(this).register(phone, code, pwd, );
                break;
            case R.id.register_get_code:
                mGetCodeBtn.setEnabled(false);
                AccountManager.getInstance(this).getRequestCode(phone, isResetMode() ? AccountManager.RequestCodeTypeEnum.RESET_PWD : AccountManager.RequestCodeTypeEnum.REGISTER, new RequestCallback<Integer>() {
                    @Override
                    public void callback(ReturnInfo<Integer> returnInfo, String callbackId) {
                        mGetCodeBtn.setEnabled(!returnInfo.isSuccess());
                        if (returnInfo.isSuccess()) {
                            Toast.makeText(RegisterActivity.this, R.string.toast_register_code_requested, Toast.LENGTH_SHORT).show();
                            mCode = returnInfo.getData().intValue() + "";
                            mStartTime = System.currentTimeMillis();
                            mGetCodeBtn.post(mTimerRunnable);
                            registerSmsReceiver();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }
}

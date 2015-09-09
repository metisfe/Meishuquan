package com.metis.base.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.base.ActivityDispatcher;
import com.metis.base.R;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.base.utils.SystemUtils;
import com.metis.base.utils.TimeUtils;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends TitleBarActivity implements View.OnClickListener{

    private static final String TAG = RegisterActivity.class.getSimpleName();

    public static final int MODE_REGISTER = 0, MODE_RESET_PWD = 1;

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    private TextView mRegisterBtn;
    private TextView mGetCodeBtn;
    private EditText mPhoneEt;
    private EditText mCodeEt;
    private EditText mPwdEt;
    private EditText mPwdConfirmEt;
    private RelativeLayout mAgreeContainer;
    private CheckBox mAgreeCb;
    private TextView mAgreeTitleTv;

    private String mAgreementStr = null;

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
                    Pattern pattern = Pattern.compile("\\d{4}");
                    Matcher matcher = pattern.matcher(content);
                    if (matcher.find()) {
                        mCodeEt.setText(matcher.group());
                        mGetCodeBtn.removeCallbacks(mTimerRunnable);
                        mGetCodeBtn.setEnabled(true);
                        mGetCodeBtn.setText(R.string.text_get_register_code);
                    }
                    /*if (mCode != null && content != null) {
                        int index = content.indexOf(mCode);
                        if (index >= 0) {
                            unregisterSmsReceiver();
                            mGetCodeBtn.removeCallbacks(mTimerRunnable);
                            mGetCodeBtn.setEnabled(true);
                            mGetCodeBtn.setText(R.string.text_get_register_code);
                            mCodeEt.setText(mCode);
                            return;
                        }
                    }*/
                }
            }
        }

    };

    private int mMode = MODE_REGISTER;

    private String mPhoneNum = null, mPwd = null;

    private boolean isAlreadyIn = false;

    private EventHandler mHandler = new EventHandler() {
        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.v(TAG, "afterEvent " + " event=" + event + " result=" + result + " data=" + data);
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //回调完成
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //提交验证码成功
                            if (isResetMode()) {
                                AccountManager.getInstance(RegisterActivity.this).resetPwd(mPhoneNum, "12345", mPwd, new RequestCallback() {
                                    @Override
                                    public void callback(ReturnInfo returnInfo, String callbackId) {
                                        if (returnInfo.isSuccess()) {
                                            Toast.makeText(RegisterActivity.this, R.string.toast_reset_pwd_success, Toast.LENGTH_SHORT).show();
                                            com.metis.base.ActivityDispatcher.loginActivity(RegisterActivity.this);
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, getString(R.string.toast_reset_pwd_failed, returnInfo.getMessage()), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                //TODO
                            } else {
                                AccountManager.getInstance(RegisterActivity.this).register(mPhoneNum, "12345", mPwd, 0, new RequestCallback<User>() {
                                    @Override
                                    public void callback(ReturnInfo<User> returnInfo, String callbackId) {
                                        if (returnInfo.isSuccess()) {
                                            Toast.makeText(RegisterActivity.this, R.string.toast_register_success, Toast.LENGTH_SHORT).show();
                                            User me = returnInfo.getData();
                                            if (me.userRole == 0) {
                                                ActivityDispatcher.userRoleActivity(RegisterActivity.this, me, isAlreadyIn);
                                            } else {
                                                if (isAlreadyIn) {
                                                    Intent data = new Intent();
                                                    setResult(RESULT_OK, data);
                                                } else {
                                                    ActivityDispatcher.mainActivity(RegisterActivity.this);
                                                }
                                            }
                                            finish();
                                            //TODO
                                        } else {
                                            Toast.makeText(RegisterActivity.this, getString(R.string.toast_register_failed, returnInfo.getMessage()), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                //TODO
                            }
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //获取验证码成功
                            unregisterSmsReceiver();
                            Toast.makeText(RegisterActivity.this, R.string.toast_register_code_requested, Toast.LENGTH_SHORT).show();
                            mStartTime = System.currentTimeMillis();
                            mGetCodeBtn.post(mTimerRunnable);
                            registerSmsReceiver();
                        } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            //返回支持发送验证码的国家列表
                        }
                    } else {
                        ((Throwable) data).printStackTrace();
                        try {
                            SmsException exception = new Gson().fromJson(((Throwable) data).getMessage(), new TypeToken<SmsException>(){}.getType());
                            switch (exception.status) {
                                case SmsException.ERROR_CODE_SUBMIT_TOO_SOON_467:
                                    Toast.makeText(RegisterActivity.this, R.string.toast_verify_too_soon, Toast.LENGTH_SHORT).show();
                                    break;
                                case SmsException.ERROR_CODE_VERIFY_CODE_NOT_MATCH_468:
                                    Toast.makeText(RegisterActivity.this, R.string.toast_verify_not_match, Toast.LENGTH_SHORT).show();
                                    break;
                                case SmsException.ERROR_CODE_PHONE_ILLEGAL_FORMAT_457:
                                    Toast.makeText(RegisterActivity.this, R.string.toast_verify_phone_illegal_format, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //提交验证码失败
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //获取验证码失败
                            mGetCodeBtn.setEnabled(true);
                        } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            //返回支持发送验证码的国家列表
                        }
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mMode = getIntent().getIntExtra(ActivityDispatcher.KEY_MODE, MODE_REGISTER);
        isAlreadyIn = getIntent().getBooleanExtra(ActivityDispatcher.KEY_STATUS, false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegisterBtn = (TextView)findViewById(R.id.register_btn);
        mGetCodeBtn = (TextView)findViewById(R.id.register_get_code);
        mPhoneEt = (EditText)findViewById(R.id.register_phone);
        mCodeEt = (EditText)findViewById(R.id.register_code);
        mPwdEt = (EditText)findViewById(R.id.register_pwd);
        mPwdConfirmEt = (EditText)findViewById(R.id.register_pwd_confirm);
        mAgreeContainer = (RelativeLayout)findViewById(R.id.register_agreement_container);
        mAgreeCb = (CheckBox)findViewById(R.id.register_agreement);
        mAgreeTitleTv = (TextView)findViewById(R.id.register_agreement_title);

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

        mRegisterBtn.setOnClickListener(this);
        mGetCodeBtn.setOnClickListener(this);

        AccountManager.getInstance(this).registerEventHandler(mHandler);
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
        AccountManager.getInstance(this).unregisterEventHandler(mHandler);
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
    @Override
    public void onClick (View v) {
        final String phone = mPhoneEt.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, R.string.toast_login_empty_account, Toast.LENGTH_SHORT).show();
            mPhoneEt.requestFocus();
            SystemUtils.showIME(this, mPhoneEt);
            return;
        }
        final int id = v.getId();
        if (id == mRegisterBtn.getId()) {
            final String code = mCodeEt.getText().toString();
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(this, R.string.toast_register_empty_code, Toast.LENGTH_SHORT).show();
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
            mPhoneNum = phone;
            mPwd = pwd;
            AccountManager.getInstance(this).submitVerificationCode(phone, code);
        } else if (id == mGetCodeBtn.getId()) {
            mGetCodeBtn.setEnabled(false);
            AccountManager.getInstance(this).askForSms(phone);
        }
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    private class SmsException implements Serializable {

        public static final int
        ERROR_CODE_APP_KEY_EMPTY_405 = 405,
        ERROR_CODE_APP_KEY_ILLEGAL_406 = 406,
        ERROR_CODE_COUNTRY_CODE_OR_PHONE_EMPTY_456 = 456,
        ERROR_CODE_PHONE_ILLEGAL_FORMAT_457 = 457,
        ERROR_CODE_VERIFY_CODE_EMPTY_466 = 466,
        ERROR_CODE_SUBMIT_TOO_SOON_467 = 467,
        ERROR_CODE_VERIFY_CODE_NOT_MATCH_468 = 468,
        ERROR_CODE_NO_VERIFY_SERVICE_474 = 474;


        public String detail;
        public int status;
        public String description;
    }
}

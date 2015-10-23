package com.metis.base.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.AnimRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

import com.metis.base.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/21.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private boolean isAlive = false;

    private ProgressDialog mProgressDialog = null;

    private ActivityResultListener mActivityResultListener = null;
    private List<OnBackPressListener> mOnBackPressListenerList = new ArrayList<OnBackPressListener>();

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        isAlive = true;

        PushAgent.getInstance(this).onAppStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAlive = false;
    }

    public @AnimRes int getEnterAnimation () {
        return R.anim.fade_in;
    }

    public @AnimRes int getExitAnimation () {
        return R.anim.fade_out;
    }

    public boolean isAlive () {
        return isAlive;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(getEnterAnimation(), getExitAnimation());
    }

    public void showProgressDialog (boolean cancelable) {
        showProgressDialog(null, cancelable);
    }

    public void showProgressDialog (@StringRes int msgRes, boolean cancelable) {
        showProgressDialog(getString(msgRes), cancelable);
    }

    public void showProgressDialog (CharSequence msg, boolean cancelable) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            dismissProgressDialog();
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setMessage(msg);
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog () {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public /*final */void onBackPressed() {
        final int mPressLength = mOnBackPressListenerList.size();
        if (mPressLength > 0) {
            for (int i = mPressLength - 1; i >= 0; i--) {
                OnBackPressListener listener = mOnBackPressListenerList.get(i);
                if (listener.onBackPressedReceived()) {
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(getEnterAnimation(), getExitAnimation());
    }

    public void startActivityForResult (Intent intent, int requestCode, Bundle options, ActivityResultListener listener) {
        this.mActivityResultListener = listener;
        startActivityForResult(intent, requestCode, options);
    }

    public void startActivityForResult (Intent intent, int requestCode, ActivityResultListener listener) {
        this.mActivityResultListener = listener;
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultListener listener = mActivityResultListener;
        mActivityResultListener = null;
        if (listener != null) {
            listener.onActivityResultReceived(requestCode, resultCode, data);
            //mActivityResultListener = null;
        }
    }

    public void setActivityResultListener (ActivityResultListener listener) {
        mActivityResultListener = listener;
    }

    public void registerOnBackPressListener (OnBackPressListener listener) {
        if (listener == null || mOnBackPressListenerList.contains(listener)) {
            return;
        }
        mOnBackPressListenerList.add(listener);
    }

    public void unregisterOnBackPressListener (OnBackPressListener listener) {
        if (listener == null || !mOnBackPressListenerList.contains(listener)) {
            return;
        }
        mOnBackPressListenerList.remove(listener);
    }

    public interface ActivityResultListener {
        public void onActivityResultReceived (int requestCode, int resultCode, Intent data);
    }

    public interface OnBackPressListener {
        public boolean onBackPressedReceived();
    }
}

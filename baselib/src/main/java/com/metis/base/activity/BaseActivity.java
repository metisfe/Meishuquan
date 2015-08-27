package com.metis.base.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.AnimRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

import com.metis.base.R;
import com.metis.base.utils.Log;

/**
 * Created by Beak on 2015/7/21.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private boolean isAlive = false;

    private ProgressDialog mProgressDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        isAlive = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(getEnterAnimation(), getExitAnimation());
    }
}

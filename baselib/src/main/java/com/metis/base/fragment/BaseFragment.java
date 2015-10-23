package com.metis.base.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.metis.base.activity.BaseActivity;
import com.metis.base.utils.Log;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Beak on 2015/7/2.
 */
public class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    private boolean isAlive = false;

    private int mLogStackSize = 0;

    private boolean isTakeControl = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAllowEnterTransitionOverlap(true);
        setAllowReturnTransitionOverlap(true);
        isAlive = true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(TAG, "LifeCycle onViewCreated " + getClass().getSimpleName() + " tag=" + getCustomTag());
        /*if (getCustomTag() != null) {
            onPageStart();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getCustomTag());
        Log.v(TAG, "LifeCycle onResume " + getClass().getSimpleName() + " tag=" + getCustomTag());
        if (getCustomTag() != null && !isHidden() && !isTakeControl) {
            onPageStart();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getCustomTag());
        Log.v(TAG, "LifeCycle onPause " + getClass().getSimpleName() + " tag=" + getCustomTag());
        if (getCustomTag() != null && !isHidden() && !isTakeControl) {
            onPageEnd();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*if (getCustomTag() != null) {
            onPageEnd();
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isAlive = false;
        Log.d(TAG, "BaseFragment onDestroy mLogStackSize=" + mLogStackSize);
    }

    public String getCustomTag () {
        return null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (getCustomTag() != null && !isTakeControl) {
            if (!hidden) {
                onPageStart();
            } else {
                onPageEnd();
            }
        }
    }

    protected void setTakeControl (boolean control) {
        isTakeControl = control;
    }

    protected void onPageStart () {
        /*if (mLogStackSize == 0) {
            MobclickAgent.onPageStart(getCustomTag());
            mLogStackSize++;
            Log.v(TAG, "onPageStart " + getClass().getSimpleName() + " tag=" + getCustomTag());
        } else {
            Log.w(TAG, "onPageStart " + getClass().getSimpleName() + " tag=" + getCustomTag() + " try to do but mLogStackSize=" + mLogStackSize);
        }*/
    }

    protected void onPageEnd () {
        /*if (mLogStackSize == 1) {
            MobclickAgent.onPageEnd(getCustomTag());
            mLogStackSize--;
            Log.v(TAG, "onPageEnd " + getClass().getSimpleName() + " tag=" + getCustomTag());
        } else {
            Log.w(TAG, "onPageEnd " + getClass().getSimpleName() + " tag=" + getCustomTag() + " try to do but mLogStackSize=" + mLogStackSize);
        }*/

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode, BaseActivity.ActivityResultListener listener) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).startActivityForResult(intent, requestCode, listener);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    public boolean isAlive () {
        return isAlive;
    }
}

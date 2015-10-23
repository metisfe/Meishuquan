package com.metis.meishuquan.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.metis.base.activity.BaseActivity;
import com.metis.base.fragment.MeTabFragment;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.WeChatPayManager;
import com.metis.base.module.User;
import com.metis.base.push.PushMessageHandler;
import com.metis.base.push.PushMessageService;
import com.metis.base.utils.FragmentUtils;
import com.metis.base.utils.Log;
import com.metis.base.widget.dock.DockBar;
import com.metis.commentpart.fragment.CommentTabFragment;
import com.metis.coursepart.fragment.CourseTabFragment;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.DiscoveryTabFragment;
import com.metis.newslib.fragment.NewsTabFragment;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity implements
        DockBar.OnDockItemClickListener, BaseActivity.OnBackPressListener,
        AccountManager.OnUserChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private NewsTabFragment mNewsFragment = new NewsTabFragment();
    private CommentTabFragment mCommentFragment = new CommentTabFragment();
    private CourseTabFragment mCourseFragment = new CourseTabFragment();
    private DiscoveryTabFragment mDiscoveryFragment = new DiscoveryTabFragment();
    private MeTabFragment mMeFragment = new MeTabFragment();

    private Fragment mCurrentFragment = null;

    @InjectView(R.id.main_dock_bar)
    DockBar mMainDockBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            Log.v(TAG, "fragmentList.size=" + fragmentList.size());
        } else {
            Log.v(TAG, "fragmentList=null");
        }*/

        ButterKnife.inject(this);

        //UmengUpdateAgent.silentUpdate(this);
        UmengUpdateAgent.setUpdateOnlyWifi(true);
        UmengUpdateAgent.update(this);

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        mPushAgent.setMessageHandler(PushMessageHandler.getInstance());
        mPushAgent.setPushIntentServiceClass(PushMessageService.class);

        AccountManager.getInstance(this).registerOnUserChangeListener(this);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null && fragmentList.size() > 0) {
            final int length = fragmentList.size();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            for (int i = 0; i < length; i++) {
                ft.remove(fragmentList.get(i));
            }
            ft.commit();
            Log.v(TAG, "onPostCreate fragmentList.size=" + fragmentList.size());
        } else {
            Log.v(TAG, "onPostCreate fragmentList=" + fragmentList);
        }

        Log.v(TAG, "onPostCreate DockBar.size = " + mMainDockBar.size());
        if (mMainDockBar.size() == 0) {
            mMainDockBar.addDock(mNewsFragment.getDock(this));
            mMainDockBar.addDock(mCommentFragment.getDock(this));
            mMainDockBar.addDock(mCourseFragment.getDock(this));
            mMainDockBar.addDock(mDiscoveryFragment.getDock(this));
            mMainDockBar.addDock(mMeFragment.getDock(this));

            mMainDockBar.setOnDockItemClickListener(this);
            if (mCurrentFragment == null) {
                mMainDockBar.selectDock(mCourseFragment.getDock(this));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerOnBackPressListener(this);
        UmengRegistrar.setDebug(this, true, true);
        String device_token = UmengRegistrar.getRegistrationId(this);

        Log.v(TAG, "device_token=" + device_token + "=");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterOnBackPressListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AccountManager.getInstance(this).unregisterOnUserChangeListener(this);
    }

    @Override
    public boolean onBackPressedReceived() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            moveTaskToBack(true);
            return true;
        }
        return false;
    }

    /*@Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            moveTaskToBack(true);
            return;
        }
        super.onBackPressed();
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        AccountManager.getInstance(this).onSaveInstanceState(outState);
        /*List<Fragment> fragments = getSupportFragmentManager().getFragments();
        final int length = fragments.size();
        for (int i = 0; i < length; i++) {
            FragmentUtils.removeFragment(getSupportFragmentManager(), fragments.get(i));
        }
        mCurrentFragment = null;*/
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        AccountManager.getInstance(this).onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onDockClick(View view, DockBar.Dock dock) {
        if (mCurrentFragment != null) {
            FragmentUtils.hideFragment(getSupportFragmentManager(), mCurrentFragment);
            mCurrentFragment = null;
        }
        FragmentUtils.showFragment(getSupportFragmentManager(), dock.target, R.id.main_fragment_container);
        mCurrentFragment = dock.target;
    }

    @Override
    public void onUserChanged(User user, boolean onLine) {

    }

    @Override
    public void onUserInfoChanged(User user) {

    }

    @Override
    public void onUserLogout() {
        finish();
    }

    /*private void showFragment (Fragment fragment) {
        if (fragment == null) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(R.id.main_fragment_container, fragment);
        }
        ft.commit();
        mCurrentFragment = fragment;
    }

    private void hideFragment (Fragment fragment) {
        if (fragment == null) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(fragment);
        ft.commit();
        mCurrentFragment = null;
    }*/

}

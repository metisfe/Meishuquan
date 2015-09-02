package com.metis.meishuquan.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.metis.base.activity.BaseActivity;
import com.metis.base.fragment.MeTabFragment;
import com.metis.base.utils.FragmentUtils;
import com.metis.base.widget.dock.DockBar;
import com.metis.commentpart.fragment.CommentTabFragment;
import com.metis.coursepart.fragment.CourseTabFragment;
import com.metis.meishuquan.R;
import com.metis.newslib.fragment.NewsTabFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity implements DockBar.OnDockItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private NewsTabFragment mNewsFragment = new NewsTabFragment();
    private CourseTabFragment mCourseFragment = new CourseTabFragment();
    private CommentTabFragment mCommentFragment = new CommentTabFragment();
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

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mMainDockBar.addDock(mNewsFragment.getDock(this));
        mMainDockBar.addDock(mCommentFragment.getDock(this));
        mMainDockBar.addDock(mCourseFragment.getDock(this));
        mMainDockBar.addDock(mMeFragment.getDock(this));

        mMainDockBar.setOnDockItemClickListener(this);
        if (mCurrentFragment == null) {
            mMainDockBar.selectDock(mNewsFragment.getDock(this));
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            moveTaskToBack(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*List<Fragment> fragments = getSupportFragmentManager().getFragments();
        final int length = fragments.size();
        for (int i = 0; i < length; i++) {
            FragmentUtils.removeFragment(getSupportFragmentManager(), fragments.get(i));
        }
        mCurrentFragment = null;*/
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

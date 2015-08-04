package com.metis.commentpart.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.metis.base.activity.TitleBarActivity;
import com.metis.base.utils.FragmentUtils;
import com.metis.base.widget.DoubleTab;
import com.metis.commentpart.R;
import com.metis.commentpart.fragment.TeacherListFragment;

public class TeacherListActivity extends TitleBarActivity implements DoubleTab.OnTabSwitchListener{

    private TeacherListFragment mMostFragment = new TeacherListFragment();
    private TeacherListFragment mBestFragment = new TeacherListFragment();

    private Fragment mCurrentFragment = null;

    private DoubleTab mDoubleTab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

        mMostFragment.setTeacherFilter(0);
        mBestFragment.setTeacherFilter(1);

        mDoubleTab = new DoubleTab(this);
        mDoubleTab.setFirstTabText(R.string.teacher_list_tab_most);
        mDoubleTab.setSecondTabText(R.string.teacher_list_tab_best);
        mDoubleTab.setOnTabSwitchListener(this);
        getTitleBar().setCenterView(mDoubleTab);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDoubleTab.select(0);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public void onSwitch(int index) {
        if (mCurrentFragment != null) {
            FragmentUtils.hideFragment(getSupportFragmentManager(), mCurrentFragment);
        }
        switch (index) {
            case DoubleTab.INDEX_FIRST:
                mCurrentFragment = mMostFragment;
                break;
            case DoubleTab.INDEX_SECOND:
                mCurrentFragment = mBestFragment;
                break;
        }
        FragmentUtils.showFragment(getSupportFragmentManager(), mCurrentFragment, R.id.teacher_list_fragment_container);
    }
}

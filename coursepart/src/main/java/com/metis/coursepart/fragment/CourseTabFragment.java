package com.metis.coursepart.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.metis.base.fragment.DockFragment;
import com.metis.base.utils.FragmentUtils;
import com.metis.base.widget.DoubleTab;
import com.metis.base.widget.TitleBar;
import com.metis.base.widget.dock.DockBar;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.activity.FilterActivity;
import com.metis.coursepart.manager.CourseManager;

import java.util.regex.Pattern;

/**
 * Created by Beak on 2015/7/2.
 */
public class CourseTabFragment extends DockFragment implements DoubleTab.OnTabSwitchListener{

    private static final String TAG = CourseTabFragment.class.getSimpleName();

    private static CourseTabFragment sFragment = new CourseTabFragment();

    public static CourseTabFragment getInstance () {
        return sFragment;
    }

    private DockBar.Dock mDock = null;
    private TitleBar mTitleBar = null;

    private CourseVideoFragment mVideoFragment = new CourseVideoFragment();
    private CourseGalleryFragment mGalleryFragment = new CourseGalleryFragment();
    private Fragment mCurrentFragment = null;

    private DoubleTab mDoubleTab = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_tab, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleBar = (TitleBar)view.findViewById(R.id.course_title_bar);

        mDoubleTab = new DoubleTab(getActivity());
        mDoubleTab.setFirstTabText(R.string.tab_video);
        mDoubleTab.setSecondTabText(R.string.tab_gallery);
        //RadioGroup switchView = (RadioGroup)LayoutInflater.from(getActivity()).inflate(R.layout.layout_tab_switch, null);
        mTitleBar.setCenterView(mDoubleTab);
        mTitleBar.setDrawableResourceLeft(R.drawable.ic_filter);
        mDoubleTab.setOnTabSwitchListener(this);
        mDoubleTab.select(0);

        mTitleBar.setOnLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentFragment == mVideoFragment) {
                    ActivityDispatcher.filterActivityForVideo(getActivity());
                } else if (mCurrentFragment == mGalleryFragment) {
                    ActivityDispatcher.filterActivityForGallery(getActivity());
                }
            }
        });
    }

    @Override
    public void onSwitch(int index) {
        if (mCurrentFragment != null) {
            FragmentUtils.hideFragment(getChildFragmentManager(), mCurrentFragment);
            mCurrentFragment = null;
        }

        switch (index) {
            case DoubleTab.INDEX_FIRST:
                mCurrentFragment = mVideoFragment;
                break;
            case DoubleTab.INDEX_SECOND:
                mCurrentFragment = mGalleryFragment;
                break;
        }
        FragmentUtils.showFragment(getChildFragmentManager(), mCurrentFragment, R.id.course_fragment_container);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if (mCurrentFragment != null) {
            FragmentUtils.removeFragment(getChildFragmentManager(), mCurrentFragment);
        }*/
    }

    @Override
    public DockBar.Dock getDock(Context context) {
        if (mDock == null) {
            mDock = new DockBar.Dock(context, 2, android.R.drawable.alert_dark_frame, R.string.tab_title_course, this);
        }
        return mDock;
    }

}

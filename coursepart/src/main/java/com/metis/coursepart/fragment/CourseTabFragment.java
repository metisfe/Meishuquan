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
import com.metis.base.widget.TitleBar;
import com.metis.base.widget.dock.DockBar;
import com.metis.coursepart.R;
import com.metis.coursepart.activity.FilterActivity;
import com.metis.coursepart.manager.CourseManager;

import java.util.regex.Pattern;

/**
 * Created by Beak on 2015/7/2.
 */
public class CourseTabFragment extends DockFragment {

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

        RadioGroup switchView = (RadioGroup)LayoutInflater.from(getActivity()).inflate(R.layout.layout_tab_switch, null);
        mTitleBar.setCenterView(switchView);

        switchView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mCurrentFragment != null) {
                    FragmentUtils.hideFragment(getFragmentManager(), mCurrentFragment);
                    mCurrentFragment = null;
                }
                if (checkedId == R.id.tab_video) {
                    mCurrentFragment = mVideoFragment;
                    mTitleBar.setTitleLeft(R.string.title_filter);
                } else if (checkedId == R.id.tab_gallery) {
                    mCurrentFragment = mGalleryFragment;
                    mTitleBar.setTitleLeft("");
                }
                FragmentUtils.showFragment(getFragmentManager(), mCurrentFragment, R.id.course_fragment_container);
            }
        });
        ((RadioButton)view.findViewById(R.id.tab_video)).setChecked(true);
        //FragmentUtils.showFragment(getFragmentManager(), mVideoFragment, R.id.course_fragment_container);
        //switchView.check(R.id.tab_video);
        //CourseManager.getInstance(getActivity()).getCourseChannelList(null);

        mTitleBar.setOnLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentFragment == mVideoFragment) {
                    Intent it = new Intent(getActivity(), FilterActivity.class);
                    startActivity(it);
                }
            }
        });
    }

    @Override
    public DockBar.Dock getDock(Context context) {
        if (mDock == null) {
            mDock = new DockBar.Dock(context, 1, android.R.drawable.alert_dark_frame, android.R.string.cancel, this);
        }
        return mDock;
    }
}

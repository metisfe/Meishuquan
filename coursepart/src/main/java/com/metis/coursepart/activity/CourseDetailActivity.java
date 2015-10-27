package com.metis.coursepart.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

import com.metis.base.activity.BaseActivity;
import com.metis.base.activity.TitleBarActivity;
import com.metis.base.fragment.AbsPagerFragment;
import com.metis.base.fragment.CcPlayFragment;
import com.metis.base.manager.PlayerManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.widget.VideoMaskView;
import com.metis.base.widget.ViewPagerTabGroup;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.CourseAdapter;
import com.metis.coursepart.adapter.delegate.CourseDelegate;
import com.metis.coursepart.fragment.CourseVideoChapterFragment;
import com.metis.coursepart.fragment.CourseVideoDetailFragment;
import com.metis.coursepart.fragment.CourseVideoDiscussFragment;
import com.metis.coursepart.manager.CourseManager;
import com.metis.coursepart.module.Course;
import com.metis.coursepart.module.CourseAlbum;
import com.metis.coursepart.module.CourseSubList;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.List;

public class CourseDetailActivity extends BaseActivity implements CourseAdapter.OnCourseClickListener {

    private FrameLayout mVideoLayout = null;
    private CcPlayFragment mCcFragment = null;
    private VideoMaskView mMaskView = null;

    private CourseVideoDetailFragment mDetailFragment = new CourseVideoDetailFragment();
    private CourseVideoChapterFragment mChapterFragment = new CourseVideoChapterFragment();
    private CourseVideoDiscussFragment mDiscussFragment = new CourseVideoDiscussFragment();

    private ViewPagerTabGroup mTabGroup = null;
    private ViewPager mVp = null;

    private Course mCurrentCourse = null;

    private AbsPagerFragment[] mFragmentArray = {
            mChapterFragment, mDetailFragment, mDiscussFragment
    };

    private CourseAlbum mAlbum = null;
    private List<Course> mCourseList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        mAlbum = (CourseAlbum)getIntent().getSerializableExtra(ActivityDispatcher.KEY_COURSE_ALBUM);

        mVideoLayout = (FrameLayout)findViewById(R.id.course_detail_video_container);
        mMaskView = (VideoMaskView)findViewById(R.id.course_detail_video_controller);
        mCcFragment = (CcPlayFragment)getSupportFragmentManager().findFragmentById(R.id.course_detail_video_fragment);

        mTabGroup = (ViewPagerTabGroup) findViewById(R.id.course_detail_view_pager_tabs);
        mVp = (ViewPager)findViewById(R.id.course_detail_view_pager);
        mVp.setOffscreenPageLimit(3);

        mVp.setAdapter(new TabAdapter(getSupportFragmentManager()));
        mTabGroup.setViewPager(mVp);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDetailFragment.setCourseAlbum(mAlbum);
        mChapterFragment.setOnCourseClickListener(this);
        CourseManager.getInstance(this).getCourseSubList(mAlbum.courseId, new RequestCallback<CourseSubList>() {
            @Override
            public void callback(ReturnInfo<CourseSubList> returnInfo, String callbackId) {
                if (returnInfo.isSuccess()) {
                    CourseSubList subList = returnInfo.getData();
                    if (subList != null) {
                        mCourseList = subList.courseSubList;
                        if (mCourseList.size() > 0) {
                            mChapterFragment.setSubCourseList(subList.courseSubList);
                            mDetailFragment.setCurrentCourse(mCourseList.get(0));
                        }
                    }
                }
            }
        });
        mCcFragment.getPlayerManager().registerPlayerCallback(mMaskView);
        mMaskView.setOnButtonListener(new VideoMaskView.OnButtonListener() {
            @Override
            public void onBackBtnPressed(@VideoMaskView.State int state) {
                if (mCcFragment.isFullScreen()) {
                    mCcFragment.setFullScreen(false);
                } else {
                    finish();
                }
            }

            @Override
            public void onPlayOrPauseBtnPressed(@VideoMaskView.State int state) {
                if (state == VideoMaskView.STATE_PAUSED) {
                    mCcFragment.getPlayerManager().resumePlay();
                } else if (state != VideoMaskView.STATE_STARTED) {
                    mCcFragment.getPlayerManager().startRemotePlayWithUrl(mCourseList.get(0).videoUrl);
                } else {
                    mCcFragment.getPlayerManager().pausePlay();
                }

            }

            @Override
            public void onFullScreenPressed(@VideoMaskView.State int state) {
                mCcFragment.setFullScreen(!mCcFragment.isFullScreen());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCcFragment.getPlayerManager().unregisterPlayerCallback(mMaskView);
    }

    @Override
    public void onCourseClick(CourseDelegate delegate) {
        Course course = delegate.getSource();
        if (mCurrentCourse != null && mCurrentCourse.equals(course)) {
            if (mCcFragment.getPlayerManager().isPlaying()) {
                mCcFragment.getPlayerManager().pausePlay();
            } else {
                mCcFragment.getPlayerManager().pausePlay();
            }
        } else {
            mDetailFragment.setCurrentCourse(course);
            mCcFragment.getPlayerManager().startRemotePlayWithUrl(course.videoUrl);
            mCurrentCourse = course;
        }

    }

    private class TabAdapter extends FragmentStatePagerAdapter {

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentArray[position];
        }

        @Override
        public int getCount() {
            return mFragmentArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentArray[position].getTitle(CourseDetailActivity.this);
        }
    }

}

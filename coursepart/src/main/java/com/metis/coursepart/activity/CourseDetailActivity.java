package com.metis.coursepart.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.metis.base.activity.BaseActivity;
import com.metis.base.fragment.AbsPagerFragment;
import com.metis.base.fragment.CcPlayFragment;
import com.metis.base.manager.RequestCallback;
import com.metis.base.widget.ViewPagerTabGroup;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.CourseAdapter;
import com.metis.coursepart.adapter.delegate.CourseDelegate;
import com.metis.coursepart.fragment.CcWrapperFragment;
import com.metis.coursepart.fragment.CourseVideoChapterFragment;
import com.metis.coursepart.fragment.CourseVideoDetailFragment;
import com.metis.coursepart.fragment.CourseVideoDiscussFragment;
import com.metis.coursepart.manager.CourseManager;
import com.metis.coursepart.module.Course;
import com.metis.coursepart.module.CourseAlbum;
import com.metis.coursepart.module.CourseSubList;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.List;

public class CourseDetailActivity extends BaseActivity implements CourseAdapter.OnCourseClickListener, CcPlayFragment.PlayerCallback, CcPlayFragment.OnFullScreenCallback {

    private FrameLayout mVideoLayout = null;
    private CcWrapperFragment mWrapperFragment = null;

    private CourseVideoDetailFragment mDetailFragment = new CourseVideoDetailFragment();
    private CourseVideoChapterFragment mChapterFragment = new CourseVideoChapterFragment();
    private CourseVideoDiscussFragment mDiscussFragment = new CourseVideoDiscussFragment();

    private ViewPagerTabGroup mTabGroup = null;
    private ViewPager mVp = null;

    private Course mCurrentCourse = null;

    private AbsPagerFragment[] mFragmentArray = {
            mChapterFragment, mDetailFragment/*, mDiscussFragment*/
    };

    private CourseAlbum mAlbum = null;
    private List<Course> mCourseList = null;

    private int orgWid, orgHei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        mAlbum = (CourseAlbum)getIntent().getSerializableExtra(ActivityDispatcher.KEY_COURSE_ALBUM);
        mDetailFragment.setCourseAlbum(mAlbum);

        mVideoLayout = (FrameLayout)findViewById(R.id.course_detail_video_container);
        mWrapperFragment = (CcWrapperFragment)getSupportFragmentManager().findFragmentById(R.id.course_detail_video_fragment);

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
        mWrapperFragment.setOnFullScreenCallback(this);
        mWrapperFragment.registerPlayerCallback(this);
        CourseManager.getInstance(this).getCourseSubList(mAlbum.courseId, new RequestCallback<CourseSubList>() {
            @Override
            public void callback(ReturnInfo<CourseSubList> returnInfo, String callbackId) {
                if (returnInfo.isSuccess()) {
                    CourseSubList subList = returnInfo.getData();
                    if (subList != null) {
                        mCourseList = subList.courseSubList;
                        if (mCourseList.size() > 0) {
                            Course firstCourse = mCourseList.get(0);
                            mChapterFragment.setSubCourseList(subList.courseSubList);
                            //mDetailFragment.setCurrentCourse(firstCourse);
                            mWrapperFragment.setCourse(firstCourse);
                            CourseManager.getInstance(CourseDetailActivity.this).getSubCourseDetail(firstCourse.subCourseId, new RequestCallback<Course>() {
                                @Override
                                public void callback(ReturnInfo<Course> returnInfo, String callbackId) {
                                    if (returnInfo.isSuccess()) {
                                        mDetailFragment.setCurrentCourse(returnInfo.getData());
                                    }
                                }
                            });
                        }
                        if (subList.relatedCourse != null && !subList.relatedCourse.isEmpty()) {
                            mDetailFragment.setRelatedCourses(subList.relatedCourse);
                        }
                    }
                }
            }
        });
        /*mMaskView.setOnButtonListener(new VideoMaskView.OnButtonListener() {
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
                } else if (state != VideoMaskView.STATE_STARTED) {
                    Course course = mCourseList.get(0);
                    mMaskView.setTitle(course.subCourseName);
                } else {
                }

            }

            @Override
            public void onFullScreenPressed(@VideoMaskView.State int state) {
                mCcFragment.setFullScreen(!mCcFragment.isFullScreen());
            }

            @Override
            public void onSeekStart(SeekBar seekBar) {
            }

            @Override
            public void onSeekStop(SeekBar seekBar) {
                int progress = seekBar.getProgress();
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        if (mWrapperFragment.isFullScreen()) {
            mWrapperFragment.setFullScreen(false);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWrapperFragment.unregisterPlayerCallback(this);
        //mCcFragment.setOnFullScreenCallback(null);
    }

    @Override
    public void onCourseClick(CourseDelegate delegate) {
        Course course = delegate.getSource();

        if (mCurrentCourse != null && mCurrentCourse.equals(course)) {
            mWrapperFragment.resumePlay();
        } else {
            mWrapperFragment.playCourse(course);
            //mDetailFragment.setCurrentCourse(course);
            mCurrentCourse = course;
            CourseManager.getInstance(CourseDetailActivity.this).getSubCourseDetail(course.subCourseId, new RequestCallback<Course>() {
                @Override
                public void callback(ReturnInfo<Course> returnInfo, String callbackId) {
                    if (returnInfo.isSuccess()) {
                        mDetailFragment.setCurrentCourse(returnInfo.getData());
                    }
                }
            });
        }

    }

    @Override
    public void onPlayerStarted() {

    }

    @Override
    public void onPlayerPrepared(int width, int height) {
        if (!mWrapperFragment.isFullScreen()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mVideoLayout.getLayoutParams();
            if (params == null) {
                params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            final int screenWid = getWindowManager().getDefaultDisplay().getWidth();
            final int targetHeight = (int)(screenWid * 1.0 / width * height);
            params.width = screenWid;
            params.height = targetHeight;
            mVideoLayout.setLayoutParams(params);
        }

    }

    @Override
    public void onPlayerPaused() {

    }

    @Override
    public void onPlayerResumed() {

    }

    @Override
    public void onPlayerStopped() {

    }

    @Override
    public void onPlayerCompleted() {

    }

    @Override
    public void onUpdateProgress(int current, int duration, int progress) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onFullScreen(boolean fullScreen) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mVideoLayout.getLayoutParams();
        if (params == null) {
            params = new RelativeLayout.LayoutParams (ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        }
        if (fullScreen) {
            orgWid = mVideoLayout.getWidth();
            orgHei = mVideoLayout.getHeight();

            params.width = ViewGroup.LayoutParams.FILL_PARENT;
            params.height = ViewGroup.LayoutParams.FILL_PARENT;

        } else {
            params.width = orgWid;
            params.height = orgHei;
        }
        mVideoLayout.setLayoutParams(params);
        //mMaskView.setFullScreen(fullScreen);
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

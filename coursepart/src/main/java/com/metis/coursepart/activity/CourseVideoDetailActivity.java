package com.metis.coursepart.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.cyberplayer.core.BVideoView;
import com.metis.base.manager.DisplayManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.utils.Log;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.CourseAdapter;
import com.metis.coursepart.adapter.delegate.CourseDelegate;
import com.metis.coursepart.fragment.CourseVideoChapterFragment;
import com.metis.coursepart.fragment.CourseVideoDetailFragment;
import com.metis.coursepart.manager.CourseManager;
import com.metis.coursepart.module.Course;
import com.metis.coursepart.module.CourseAlbum;
import com.metis.coursepart.module.CourseSubList;
import com.metis.msnetworklib.contract.ReturnInfo;
import com.metis.playerlib.PlayCallback;
import com.metis.playerlib.PlayerFragment;
import com.metis.playerlib.VideoFragment;
import com.metis.playerlib.VideoWrapperFragment;

import java.util.List;


public class CourseVideoDetailActivity extends AppCompatActivity implements
        View.OnClickListener, ViewPager.OnPageChangeListener, CourseAdapter.OnCourseClickListener,
        VideoFragment.OnFullScreenListener, PlayCallback{

    private static final String TAG = CourseVideoDetailActivity.class.getSimpleName();

    private FrameLayout mPlayerContainer = null;
    private VideoWrapperFragment mPlayerFragment = null;
    private Button mDetailBtn, mChapterBtn;
    private LinearLayout mCtrlContainer = null;
    private ViewPager mViewPager = null;

    private TabAdapter mTabAdapter = null;

    private CourseAlbum mCourseAlbum = null;
    //private long mCourseId = 0;

    private CourseVideoDetailFragment mDetailFragment = new CourseVideoDetailFragment();
    private CourseVideoChapterFragment mChapterFragment = new CourseVideoChapterFragment();
    private Fragment[] mFragmentArray = {
            mDetailFragment, mChapterFragment
    };

    private CourseDelegate mCurrentCourse = null;

    private String mRequestId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_video_detail);

        mPlayerContainer = (FrameLayout)findViewById(R.id.video_detail_player_fragment_container);
        mPlayerFragment = (VideoWrapperFragment)getSupportFragmentManager().findFragmentById(R.id.video_detail_player_fragment);
        mCtrlContainer = (LinearLayout)findViewById(R.id.video_detail_btn_container);
        mDetailBtn = (Button)findViewById(R.id.video_detail_detail_btn);
        mChapterBtn = (Button)findViewById(R.id.video_detail_chapter_btn);
        mDetailBtn.setOnClickListener(this);
        mChapterBtn.setOnClickListener(this);

        mViewPager = (ViewPager)findViewById(R.id.video_detail_view_pager);

        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabAdapter);
        mViewPager.addOnPageChangeListener(this);
        mDetailBtn.setSelected(true);

        mPlayerFragment.setOnFullScreenListener(this);
        mPlayerFragment.setPlayCallback(this);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mCourseAlbum = (CourseAlbum)getIntent().getSerializableExtra(ActivityDispatcher.KEY_COURSE_ALBUM);
        if (mCourseAlbum != null) {
            mDetailFragment.setCourseAlbum(mCourseAlbum);
            DisplayManager.getInstance(this).display(mCourseAlbum.coursePic, mPlayerFragment.getPreviewImageView());
            CourseManager.getInstance(this).getCourseSubList(mCourseAlbum.courseId, new RequestCallback<CourseSubList>() {
                @Override
                public void callback(ReturnInfo<CourseSubList> returnInfo, String callbackId) {
                    if (returnInfo.isSuccess()) {
                        CourseSubList courseSubList = returnInfo.getData();
                        List<CourseAlbum> relatedCourse = courseSubList.relatedCourse;
                        List<Course> subList = courseSubList.courseSubList;
                        if (relatedCourse != null && !relatedCourse.isEmpty()) {
                            mDetailFragment.setRelatedCourses(relatedCourse);
                        }
                        if (subList != null && !subList.isEmpty()) {
                            mChapterFragment.setSubCourseList(subList);
                            loadSubCourseDetail(subList.get(0).subCourseId);

                            //File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Movies" + File.separator + "TR.mp4");
                            mPlayerFragment.setSource(subList.get(0).videoUrl);
                            //mPlayerFragment.setDataSource(subList.get(0).videoUrl);
                            mPlayerFragment.setTitle(subList.get(0).subCourseName);

                        }
                    }
                }
            });
        }

        mChapterFragment.setOnCourseClickListener(this);

    }

    @Override
    public void onCourseClick(CourseDelegate delegate) {
        final Course course = delegate.getSource();
        if (delegate.equals(mCurrentCourse)) {
            if (delegate.isSelected()) {
                mPlayerFragment.pausePlay();
                //TODO pause
            } else {
                mPlayerFragment.startPlay();
                //TODO resume
            }
            delegate.setSelected(!delegate.isSelected());
        } else {
            if (mPlayerFragment.isStarted()) {
                mPlayerFragment.stopPlay(new VideoFragment.OnStopCallback() {
                    @Override
                    public void onStopped() {
                        mPlayerFragment.setTitle(course.subCourseName);
                        mPlayerFragment.setSource(course.videoUrl);
                        mPlayerFragment.startPlay();
                    }
                });
            } else {
                mPlayerFragment.setTitle(course.subCourseName);
                mPlayerFragment.setSource(course.videoUrl);
                mPlayerFragment.startPlay();
            }


            //TODO perform new video playing
            if (mCurrentCourse != null) {
                mCurrentCourse.setSelected(false);
            }
            delegate.setSelected(true);
        }
        mCurrentCourse = delegate;
        loadSubCourseDetail(mCurrentCourse.getSource().subCourseId);
        mChapterFragment.notifyDataSetChanged();

        Toast.makeText(this, delegate.getSource().subCourseName, Toast.LENGTH_SHORT).show();
    }

    private void loadSubCourseDetail (long subCourseId) {
        mRequestId = CourseManager.getInstance(CourseVideoDetailActivity.this).getSubCourseDetail(subCourseId, new RequestCallback<Course>() {
            @Override
            public void callback(ReturnInfo<Course> returnInfo, String callbackId) {
                Log.v(TAG, "loadSubCourseDetail " + returnInfo.isSuccess());
                if (returnInfo.isSuccess()) {
                    if (callbackId.equals(mRequestId)) {
                        mDetailFragment.setCurrentCourse(returnInfo.getData());
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mPlayerFragment.isFullScreen()) {
            mPlayerFragment.setFullScreen(false);
            return;
        }
        super.onBackPressed();
    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mPlayerContainer.getLayoutParams();
        if (params == null) {
            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        }
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
            mCtrlContainer.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            Log.v(TAG, TAG + " onConfigurationChanged SCREEN_ORIENTATION_LANDSCAPE");
            params.width = ViewGroup.LayoutParams.FILL_PARENT;
            params.height = ViewGroup.LayoutParams.FILL_PARENT;
            mPlayerContainer.setLayoutParams(params);
        } else {
            mCtrlContainer.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
            params.width = ViewGroup.LayoutParams.FILL_PARENT;
            params.height = getResources().getDimensionPixelSize(R.dimen.video_player_vertical_height);
            mPlayerContainer.setLayoutParams(params);
        }
        Log.v(TAG, TAG + " onConfigurationChanged " + newConfig.orientation);
    }*/

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.video_detail_detail_btn) {
            mViewPager.setCurrentItem(0, true);
        } else if (v.getId() == R.id.video_detail_chapter_btn) {
            mViewPager.setCurrentItem(1, true);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mDetailBtn.setSelected(true);
                mChapterBtn.setSelected(false);
                break;
            case 1:
                mDetailBtn.setSelected(false);
                mChapterBtn.setSelected(true);
                break;
            case 2:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFullScreen(boolean isFullScreen) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mPlayerContainer.getLayoutParams();
        if (params == null) {
            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        }
        if (isFullScreen) {
            mCtrlContainer.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            Log.v(TAG, TAG + " onConfigurationChanged SCREEN_ORIENTATION_LANDSCAPE");
            params.width = ViewGroup.LayoutParams.FILL_PARENT;
            params.height = ViewGroup.LayoutParams.FILL_PARENT;
            mPlayerContainer.setLayoutParams(params);
        } else {
            mCtrlContainer.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
            params.width = ViewGroup.LayoutParams.FILL_PARENT;
            params.height = getResources().getDimensionPixelSize(R.dimen.video_player_vertical_height);
            mPlayerContainer.setLayoutParams(params);
        }
    }

    @Override
    public void onStarted(BVideoView bVideoView) {

    }

    @Override
    public void onPaused(BVideoView bVideoView) {

    }

    @Override
    public void onResumed(BVideoView bVideoView) {

    }

    @Override
    public void onCompleted(BVideoView bVideoView) {
        mPlayerFragment.setFullScreen(false);
    }

    @Override
    public void onError(BVideoView bVideoView) {

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
    }

}

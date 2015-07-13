package com.metis.coursepart.activity;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.metis.base.manager.RequestCallback;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.fragment.CourseVideoChapterFragment;
import com.metis.coursepart.fragment.CourseVideoDetailFragment;
import com.metis.coursepart.manager.CourseManager;
import com.metis.coursepart.module.CourseAlbum;
import com.metis.coursepart.module.CourseSubList;
import com.metis.msnetworklib.contract.ReturnInfo;
import com.metis.playerlib.PlayerFragment;

import java.io.File;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;


public class CourseVideoDetailActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{

    private static final String TAG = CourseVideoDetailActivity.class.getSimpleName();

    private FrameLayout mPlayerContainer = null;
    private PlayerFragment mPlayerFragment = null;
    private Button mDetailBtn, mChapterBtn;
    private LinearLayout mCtrlContainer = null;
    private ViewPager mViewPager = null;

    private TabAdapter mTabAdapter = null;

    private long mCourseId = 0;

    private CourseVideoDetailFragment mDetailFragment = new CourseVideoDetailFragment();
    private CourseVideoChapterFragment mChapterFragment = new CourseVideoChapterFragment();
    private Fragment[] mFragmentArray = {
            mDetailFragment, mChapterFragment
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_video_detail);

        mPlayerContainer = (FrameLayout)findViewById(R.id.video_detail_player_fragment_container);
        mPlayerFragment = (PlayerFragment)getSupportFragmentManager().findFragmentById(R.id.video_detail_player_fragment);
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
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Movies" + File.separator + "TR.mp4");
        mPlayerFragment.setDataSource(file.getAbsolutePath());
        mPlayerFragment.setTitle(file.getName());
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mCourseId = getIntent().getLongExtra(ActivityDispatcher.KEY_ALBUM_ID, 0);
        if (mCourseId != 0) {
            CourseManager.getInstance(this).getCourseSubList(mCourseId, new RequestCallback<CourseSubList>() {
                @Override
                public void callback(ReturnInfo<CourseSubList> returnInfo) {
                    if (returnInfo.isSuccess()) {
                        CourseSubList courseSubList = returnInfo.getData();
                        List<CourseAlbum> relatedCourse = courseSubList.relatedCourse;
                        if (relatedCourse != null && !relatedCourse.isEmpty()) {
                            mDetailFragment.setRelatedCourses(relatedCourse);
                        }
                    }
                }
            });
        }
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

    @Override
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
    }

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

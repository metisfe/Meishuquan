package com.metis.coursepart.activity;

import android.content.DialogInterface;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.metis.base.manager.RequestCallback;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.fragment.CourseVideoChapterFragment;
import com.metis.coursepart.fragment.CourseVideoDetailFragment;
import com.metis.coursepart.manager.CourseManager;
import com.metis.coursepart.module.CourseSubList;
import com.metis.msnetworklib.contract.ReturnInfo;

import butterknife.InjectView;
import butterknife.OnClick;


public class CourseVideoDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mDetailBtn, mChapterBtn;
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

        mDetailBtn = (Button)findViewById(R.id.video_detail_detail_btn);
        mChapterBtn = (Button)findViewById(R.id.video_detail_chapter_btn);
        mDetailBtn.setOnClickListener(this);
        mChapterBtn.setOnClickListener(this);

        mViewPager = (ViewPager)findViewById(R.id.video_detail_view_pager);

        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabAdapter);
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
                        mDetailFragment.setRelatedCourses(returnInfo.getData().relatedCourse);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.video_detail_detail_btn) {
            mViewPager.setCurrentItem(0, true);
        } else if (v.getId() == R.id.video_detail_chapter_btn) {
            mViewPager.setCurrentItem(1, true);
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
    }

}

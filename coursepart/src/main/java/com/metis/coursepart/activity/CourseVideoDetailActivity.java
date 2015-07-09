package com.metis.coursepart.activity;

import android.content.DialogInterface;
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

import com.metis.coursepart.R;
import com.metis.coursepart.fragment.CourseVideoChapterFragment;
import com.metis.coursepart.fragment.CourseVideoDetailFragment;

import butterknife.InjectView;
import butterknife.OnClick;


public class CourseVideoDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mDetailBtn, mChapterBtn;
    private ViewPager mViewPager = null;

    private TabAdapter mTabAdapter = null;

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
    public void onClick(View v) {
        if (v.getId() == R.id.video_detail_detail_btn) {
            mViewPager.setCurrentItem(0, true);
        } else if (v.getId() == R.id.video_detail_chapter_btn) {
            mViewPager.setCurrentItem(1, true);
        }
    }

    private static class TabAdapter extends FragmentStatePagerAdapter {

        private Fragment[] mFragmentArray = {
                CourseVideoDetailFragment.getInstance(),
                CourseVideoChapterFragment.getInstance()
        };

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

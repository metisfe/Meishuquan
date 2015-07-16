package com.metis.coursepart.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.metis.base.TitleBarActivity;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.fragment.CourseGalleryFragment;
import com.metis.coursepart.fragment.CourseGalleryItemFragment;
import com.metis.coursepart.manager.GalleryCacheManager;
import com.metis.coursepart.module.GalleryItem;

import java.util.List;

public class GalleryItemDetailActivity extends TitleBarActivity implements ViewPager.OnPageChangeListener{

    private static final String TAG = GalleryItemDetailActivity.class.getSimpleName();

    private ViewPager mPhotoVp = null;

    private GalleryAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_item_detail);

        mPhotoVp = (ViewPager)findViewById(R.id.item_detail_view_pager);
        mAdapter = new GalleryAdapter(this, getSupportFragmentManager());
        mPhotoVp.setAdapter(mAdapter);

        long picId = getIntent().getLongExtra(ActivityDispatcher.KEY_GALLERY_ITEM_ID, 0);
        Log.v(TAG, "imageDetailActivity get out id=" + picId);
        int index = GalleryCacheManager.getInstance(this).getIndexById(picId);
        Log.v(TAG, "imageDetailActivity get out inext=" + index);
        if (index < 0) {
            index = 0;
        }
        mPhotoVp.addOnPageChangeListener(this);
        mPhotoVp.setCurrentItem(index);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhotoVp.removeOnPageChangeListener(this);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ((CourseGalleryItemFragment)mAdapter.getItem(position)).setGalleryItem(GalleryCacheManager.getInstance(this).getGalleryItem(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class GalleryAdapter extends FragmentStatePagerAdapter {
        private GalleryCacheManager mManager = null;
        private CourseGalleryItemFragment[] mFragmentArray = null;
        public GalleryAdapter(Context context, FragmentManager fm) {
            super(fm);
            mManager = GalleryCacheManager.getInstance(context);
            mFragmentArray = new CourseGalleryItemFragment[mManager.size()];
        }

        @Override
        public Fragment getItem(int position) {
            if (mFragmentArray[position] == null) {
                mFragmentArray[position] = new CourseGalleryItemFragment();
            }
            //fragment.setGalleryItem(mManager.getGalleryItem(position));
            return mFragmentArray[position];
        }

        @Override
        public int getCount() {
            return mManager.size();
        }
    }

}

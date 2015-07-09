package com.metis.coursepart.activity;

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
import com.metis.coursepart.R;
import com.metis.coursepart.fragment.CourseGalleryFragment;
import com.metis.coursepart.fragment.CourseGalleryItemFragment;
import com.metis.coursepart.module.GalleryItem;

import java.util.List;

public class GalleryItemDetailActivity extends TitleBarActivity {

    private static final String TAG = GalleryItemDetailActivity.class.getSimpleName();

    private ViewPager mPhotoVp = null;

    private List<GalleryItem> mGalleryItems = CourseGalleryFragment.FakeDataFactory.make();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_item_detail);

        mPhotoVp = (ViewPager)findViewById(R.id.item_detail_view_pager);
        mPhotoVp.setAdapter(new GalleryAdapter(getSupportFragmentManager()));

        Log.v(TAG, "onCreate " + mGalleryItems.size());
    }

    private class GalleryAdapter extends FragmentStatePagerAdapter {

        public GalleryAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            CourseGalleryItemFragment fragment = new CourseGalleryItemFragment();
            fragment.setGalleryItem(mGalleryItems.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return mGalleryItems.size();
        }
    }

}

package com.metis.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.R;
import com.metis.base.widget.ImagePreviewable;

import java.util.List;

/**
 * Created by Beak on 2015/7/20.
 */
public class MultiImagePreviewFragment extends Fragment implements ViewPager.OnPageChangeListener{

    private ViewPager mViewPager = null;
    private SingleImagePreviewFragment[] mFragmentArray = null;
    private List<ImagePreviewable> mImages = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multi_image_preview, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager)view.findViewById(R.id.preview_view_pager);
        mViewPager.addOnPageChangeListener(this);

        setImages(mImages);
    }

    public void setImages (List<ImagePreviewable> images) {
        mImages = images;
        if (mImages != null) {
            mFragmentArray = new SingleImagePreviewFragment[images.size()];
            mViewPager.setAdapter(new PreviewAdapter(getChildFragmentManager()));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Fragment fragment = ((PreviewAdapter)mViewPager.getAdapter()).getItem(position);
        ((SingleImagePreviewFragment)fragment).setImagePreviewable(mImages.get(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class PreviewAdapter extends FragmentStatePagerAdapter {

        public PreviewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            SingleImagePreviewFragment fragment = mFragmentArray[position];
            if (fragment == null) {
                mFragmentArray[position] = new SingleImagePreviewFragment();
                mFragmentArray[position].setImagePreviewable(mImages.get(position));
            }
            return mFragmentArray[position];
        }

        @Override
        public int getCount() {
            return mImages.size();
        }
    }
}

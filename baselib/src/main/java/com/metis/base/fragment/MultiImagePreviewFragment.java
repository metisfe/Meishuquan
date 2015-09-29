package com.metis.base.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.metis.base.ActivityDispatcher;
import com.metis.base.R;
import com.metis.base.widget.ImagePreviewable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Beak on 2015/7/20.
 */
public class MultiImagePreviewFragment extends BaseFragment implements ViewPager.OnPageChangeListener, SingleImagePreviewFragment.OnImageTabListener{

    private ViewPager mViewPager = null;
    private SingleImagePreviewFragment[] mFragmentArray = null;
    private List<ImagePreviewable> mImages = null;
    private int mIndex = 0;

    private OnImageOperateListener mOperateListener = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent it = getActivity().getIntent();
        final int index = it.getIntExtra(ActivityDispatcher.KEY_INDEX, 0);
        Parcelable[] parcelables = it.getParcelableArrayExtra(ActivityDispatcher.KEY_IMAGES);
        final int length = parcelables.length;
        List<ImagePreviewable> imagePreviewables = new ArrayList<ImagePreviewable>();
        for (int i = 0; i < length; i++) {
            Parcelable parcelable = parcelables[i];
            if (parcelable instanceof ImagePreviewable) {
                imagePreviewables.add((ImagePreviewable)parcelable);
            }
        }
        setImages(imagePreviewables, index);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewPager.removeOnPageChangeListener(this);
    }

    public void setImages (List<ImagePreviewable> images) {
        setImages(images, 0);
    }

    public void setImages (List<ImagePreviewable> images, int index) {
        mImages = images;
        mIndex = index;
        if (mImages != null) {
            mFragmentArray = new SingleImagePreviewFragment[images.size()];
            mViewPager.setAdapter(new PreviewAdapter(getChildFragmentManager()));
            mViewPager.setCurrentItem(index);
            if (index == 0) {
                onPageSelected(0);
            }
        }
    }

    public File getImageFile (int position) {
        return mFragmentArray[position].getImageFile();
    }

    public File getCurrentImageFile () {
        return getImageFile(mViewPager.getCurrentItem());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Fragment fragment = ((PreviewAdapter)mViewPager.getAdapter()).getItem(position);
        ImagePreviewable previewable = mImages.get(position);
        ((SingleImagePreviewFragment) fragment).setImagePreviewable(previewable);

        if (mOperateListener != null) {
            mOperateListener.onPageChange(position, previewable);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setOnOperateListener (OnImageOperateListener listener) {
        mOperateListener = listener;
    }

    @Override
    public void onImageTab(ImagePreviewable previewable) {
        if (mOperateListener != null) {
            mOperateListener.onPageTab(mViewPager.getCurrentItem(), previewable);
        }
    }

    @Override
    public void onImageLongClick(ImagePreviewable previewable, File cacheFile) {
        if (mOperateListener != null) {
            mOperateListener.onPageLongClick(mViewPager.getCurrentItem(), previewable, cacheFile);
        }
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
                mFragmentArray[position].setOnImageTabListener(MultiImagePreviewFragment.this);
            }
            return mFragmentArray[position];
        }

        @Override
        public int getCount() {
            return mImages.size();
        }
    }

    public static interface OnImageOperateListener {
        public void onPageChange (int position, ImagePreviewable image);
        public void onPageTab (int position, ImagePreviewable image);
        public void onPageLongClick (int position, ImagePreviewable image, File cacheFile);
    }
}

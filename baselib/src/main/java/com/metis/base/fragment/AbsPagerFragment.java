package com.metis.base.fragment;

import android.content.Context;
import android.support.v4.view.ViewPager;

/**
 * Created by Beak on 2015/9/1.
 */
public abstract class AbsPagerFragment extends BaseFragment {

    public abstract CharSequence getTitle (Context context);

    private boolean isInThisPage = false;

    public boolean isInThisPage () {
        return isInThisPage;
    }

    public void onPagerIn() {
        isInThisPage = true;
    }

    public void onPagerOut () {
        isInThisPage = false;
    }

    public class PagerListener implements ViewPager.OnPageChangeListener {

        private int mCurrentPosition = 0;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
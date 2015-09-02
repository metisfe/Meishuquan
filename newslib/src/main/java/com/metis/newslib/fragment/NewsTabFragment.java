package com.metis.newslib.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.fragment.DockFragment;
import com.metis.base.manager.RequestCallback;
import com.metis.base.widget.SlidingTabLayout;
import com.metis.base.widget.TitleBar;
import com.metis.base.widget.dock.DockBar;
import com.metis.msnetworklib.contract.ReturnInfo;
import com.metis.newslib.R;
import com.metis.newslib.manager.NewsManager;
import com.metis.newslib.module.ChannelItem;
import com.metis.newslib.module.ChannelList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/9/1.
 */
public class NewsTabFragment extends DockFragment {

    private DockBar.Dock mDock = null;

    private RecyclerView mRv = null;
    private ViewPager mVp = null;

    private NewsFragmentPagerAdapter mPagerAdapter = null;

    private SlidingTabLayout mSlidingTabLayout = null;

    private List<ChannelItem> mChannelItemList = new ArrayList<ChannelItem>();
    private List<NewsPagerFragment> mFragmentList = new ArrayList<NewsPagerFragment>();

    @Override
    public DockBar.Dock getDock(Context context) {
        if (mDock == null) {
            mDock = new DockBar.Dock(context, R.id.dock_item_id_news, android.R.drawable.btn_radio, R.string.dock_item_news_title, this);
        }
        return mDock;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_tab, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSlidingTabLayout = (SlidingTabLayout)view.findViewById(R.id.news_tab_layout);
        mVp = (ViewPager)view.findViewById(R.id.news_view_pager);

        mPagerAdapter = new NewsFragmentPagerAdapter(getChildFragmentManager());
        mVp.setAdapter(mPagerAdapter);
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mFragmentList.get(position).onSelected();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mSlidingTabLayout.setCustomTabView(R.layout.tab_item, R.id.tab_item_name);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.std_color_A);
            }

            @Override
            public int getDividerColor(int position) {
                return Color.TRANSPARENT;
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NewsManager.getInstance(getActivity()).getChannelList("0", 0, new RequestCallback<ChannelList>() {
            @Override
            public void callback(ReturnInfo<ChannelList> returnInfo, String callbackId) {
                if (returnInfo.isSuccess()) {
                    mChannelItemList = returnInfo.getData().selectedChannels;
                    final int length = mChannelItemList.size();
                    for (int i = 0; i < length; i++) {
                        ChannelItem item = mChannelItemList.get(i);
                        NewsPagerFragment fragment = new NewsPagerFragment();
                        fragment.setChannelItem(item);
                        mFragmentList.add(fragment);
                    }
                    mPagerAdapter.notifyDataSetChanged();
                    mSlidingTabLayout.setViewPager(mVp);

                    mFragmentList.get(0).onSelected();
                }
            }
        });
    }

    private class NewsFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public NewsFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentList.get(position).getTitle(getActivity());
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}

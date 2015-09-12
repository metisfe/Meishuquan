package com.metis.newslib.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.fragment.AbsPagerFragment;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.Footer;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.base.widget.callback.OnScrollBottomListener;
import com.metis.msnetworklib.contract.ReturnInfo;
import com.metis.newslib.R;
import com.metis.newslib.adapter.NewsAdapter;
import com.metis.newslib.adapter.NewsDecoration;
import com.metis.newslib.adapter.delegate.NewsBigDelegate;
import com.metis.newslib.adapter.delegate.NewsSmallDelegate;
import com.metis.newslib.manager.NewsManager;
import com.metis.newslib.module.ChannelItem;
import com.metis.newslib.module.NewsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Beak on 2015/9/1.
 */
public class NewsPagerFragment extends AbsPagerFragment {

    private static final String TAG = NewsPagerFragment.class.getSimpleName();

    private ChannelItem mItem = null;
    private String mTitle = null;

    private RecyclerView mNewsRv = null;
    private NewsAdapter mAdapter = null;
    private LinearLayoutManager mLinearLayoutManager;

    private SwipeRefreshLayout mSrl = null;

    private long mLastNewsId = 0;

    private int mGroupSize = 6;

    private boolean isLoading = false;

    private int mScrolledPosition = 0;

    private Footer mFooter = null;
    private FooterDelegate mFooterDelegate = null;

    @Override
    public CharSequence getTitle(Context context) {
        return mTitle;
    }

    public void setTitle (String string) {
        mTitle = string;
    }

    public void setChannelItem (ChannelItem item) {
        mItem = item;
        mTitle = item.channelName;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_pager, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNewsRv = (RecyclerView)view.findViewById(R.id.news_pager_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mNewsRv.setLayoutManager(mLinearLayoutManager);
        mNewsRv.setHasFixedSize(true);
        mNewsRv.addItemDecoration(new NewsDecoration());
        mNewsRv.addOnScrollListener(new OnScrollBottomListener() {
            @Override
            public void onScrollBottom(RecyclerView recyclerView, int newState) {
                if (!isLoading) {
                    loadData(mAdapter.getLastId());
                }
            }
        });

        mSrl = (SwipeRefreshLayout)view.findViewById(R.id.news_pager_swipe_refresh_layout);
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLastNewsId = 0;
                loadData(mLastNewsId);
            }
        });
        mSrl.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_purple
        );

        mFooter = new Footer(Footer.STATE_SUCCESS);
        mFooterDelegate = new FooterDelegate(mFooter);
    }

    @Override
    public void onPagerIn() {
        super.onPagerIn();
        if (mLastNewsId == 0) {
            if (mSrl != null) {
                mSrl.post(new Runnable() {
                    @Override
                    public void run() {
                        mSrl.setRefreshing(true);
                    }
                });
                loadData(0);
            }
        } else if (mAdapter != null) {
            mNewsRv.setAdapter(mAdapter);
            mLinearLayoutManager.scrollToPosition(mScrolledPosition);
        }
    }

    @Override
    public void onPagerOut() {
        super.onPagerOut();
        mScrolledPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
    }

    public void loadData (final long lastId) {
        if (mItem != null) {
            isLoading = true;
            if (mAdapter != null && mAdapter.getItemCount() > 0) {
                mFooter.setState(Footer.STATE_WAITTING);
                mAdapter.notifyDataSetChanged();
            }
            NewsManager.getInstance(getActivity())
                    .getNewsList(mItem.channelId, lastId, new RequestCallback<List<NewsItem>>() {
                        @Override
                        public void callback(ReturnInfo<List<NewsItem>> returnInfo, String callbackId) {
                            isLoading = false;
                            if (!isAlive()) {
                                return;
                            }
                            if (mSrl.isRefreshing()) {
                                mSrl.setRefreshing(false);
                            }
                            if (mAdapter == null) {
                                mAdapter = new NewsAdapter(getActivity());
                                mNewsRv.setAdapter(mAdapter);
                            }
                            if (returnInfo.isSuccess()) {
                                if (lastId == 0) {
                                    mAdapter.clearDataList();
                                }
                                List<BaseDelegate> delegateList = translate(returnInfo.getData());
                                mFooter.setState(delegateList != null && delegateList.size() > 0 ? Footer.STATE_SUCCESS : Footer.STATE_NO_MORE);
                                if (!mAdapter.hasFooter()) {
                                    mAdapter.addDataItem(mFooterDelegate);
                                }
                                mAdapter.addDataList(mAdapter.getItemCount() - 1, delegateList);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    private List<BaseDelegate> translate (List<NewsItem> items) {
        final int length = items.size();
        if (length > 0) {
            mLastNewsId = items.get(length - 1).newsId;
            final int groupCount = length / mGroupSize + 1;
            List<Integer> mBigIndexList = new ArrayList<>();
            mBigIndexList.add(0);
            if (groupCount > 0) {
                Random random = new Random();
                for (int i = mBigIndexList.size(); i < groupCount; i++) {
                    int randomInt = random.nextInt(mGroupSize / 2);
                    int index = mGroupSize / 2 + 1 + randomInt + mBigIndexList.get(mBigIndexList.size() - 1);
                    index = Math.min(index, length - 1);
                    mBigIndexList.add(index);
                }
            }
            List<BaseDelegate> list = new ArrayList<BaseDelegate>();
            boolean lastContains = false;
            for (int i = 0; i < length; i++) {
                NewsItem item = items.get(i);
                boolean contains = mBigIndexList.contains(i);
                if (contains) {
                    list.add(new NewsBigDelegate(item));
                    if (i > 0) {
                        ((NewsSmallDelegate)list.get(i - 1)).aboveBig(true);
                    }
                } else {
                    NewsSmallDelegate smallDelegate = new NewsSmallDelegate(item);
                    list.add(smallDelegate);
                    if (lastContains) {
                        smallDelegate.belowBig(true);
                    }
                }
                lastContains = contains;
            }
            return list;
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", mTitle);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString("title");
        }
    }
}

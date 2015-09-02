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
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
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

    private SwipeRefreshLayout mSrl = null;

    private long mLastNewsId = 0;

    private int mGroupSize = 6;

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
        mNewsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNewsRv.setHasFixedSize(true);
        mNewsRv.addItemDecoration(new NewsDecoration());

        mSrl = (SwipeRefreshLayout)view.findViewById(R.id.news_pager_swipe_refresh_layout);

        Log.v(TAG, TAG + " onViewCreated");
    }

    @Override
    public void onSelected() {
        super.onSelected();
        if (mLastNewsId == 0) {
            loadData(0);
        } else {
            mNewsRv.setAdapter(mAdapter);
        }
    }

    public void loadData (int lastId) {
        if (mItem != null) {
            NewsManager.getInstance(getActivity())
                    .getNewsList(mItem.channelId, lastId, new RequestCallback<List<NewsItem>>() {
                        @Override
                        public void callback(ReturnInfo<List<NewsItem>> returnInfo, String callbackId) {
                            if (returnInfo.isSuccess()) {
                                if (mAdapter == null) {
                                    mAdapter = new NewsAdapter(getActivity());
                                }
                                mNewsRv.setAdapter(mAdapter);
                                mAdapter.addDataList(translate(returnInfo.getData()));
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

package com.metis.commentpart.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.metis.base.fragment.DockFragment;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.User;
import com.metis.base.widget.TitleBar;
import com.metis.base.widget.dock.DockBar;
import com.metis.commentpart.ActivityDispatcher;
import com.metis.commentpart.R;
import com.metis.commentpart.activity.CommentDetailActivity;
import com.metis.commentpart.adapter.StatusAdapter;
import com.metis.commentpart.adapter.delegate.StatusDelegate;
import com.metis.commentpart.widget.OnScrollViewFlipperListener;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Beak on 2015/7/24.
 */
public class CommentTabFragment extends DockFragment {

    private DockBar.Dock mDock = null;

    private TitleBar mTitleBar = null;
    private RecyclerView mStatusRv = null;
    private SwipeRefreshLayout mSrl = null;

    private String[] mTotalData = {
            "AMA (Ask Me Anything) ", "session with ", "Google Design", "(see in your timezone)",
            "Ask questions", " to Mike Denny, ", "Design Advocate at Google Design ", "about Creating & Developing ",
            "the Material Design ", "language this ", "Wednesday July 29 ", "at 9am PST ",
    };

    private StatusAdapter mAdapter = null;

    @Override
    public DockBar.Dock getDock(Context context) {
        if (mDock == null) {
            mDock = new DockBar.Dock(context, 1, android.R.drawable.btn_star, android.R.string.copyUrl, this);
        }
        return mDock;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment_tab, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleBar = (TitleBar)view.findViewById(R.id.comment_tab_title_bar);
        mStatusRv = (RecyclerView)view.findViewById(R.id.comment_tab_recycler_view);
        mSrl = (SwipeRefreshLayout)view.findViewById(R.id.comment_swipe_refresh_layout);

        mTitleBar.setTitleCenter(R.string.tab_title_center);
        mTitleBar.setDrawableResourceLeft(R.drawable.ic_filter);
        mTitleBar.setDrawableResourceRight(R.drawable.ic_camera);

        mTitleBar.setOnLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.filterActivity(getActivity());
            }
        });
        mTitleBar.setOnRightBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.publishStatusActivity(getActivity());
            }
        });

        mStatusRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mStatusRv.addOnScrollListener(new OnScrollViewFlipperListener());
        mAdapter = new StatusAdapter(getActivity());
        mStatusRv.setAdapter(mAdapter);

        mSrl.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_purple
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<StatusDelegate> list = new ArrayList<StatusDelegate>();
        List<String[]> data = makeFakeData(20);
        final int length = data.size();
        for (int i = 0; i < length; i++) {
            list.add(new StatusDelegate(data.get(i)));
        }
        mAdapter.addDataList(list);
    }

    private List<String[]> makeFakeData (int count) {
        List<String[]> list = new ArrayList<String[]>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int length = random.nextInt(5) + 3;
            String[] strings = new String[length];
            list.add(strings);
            for (int k = 0; k < length; k++) {
                strings[k] = i + " -- " + mTotalData[(length + i + k)%mTotalData.length];
            }
        }
        return list;
    }

}

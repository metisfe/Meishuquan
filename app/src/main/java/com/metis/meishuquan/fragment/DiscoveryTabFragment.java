package com.metis.meishuquan.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.fragment.DockFragment;
import com.metis.base.manager.CacheManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.widget.IconTextView;
import com.metis.base.widget.TitleBar;
import com.metis.base.widget.dock.DockBar;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.DiscoveryExamActivity;
import com.metis.meishuquan.activity.ExamJoinActivity;
import com.metis.meishuquan.adapter.DiscoveryAdapter;
import com.metis.meishuquan.adapter.DiscoveryItemDecoration;
import com.metis.meishuquan.adapter.delegate.DiscoveryItemDelegate;
import com.metis.meishuquan.manager.DiscoveryManager;
import com.metis.meishuquan.module.DiscoveryItem;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/10/14.
 */
public class DiscoveryTabFragment extends DockFragment {

    private DockBar.Dock mDock = null;

    private TitleBar mTitleBar = null;

    private RecyclerView mDiscoveryRv = null;

    private DiscoveryAdapter mAdapter = null;

    private DiscoveryItem mExamJoinItem = null;
    private DiscoveryItemDelegate mExamJoinItemDelegate = null;

    @Override
    public DockBar.Dock getDock(Context context) {
        if (mDock == null) {
            mDock = new DockBar.Dock(context, R.id.dock_item_id_discovery, R.drawable.ic_discovery_sel, R.string.dock_item_discovery_title, this);
        }
        return mDock;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discovery, null, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleBar = (TitleBar)view.findViewById(R.id.discovery_title);
        mTitleBar.setTitleCenter(R.string.dock_item_discovery_title);

        mDiscoveryRv = (RecyclerView)view.findViewById(R.id.discovery_recycler_view);
        mDiscoveryRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mDiscoveryRv.addItemDecoration(new DiscoveryItemDecoration());

        mAdapter = new DiscoveryAdapter(getContext());
        mDiscoveryRv.setAdapter(mAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mExamJoinItem = new DiscoveryItem();
        mExamJoinItem.name = getString(R.string.text_join_exam);
        mExamJoinItemDelegate = new DiscoveryItemDelegate(mExamJoinItem);
        mExamJoinItemDelegate.setIsNative(true);
        mExamJoinItemDelegate.setIconRes(R.drawable.ic_discovery_join_exam);
        mExamJoinItemDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ExamJoinActivity.class));
            }
        });

        mAdapter.addDataItem(mExamJoinItemDelegate);

        List<DiscoveryItem> items = CacheManager.getInstance(getContext()).readUserDataAtDatabase(DiscoveryItem.class, "extends_discoveries.db");
        if (items != null && !items.isEmpty()) {
            parseItems(items);
        }

        mAdapter.notifyDataSetChanged();

        DiscoveryManager.getInstance(getContext()).getWebModule(0, new RequestCallback<List<DiscoveryItem>>() {
            @Override
            public void callback(ReturnInfo<List<DiscoveryItem>> returnInfo, String callbackId) {
                if (returnInfo.isSuccess()) {
                    List<DiscoveryItem> items = returnInfo.getData();
                    if (items != null && !items.isEmpty()) {
                        CacheManager.getInstance(getContext()).saveAllUserDataAtDatabase(items, "extends_discoveries.db", DiscoveryItem.class, true);
                        parseItems(items);
                    }
                }
            }
        });
    }

    private void parseItems (List<DiscoveryItem> items) {
        if (items == null && items.isEmpty()) {
            return;
        }
        List<DiscoveryItemDelegate> delegates = new ArrayList<DiscoveryItemDelegate>();
        final int length = items.size();
        for (int i = 0; i < length; i++) {
            DiscoveryItem item = items.get(i);
            if (!item.isShow) {
                continue;
            }
            delegates.add(new DiscoveryItemDelegate(item));
        }
        mAdapter.clearExtendItems();
        mAdapter.addDataList(delegates);
        mAdapter.notifyDataSetChanged();
    }

}

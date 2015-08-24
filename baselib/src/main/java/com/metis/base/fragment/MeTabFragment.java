package com.metis.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.R;
import com.metis.base.manager.AccountManager;
import com.metis.base.widget.adapter.MeAdapter;
import com.metis.base.widget.adapter.delegate.MeHeaderDelegate;
import com.metis.base.widget.dock.DockBar;

/**
 * Created by Beak on 2015/8/24.
 */
public class MeTabFragment extends DockFragment {

    private DockBar.Dock mDock = null;

    private RecyclerView mRv;

    private MeAdapter mMeAdapter = null;

    @Override
    public DockBar.Dock getDock(Context context) {
        if (mDock == null) {
            mDock = new DockBar.Dock(context, 5, android.R.drawable.btn_star, R.string.tab_me, this);
        }
        return mDock;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRv = (RecyclerView)view.findViewById(R.id.me_recycler_view);

        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        mMeAdapter = new MeAdapter(getActivity());

        mRv.setAdapter(mMeAdapter);

        mMeAdapter.addDataItem(new MeHeaderDelegate(AccountManager.getInstance(getActivity()).getMe()));

    }
}

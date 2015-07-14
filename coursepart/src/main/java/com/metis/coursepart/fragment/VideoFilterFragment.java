package com.metis.coursepart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.base.manager.RequestCallback;
import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.coursepart.adapter.AlbumAdapter;
import com.metis.coursepart.adapter.decoration.MarginDecoration;
import com.metis.coursepart.adapter.decoration.VideoItemDetailDecoration;
import com.metis.coursepart.adapter.decoration.VideoItemSmallDecoration;
import com.metis.coursepart.adapter.delegate.AlbumSmallDelegate;
import com.metis.coursepart.manager.CourseManager;
import com.metis.coursepart.module.CourseAlbum;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/14.
 */
public class VideoFilterFragment extends BaseFilterFragment implements FilterPanelFragment.OnFilterChangeListener{

    private AlbumAdapter mAdapter = null;

    private String mCurrentRequestId = null;

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    public DelegateAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new AlbumAdapter(getActivity());
        }
        return mAdapter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFilterPanelFragment().setOnFilterChangeListener(this);
        getRecyclerView().addItemDecoration(new MarginDecoration((int)(getResources().getDisplayMetrics().density * 240)));
        getRecyclerView().addItemDecoration(new VideoItemSmallDecoration());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getFilterPanelFragment().setOnFilterChangeListener(null);
    }

    @Override
    public void onFilterChanged(long state, long category, long studio, long charge) {
        mCurrentRequestId = CourseManager.getInstance(getActivity()).getCourseList(
                category,
                state,
                "",
                1,
                studio,
                charge,
                new RequestCallback<List<CourseAlbum>>() {
                    @Override
                    public void callback(ReturnInfo<List<CourseAlbum>> returnInfo, String callbackId) {
                        if (!callbackId.equals(mCurrentRequestId)) {
                            return;
                        }
                        List<CourseAlbum> albumList = returnInfo.getData();
                        List<AlbumSmallDelegate> delegates = new ArrayList<AlbumSmallDelegate>();
                        final int length = albumList.size();
                        for (int i = 0; i < length; i++) {
                            delegates.add(new AlbumSmallDelegate(albumList.get(i)));
                        }
                        mAdapter.addDataList(delegates);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }
}

package com.metis.coursepart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.base.manager.RequestCallback;
import com.metis.base.module.Footer;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.coursepart.adapter.AlbumAdapter;
import com.metis.coursepart.adapter.decoration.VideoFilterMarginDecoration;
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

    private static final String TAG = VideoFilterFragment.class.getSimpleName();

    private AlbumAdapter mAdapter = null;

    private String mCurrentRequestId = null;
    private int mIndex = 1;

    private boolean isLoading = false;

    private Footer mFooter = null;
    private FooterDelegate mFooterDelegate = null;

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
        getRecyclerView().addItemDecoration(new VideoFilterMarginDecoration((int) (getResources().getDisplayMetrics().density * 240)));
        getRecyclerView().addItemDecoration(new VideoItemSmallDecoration());

        mFooter = new Footer(Footer.STATE_WAITTING);
        mFooterDelegate = new FooterDelegate(mFooter);
        mAdapter.addDataItem(mFooterDelegate);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData(
                getCurrentState(),
                getFilterPanelFragment().getCurrentCategory(),
                getFilterPanelFragment().getCurrentStudio(),
                getFilterPanelFragment().getCurrentCharge());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getFilterPanelFragment().setOnFilterChangeListener(null);
    }

    @Override
    public void onScrollBottom() {
        super.onScrollBottom();
        if (!isLoading) {
            mIndex++;
            loadData(
                    getFilterPanelFragment().getCurrentState(),
                    getFilterPanelFragment().getCurrentCategory(),
                    getFilterPanelFragment().getCurrentStudio(),
                    getFilterPanelFragment().getCurrentCharge());
        }

    }

    @Override
    public void onFilterChanged(final long state, final long category, final long studio, final long charge) {
        mIndex = 1;
        mAdapter.clearDataList();
        mFooter.setState(Footer.STATE_WAITTING);
        mAdapter.addDataItem(mFooterDelegate);
        mAdapter.notifyDataSetChanged();
        loadData(state, category, studio, charge);

    }

    public void loadData (long state, long category, long studio, long charge) {
        isLoading = true;
        mCurrentRequestId = CourseManager.getInstance(getActivity()).getCourseList(
                category,
                state,
                "",
                mIndex,
                studio,
                charge,
                new RequestCallback<List<CourseAlbum>>() {
                    @Override
                    public void callback(ReturnInfo<List<CourseAlbum>> returnInfo, String callbackId) {
                        isLoading = false;
                        if (!isAlive()) {
                            return;
                        }
                        if (!callbackId.equals(mCurrentRequestId)) {
                            return;
                        }
                        if (returnInfo.isSuccess()) {
                            List<CourseAlbum> albumList = returnInfo.getData();
                            List<AlbumSmallDelegate> delegates = new ArrayList<AlbumSmallDelegate>();
                            final int length = albumList.size();
                            for (int i = 0; i < length; i++) {
                                delegates.add(new AlbumSmallDelegate(albumList.get(i)));
                            }
                            if (mIndex == 1) {
                                mAdapter.clearDataList();
                                mAdapter.addDataItem(mFooterDelegate);
                            }
                            Log.v(TAG, "loadData mIndex=" + mIndex);
                            mFooter.setState(albumList.isEmpty() ? Footer.STATE_NO_MORE : Footer.STATE_SUCCESS);

                            mAdapter.addDataList(mAdapter.getItemCount() - 1, delegates);
                        } else {
                            mFooter.setState(Footer.STATE_FAILED);
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                });
    }
}

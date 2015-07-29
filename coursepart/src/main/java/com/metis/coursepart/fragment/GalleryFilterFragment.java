package com.metis.coursepart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.metis.base.manager.RequestCallback;
import com.metis.base.module.Footer;
import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.GalleryAdapter;
import com.metis.coursepart.adapter.decoration.GalleryFilterMarginDecoration;
import com.metis.coursepart.adapter.decoration.GalleryItemDecoration;
import com.metis.coursepart.adapter.decoration.VideoFilterMarginDecoration;
import com.metis.coursepart.adapter.delegate.GalleryItemDelegate;
import com.metis.coursepart.manager.CourseManager;
import com.metis.coursepart.module.GalleryItem;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/21.
 */
public class GalleryFilterFragment extends BaseFilterFragment implements FilterPanelFragment.OnFilterChangeListener{

    private static final String TAG = GalleryFilterFragment.class.getSimpleName();

    private GalleryAdapter mAdapter = null;

    private int mIndex = 1;
    private boolean isLoading = false;

    private String mRequestId = null;

    private Footer mFooter = null;
    private FooterDelegate mFooterDelegate = null;

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        final int spanCount = getResources().getInteger(R.integer.gallery_span_count);
        StaggeredGridLayoutManager staggeredGridLayoutManager
                = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        return staggeredGridLayoutManager;
    }

    @Override
    public DelegateAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new GalleryAdapter(getActivity());
        }
        return mAdapter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRecyclerView().addItemDecoration(new GalleryFilterMarginDecoration((int) (getResources().getDisplayMetrics().density * 240)));

        getFilterPanelFragment().setOnFilterChangeListener(this);

        mFooter = new Footer(Footer.STATE_WAITTING);
        mFooterDelegate = new FooterDelegate(mFooter);
        mFooterDelegate.setIsInStaggeredGrid(true);
        mAdapter.addDataItem(mFooterDelegate);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData(getFilterPanelFragment().getCurrentState(),
                getFilterPanelFragment().getCurrentCategory(),
                getFilterPanelFragment().getCurrentStudio(),
                getFilterPanelFragment().getCurrentCharge(),
                1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getFilterPanelFragment().setOnFilterChangeListener(null);
    }

    @Override
    public void onScrollBottom() {
        if (!isLoading) {
            mFooter.setState(Footer.STATE_WAITTING);
            mAdapter.notifyDataSetChanged();
            loadData(
                    getFilterPanelFragment().getCurrentState(),
                    getFilterPanelFragment().getCurrentCategory(),
                    getFilterPanelFragment().getCurrentStudio(),
                    getFilterPanelFragment().getCurrentCharge(),
                    mIndex + 1
            );
        }
    }

    public void loadData (long state, long category, long studioId, long chargeType, final int index) {
        isLoading = true;
        mRequestId = CourseManager.getInstance(getActivity()).getGalleryPicList(category, "", state, studioId, chargeType, index, new RequestCallback<List<GalleryItem>>() {
            @Override
            public void callback(ReturnInfo<List<GalleryItem>> returnInfo, String callbackId) {
                isLoading = false;
                if (!isAlive()) {
                    return;
                }
                if (!callbackId.equals(mRequestId)) {
                    return;
                }
                if (returnInfo.isSuccess()) {
                    List<GalleryItemDelegate> delegates = new ArrayList<GalleryItemDelegate>();
                    List<GalleryItem> galleryItemList = returnInfo.getData();
                    final int length = galleryItemList.size();
                    for (int i = 0; i < length; i++) {
                        GalleryItemDelegate delegate = new GalleryItemDelegate(galleryItemList.get(i));
                        delegate.setTag(TAG);
                        delegates.add(delegate);
                    }
                    if (index == 1) {
                        mAdapter.clearDataList();
                        mAdapter.addDataItem(mFooterDelegate);
                    }
                    mFooter.setState(length == 0 ? Footer.STATE_NO_MORE : Footer.STATE_SUCCESS);
                    mAdapter.addDataList(mAdapter.getItemCount() - 1, delegates);
                    mIndex = index;
                } else {
                    mFooter.setState(Footer.STATE_FAILED);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onFilterChanged(long state, long category, long studio, long charge) {
        mAdapter.clearDataList();
        mFooter.setState(Footer.STATE_WAITTING);
        mAdapter.addDataItem(mFooterDelegate);
        mAdapter.notifyDataSetChanged();
        loadData(state, category, studio, charge, 1);
    }
}

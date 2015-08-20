package com.metis.coursepart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.fragment.BaseFragment;
import com.metis.base.manager.CacheManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.Footer;
import com.metis.base.widget.callback.OnScrollBottomListener;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.base.widget.adapter.delegate.TypeLayoutProvider;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.decoration.GalleryItemDecoration;
import com.metis.coursepart.manager.CourseManager;
import com.metis.coursepart.module.GalleryItem;
import com.metis.coursepart.adapter.GalleryAdapter;
import com.metis.coursepart.adapter.delegate.GalleryItemDelegate;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Beak on 2015/7/6.
 */
public class CourseGalleryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = CourseGalleryFragment.class.getSimpleName();

    private static CourseGalleryFragment sFragment = null;

    public static CourseGalleryFragment getInstance () {
        if (sFragment == null) {
            sFragment = new CourseGalleryFragment();
        }
        return sFragment;
    }

    private SwipeRefreshLayout mGallerySrl = null;
    private RecyclerView mGalleryRv = null;
    private GalleryAdapter mAdapter = null;

    private GalleryItemDecoration mDecoration = new GalleryItemDecoration();

    private Footer mFooter = null;
    private FooterDelegate mFooterDelegate = null;

    private int mIndex = 1;
    private boolean isLoading = false;

    private CacheManager mCacheManager = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCacheManager = CacheManager.getInstance(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_gallery, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGallerySrl = (SwipeRefreshLayout)view.findViewById(R.id.gallery_swipe_refresh_layout);
        mGalleryRv = (RecyclerView)view.findViewById(R.id.gallery_recycler_view);
        mGalleryRv.setHasFixedSize(true);
        final int spanCount = getResources().getInteger(R.integer.gallery_span_count);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mGalleryRv.setLayoutManager(staggeredGridLayoutManager);
        mGalleryRv.addOnScrollListener(new OnScrollBottomListener() {
            @Override
            public void onScrollBottom(RecyclerView recyclerView, int newState) {
                if (!isLoading) {
                    loadData(mIndex + 1);
                }
            }
        });

        mAdapter = new GalleryAdapter(getActivity());

        mGalleryRv.addItemDecoration(mDecoration);
        mGalleryRv.setAdapter(mAdapter);
        mFooter = new Footer(Footer.STATE_WAITTING);
        mFooterDelegate = new FooterDelegate(mFooter);
        mFooterDelegate.setIsInStaggeredGrid(true);
        mGallerySrl.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_purple
        );
        mGallerySrl.setOnRefreshListener(this);
        mGallerySrl.post(new Runnable() {
            @Override
            public void run() {
                mGallerySrl.setRefreshing(true);
            }
        });
        loadData(1);
        List<GalleryItem> galleryItems = mCacheManager.readUserDataAtDatabase(GalleryItem.class, "galleryItems.db");
        parseData(galleryItems, 1);
    }

    private void loadData (final int index) {
        isLoading = true;
        if (index > 1) {
            mFooter.setState(Footer.STATE_WAITTING);
            mAdapter.notifyDataSetChanged();
        }
        CourseManager.getInstance(getActivity()).getGalleryPicList(0, "", 0, 0, 0, index, new RequestCallback<List<GalleryItem>>() {
            @Override
            public void callback(ReturnInfo<List<GalleryItem>> returnInfo, String callbackId) {
                if (!isAlive()) {
                    return;
                }
                if (returnInfo.isSuccess()) {
                    List<GalleryItem> galleryItems = returnInfo.getData();
                    if (index == 1) {
                        mCacheManager.saveAllUserDataAtDatabase(galleryItems, "galleryItems.db", GalleryItem.class, true);
                    }
                    parseData(galleryItems, index);
                    mIndex = index;
                } else {
                    mFooter.setState(Footer.STATE_FAILED);
                    if (mAdapter.getDataList().contains(mFooterDelegate)) {
                        mAdapter.addDataItem(mFooterDelegate);
                    }
                }
                mAdapter.notifyDataSetChanged();
                if (mGallerySrl.isRefreshing()) {
                    mGallerySrl.setRefreshing(false);
                }
                isLoading = false;
            }
        });
    }

    private void parseData (List<GalleryItem> galleryItems, int index) {
        if (galleryItems == null) {
            return;
        }
        List<GalleryItemDelegate> delegates = new ArrayList<GalleryItemDelegate>();
        final int length = galleryItems.size();
        for (int i = 0; i < length; i++) {
            GalleryItemDelegate delegate = new GalleryItemDelegate(galleryItems.get(i));
            delegate.setTag(TAG);
            delegates.add(delegate);
        }
        if (index == 1) {
            mAdapter.clearDataList();
            mAdapter.addDataItem(mFooterDelegate);
        }
        mAdapter.addDataList(mAdapter.getItemCount() - 1, delegates);

        mFooter.setState(galleryItems.isEmpty() ? Footer.STATE_NO_MORE : Footer.STATE_SUCCESS);
    }

    @Override
    public void onRefresh() {
        loadData(1);
    }
}

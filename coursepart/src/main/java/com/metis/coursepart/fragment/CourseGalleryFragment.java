package com.metis.coursepart.fragment;

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

import com.metis.base.manager.RequestCallback;
import com.metis.base.module.Footer;
import com.metis.base.widget.callback.OnScrollBottomListener;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.base.widget.adapter.delegate.TypeLayoutProvider;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.decoration.GalleryItemDecoration;
import com.metis.coursepart.manager.CourseManager;
import com.metis.coursepart.manager.GalleryCacheManager;
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
public class CourseGalleryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

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

    private FooterDelegate mFooterDelegate = null;

    private int mIndex = 1;
    private boolean isLoading = false;

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
        mFooterDelegate = new FooterDelegate(new Footer(Footer.STATE_WAITTING));
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
    }

    private void loadData (final int index) {
        isLoading = true;
        CourseManager.getInstance(getActivity()).getGalleryPicList(0, "", 0, 0, 0, index, new RequestCallback<List<GalleryItem>>() {
            @Override
            public void callback(ReturnInfo<List<GalleryItem>> returnInfo, String callbackId) {
                Log.v(TAG, "getGalleryPicList " + returnInfo.isSuccess());
                if (!isAdded()) {
                    return;
                }
                if (returnInfo.isSuccess()) {
                    Log.v(TAG, "getGalleryPicList " + returnInfo.getData().size());
                    List<GalleryItem> galleryItems = returnInfo.getData();
                    List<GalleryItemDelegate> delegates = new ArrayList<GalleryItemDelegate>();
                    final int length = galleryItems.size();
                    for (int i = 0; i < length; i++) {
                        delegates.add(new GalleryItemDelegate(galleryItems.get(i)));
                    }
                    if (index == 1) {
                        mAdapter.clearDataList();
                        mAdapter.addDataItem(mFooterDelegate);
                        GalleryCacheManager.getInstance(getActivity()).clearGalleryItemList();
                    }
                    mAdapter.addDataList(mAdapter.getItemCount() - 1, delegates);
                    GalleryCacheManager.getInstance(getActivity()).addAll(galleryItems);

                    mAdapter.notifyDataSetChanged();
                    mIndex = index;
                }
                mGallerySrl.setRefreshing(false);
                isLoading = false;
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData(1);
    }
}

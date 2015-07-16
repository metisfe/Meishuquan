package com.metis.coursepart.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class CourseGalleryFragment extends Fragment {

    private static final String TAG = CourseGalleryFragment.class.getSimpleName();

    private static CourseGalleryFragment sFragment = null;

    public static CourseGalleryFragment getInstance () {
        if (sFragment == null) {
            sFragment = new CourseGalleryFragment();
        }
        return sFragment;
    }

    static {
        TypeLayoutProvider.put(1, R.layout.layout_load_more_footer);
        TypeLayoutProvider.put(2, R.layout.layout_gallery_item);
    }

    private RecyclerView mGalleryRv = null;
    private GalleryAdapter mAdapter = null;

    private GalleryItemDecoration mDecoration = new GalleryItemDecoration();

    private FooterDelegate mFooterDelegate = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_gallery, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGalleryRv = (RecyclerView)view.findViewById(R.id.gallery_recycler_view);
        final int spanCount = getResources().getInteger(R.integer.gallery_span_count);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mGalleryRv.setLayoutManager(staggeredGridLayoutManager);
        mGalleryRv.addOnScrollListener(new OnScrollBottomListener() {
            @Override
            public void onScrollBottom(RecyclerView recyclerView, int newState) {

            }
        });

        mAdapter = new GalleryAdapter(getActivity());

        mGalleryRv.addItemDecoration(mDecoration);
        mGalleryRv.setAdapter(mAdapter);
        mFooterDelegate = new FooterDelegate(new Footer(Footer.STATE_WAITTING));
        mFooterDelegate.setIsInStaggeredGrid(true);
        loadData(1);
    }

    private void loadData (final int index) {
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
                        GalleryCacheManager.getInstance(getActivity()).clearGalleryItemList();
                    }
                    mAdapter.addDataList(delegates);
                    GalleryCacheManager.getInstance(getActivity()).addAll(galleryItems);
                    mAdapter.addDataItem(mFooterDelegate);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}

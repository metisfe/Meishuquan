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

import com.metis.base.module.Footer;
import com.metis.base.widget.callback.OnScrollBottomListener;
import com.metis.base.widget.delegate.FooterDelegate;
import com.metis.base.widget.delegate.TypeLayoutProvider;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.decoration.GalleryItemDecoration;
import com.metis.coursepart.module.GalleryItem;
import com.metis.coursepart.adapter.GalleryAdapter;
import com.metis.coursepart.adapter.delegate.GalleryItemDelegate;

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
        //staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mGalleryRv.setLayoutManager(staggeredGridLayoutManager);
        mGalleryRv.addOnScrollListener(new OnScrollBottomListener() {
            @Override
            public void onScrollBottom(RecyclerView recyclerView, int newState) {
                Log.v(TAG, "onScrollBottom happened");
                if (mAdapter.getItemCount() < 100) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<GalleryItem> galleryItems = FakeDataFactory.make();
                            List<GalleryItemDelegate> delegates = new ArrayList<GalleryItemDelegate>();
                            for (GalleryItem item : galleryItems) {
                                delegates.add(new GalleryItemDelegate(getActivity(), item));
                            }
                            mAdapter.addDataList(mAdapter.getItemCount() - 1, delegates);
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 3 * 1000);

                }

            }
        });

        mAdapter = new GalleryAdapter(getActivity());

        List<GalleryItem> galleryItems = FakeDataFactory.make();
        List<GalleryItemDelegate> delegates = new ArrayList<GalleryItemDelegate>();
        for (GalleryItem item : galleryItems) {
            delegates.add(new GalleryItemDelegate(getActivity(), item));
        }
        mAdapter.addDataList(delegates);
        FooterDelegate footerDelegate = new FooterDelegate(new Footer());
        footerDelegate.setIsInStaggeredGrid(true);
        mAdapter.addDataItem(footerDelegate);

        mGalleryRv.addItemDecoration(mDecoration);
        mGalleryRv.setAdapter(mAdapter);
    }

    public static class FakeDataFactory {

        private static String[] urls = {
                "https://metisfile.blob.core.chinacloudapi.cn/asset-e522435d-1500-80c4-21da-f1e524b34b32/201507071020239529.mp4?sv=2012-02-12&sr=c&si=83dcee9d-1a05-43f2-a62b-64a2276e5a99&sig=V7imC8TXQmdzG4W8J89RyU1%2B2pqkEJCcrQB4sR0Kevg%3D&se=2016-07-06T14%3A20%3A26Z",
                "https://metisfile.blob.core.chinacloudapi.cn/asset-e522435d-1500-80c3-93e5-f1e51f996d37/20150701103232335.mp4?sv=2012-02-12&sr=c&si=f0095b87-22c8-4ad8-9034-228d794841ae&sig=87cOHxrqWt2dUf7Y7oG7z81cCiiWhFpxBNJbUr6bND8%3D&se=2016-06-30T02%3A32%3A34Z",
                "https://metisfile.blob.core.chinacloudapi.cn/asset-932c435d-1500-80c3-edd0-f1e51bce6acc/201506260241372669.mp4?sv=2012-02-12&sr=c&si=213f6a65-d37c-40a2-9f96-d224c92659b7&sig=cBulNhcFiBx8Dfb99fUgBWoKKodrKd1mQd9ft0eyUnY%3D&se=2016-06-25T06%3A41%3A50Z",
        };

        private static String[] mImages = {
                "http://pic.miercn.com/uploads/allimg/150709/40-150FZSP0.jpg",
                "http://pic.miercn.com/uploads/allimg/150709/40-150FZSR4.jpg",
                "http://pic.miercn.com/uploads/allimg/150709/40-150FZST0.jpg",
                "http://pic.miercn.com/uploads/allimg/150709/40-150FZSU6.jpg",
                "http://pic.miercn.com/uploads/allimg/150709/40-150FZSZ9.jpg",
                "http://pic.miercn.com/uploads/allimg/150709/40-150FZS936.jpg",
                "http://pic.miercn.com/uploads/allimg/150709/40-150FZS950.jpg",
                /*"http://img1.imgtn.bdimg.com/it/u=3299441974,1994946559&fm=21&gp=0.jpg",
                "http://img4.imgtn.bdimg.com/it/u=2253211935,925058203&fm=21&gp=0.jpg",
                "http://img1.imgtn.bdimg.com/it/u=322741655,1670014436&fm=21&gp=0.jpg",
                "http://img5.imgtn.bdimg.com/it/u=1362128138,1143864339&fm=21&gp=0.jpg",
                "http://img5.imgtn.bdimg.com/it/u=482941909,2782375774&fm=21&gp=0.jpg",
                "http://img0.imgtn.bdimg.com/it/u=3285225847,11870555&fm=21&gp=0.jpg",
                "http://img5.imgtn.bdimg.com/it/u=3130206214,507191485&fm=21&gp=0.jpg",
                "http://img1.imgtn.bdimg.com/it/u=2394649328,757682711&fm=21&gp=0.jpg",
                "http://img4.imgtn.bdimg.com/it/u=2659067893,4290506443&fm=21&gp=0.jpg",
                "http://img1.imgtn.bdimg.com/it/u=1268793931,1056642096&fm=23&gp=0.jpg",
                "http://img2.imgtn.bdimg.com/it/u=1580373193,3038376875&fm=21&gp=0.jpg",
                "http://img0.imgtn.bdimg.com/it/u=891394858,1724018534&fm=21&gp=0.jpg",
                "http://img0.imgtn.bdimg.com/it/u=2830469808,4260396754&fm=21&gp=0.jpg",*/
        };

        public static List<GalleryItem> make () {
            List<GalleryItem> galleryItems = new ArrayList<GalleryItem>();
            Random random = new Random();
            for (String str : mImages) {
                GalleryItem item = new GalleryItem();
                item.url = str;
                item.count = random.nextInt(400) + 20;
                item.tags = new ArrayList<>();
                item.tags.add("ZDD");
                item.tags.add("aaa");
                item.source = "XXX";
                galleryItems.add(item);
            }
            return galleryItems;
        }
    }

}

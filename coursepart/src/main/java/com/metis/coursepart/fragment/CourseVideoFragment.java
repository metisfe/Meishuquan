package com.metis.coursepart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.fragment.BaseFragment;
import com.metis.base.manager.CacheManager;
import com.metis.base.manager.RequestCallback;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.AlbumContainerAdapter;
import com.metis.coursepart.adapter.decoration.VideoItemDecoration;
import com.metis.coursepart.adapter.delegate.AlbumContainerDelegate;
import com.metis.coursepart.manager.CourseManager;
import com.metis.coursepart.module.CourseAlbum;
import com.metis.coursepart.module.MainCourseList;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/6.
 */
public class CourseVideoFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = CourseVideoFragment.class.getSimpleName();

    private static CourseVideoFragment sFragment = null;

    public static CourseVideoFragment getInstance () {
        if (sFragment == null) {
            sFragment = new CourseVideoFragment();
        }
        return sFragment;
    }

    private SwipeRefreshLayout mVideoSrl = null;
    private RecyclerView mVideoRv = null;

    private AlbumContainerAdapter mAdapter = null;

    private MainCourseList mCourseList = null;

    private CacheManager mCacheManager = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCacheManager = CacheManager.getInstance(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_video, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVideoSrl = (SwipeRefreshLayout)view.findViewById(R.id.course_video_swipe_refresh_layout);
        mVideoRv = (RecyclerView)view.findViewById(R.id.course_video_recycler_view);

        mVideoSrl.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_purple
        );
        mVideoSrl.setOnRefreshListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mVideoRv.setLayoutManager(linearLayoutManager);
        mVideoRv.addItemDecoration(new VideoItemDecoration());

        mAdapter = new AlbumContainerAdapter(getActivity());
        mVideoRv.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /* else {
            parseCourseList(mCourseList);
        }*/
        loadData();
        List<CourseAlbum> topCourse = mCacheManager.readUserDataAtDatabase(CourseAlbum.class, "topCourse.db");
        List<CourseAlbum> newestCourse = mCacheManager.readUserDataAtDatabase(CourseAlbum.class, "newestCourse.db");
        List<CourseAlbum> hottestCourse = mCacheManager.readUserDataAtDatabase(CourseAlbum.class, "hottestCourse.db");
        List<CourseAlbum> recommendCourse = mCacheManager.readUserDataAtDatabase(CourseAlbum.class, "recommendCourse.db");
        MainCourseList cacheCourseList = new MainCourseList();
        cacheCourseList.topCourse = topCourse;
        cacheCourseList.newestCourse = newestCourse;
        cacheCourseList.hottestCourse = hottestCourse;
        cacheCourseList.recommendCourse = recommendCourse;
        parseCourseList(cacheCourseList);
        mVideoSrl.post(new Runnable() {
            @Override
            public void run() {
                mVideoSrl.setRefreshing(true);
            }
        });
    }

    private void loadData () {

        CourseManager.getInstance(getActivity()).getMainCourseList(new RequestCallback<MainCourseList>() {
            @Override
            public void callback(ReturnInfo<MainCourseList> returnInfo, String callbackId) {
                if (!isAlive()) {
                    return;
                }
                if (returnInfo.isSuccess()) {
                    mCourseList = returnInfo.getData();

                    mCacheManager.saveAllUserDataAtDatabase(mCourseList.topCourse, "topCourse.db", CourseAlbum.class, true);
                    mCacheManager.saveAllUserDataAtDatabase(mCourseList.newestCourse, "newestCourse.db", CourseAlbum.class, true);
                    mCacheManager.saveAllUserDataAtDatabase(mCourseList.hottestCourse, "hottestCourse.db", CourseAlbum.class, true);
                    mCacheManager.saveAllUserDataAtDatabase(mCourseList.recommendCourse, "recommendCourse.db", CourseAlbum.class, true);
                    parseCourseList(mCourseList);
                }
                if (mVideoSrl.isRefreshing()) {
                    mVideoSrl.setRefreshing(false);
                }
            }
        });
    }

    private void parseCourseList (MainCourseList mainCourseList) {
        List<AlbumContainerDelegate> delegateList = new ArrayList<AlbumContainerDelegate>();
        //TODO
//        List<CourseAlbum> topList = mainCourseList.topCourse;
//        Log.v(TAG, "topList=" + topList.size());
//        if (topList != null && !topList.isEmpty()) {
//            topList.get(0).setChannel(getString(R.string.course_top));
//            delegateList.add(new AlbumContainerDelegate(topList));
//        }
        List<CourseAlbum> recommentedList = mainCourseList.recommendCourse;
        if (recommentedList != null && !recommentedList.isEmpty()) {
            recommentedList.get(0).setChannel(getString(R.string.course_recommend));
            AlbumContainerDelegate delegate = new AlbumContainerDelegate(recommentedList);
            delegate.setFilterId(1);
            delegateList.add(delegate);
        }
        List<CourseAlbum> newList = mainCourseList.newestCourse;
        if (newList != null && !newList.isEmpty()) {
            newList.get(0).setChannel(getString(R.string.course_new));
            AlbumContainerDelegate delegate = new AlbumContainerDelegate(newList);
            delegate.setFilterId(2);
            delegateList.add(delegate);
        }
        List<CourseAlbum> hotList = mainCourseList.hottestCourse;
        if (hotList != null && !hotList.isEmpty()) {
            hotList.get(0).setChannel(getString(R.string.course_hot));
            AlbumContainerDelegate delegate = new AlbumContainerDelegate(hotList);
            delegate.setFilterId(3);
            delegateList.add(delegate);
        }

        mAdapter.clearDataList();
        mAdapter.addDataList(delegateList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}

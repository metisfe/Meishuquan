package com.metis.coursepart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.coursepart.R;
import com.metis.coursepart.adapter.VideoSubAdapter;
import com.metis.coursepart.adapter.delegate.VideoAlbumDelegate;
import com.metis.coursepart.adapter.delegate.VideoItemDelegate;
import com.metis.coursepart.module.CourseAlbum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/8.
 */
public class CourseVideoDetailFragment extends Fragment {

    private static CourseVideoDetailFragment sFragment = new CourseVideoDetailFragment();

    public static CourseVideoDetailFragment getInstance () {
        return sFragment;
    }

    private RecyclerView mDetailRv = null;

    private VideoSubAdapter mAdapter = null;

    private List<CourseAlbum> mRelatedCourseList = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_video_detail, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDetailRv = (RecyclerView)view.findViewById(R.id.detail_recycler_view);
        mDetailRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new VideoSubAdapter(getActivity());
        mDetailRv.setAdapter(mAdapter);
    }

    public void setRelatedCourses (List<CourseAlbum> albumList) {
        if (albumList == null) {
            return;
        }
        if (mRelatedCourseList == null) {
            mRelatedCourseList = albumList;
            List<VideoAlbumDelegate> delegates = new ArrayList<VideoAlbumDelegate>();
            final int length = albumList.size();
            for (int i = 0; i < length; i++) {
                delegates.add(new VideoAlbumDelegate(albumList.get(i)));
            }
            mAdapter.addDataList(delegates);
            mAdapter.notifyDataSetChanged();
        }
    }
}

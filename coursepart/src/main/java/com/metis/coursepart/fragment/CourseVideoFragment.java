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
import com.metis.coursepart.adapter.VideoAdapter;
import com.metis.coursepart.manager.CourseManager;

/**
 * Created by Beak on 2015/7/6.
 */
public class CourseVideoFragment extends Fragment {

    private static CourseVideoFragment sFragment = null;

    public static CourseVideoFragment getInstance () {
        if (sFragment == null) {
            sFragment = new CourseVideoFragment();
        }
        return sFragment;
    }

    private RecyclerView mVideoRv = null;

    private VideoAdapter mAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_video, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVideoRv = (RecyclerView)view.findViewById(R.id.course_video_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mVideoRv.setLayoutManager(linearLayoutManager);

        mAdapter = new VideoAdapter(getActivity());
        mVideoRv.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CourseManager.getInstance(getActivity()).getMainCourseList(null);
    }
}

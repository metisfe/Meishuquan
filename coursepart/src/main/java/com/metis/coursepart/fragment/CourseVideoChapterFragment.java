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

/**
 * Created by Beak on 2015/7/8.
 */
public class CourseVideoChapterFragment extends Fragment {

    private static CourseVideoChapterFragment sFragment = new CourseVideoChapterFragment();

    public static CourseVideoChapterFragment getInstance () {
        return sFragment;
    }

    private RecyclerView mChapterRv = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_video_chapter, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mChapterRv = (RecyclerView)view.findViewById(R.id.chapter_recycler_view);

        mChapterRv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}

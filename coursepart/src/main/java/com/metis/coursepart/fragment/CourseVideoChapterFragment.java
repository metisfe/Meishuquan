package com.metis.coursepart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.metis.base.fragment.BaseFragment;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.CourseAdapter;
import com.metis.coursepart.adapter.decoration.VideoItemDetailDecoration;
import com.metis.coursepart.adapter.decoration.VideoItemSmallDecoration;
import com.metis.coursepart.adapter.delegate.CourseDelegate;
import com.metis.coursepart.adapter.delegate.CourseTitleDelegate;
import com.metis.coursepart.module.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/8.
 */
public class CourseVideoChapterFragment extends BaseFragment {

    private static CourseVideoChapterFragment sFragment = new CourseVideoChapterFragment();

    public static CourseVideoChapterFragment getInstance () {
        return sFragment;
    }

    private RecyclerView mChapterRv = null;

    private List<Course> mCourseList = null;

    private CourseAdapter mAdapter = null;

    private CourseAdapter.OnCourseClickListener mCourseClickListener = null;

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
        mChapterRv.addItemDecoration(new VideoItemDetailDecoration());
        mAdapter = new CourseAdapter(getActivity());
        mChapterRv.setAdapter(mAdapter);

        setOnCourseClickListener(mCourseClickListener);
    }

    public void setSubCourseList(List<Course> courseList) {
        if (!isAdded()) {
            return;
        }
        if (mCourseList == null) {
            List<CourseDelegate> delegates = new ArrayList<CourseDelegate>();
            mCourseList = courseList;
            final int length = mCourseList.size();
            for (int i = 0; i < length; i++) {
                delegates.add(new CourseDelegate(courseList.get(i)));
            }
            mAdapter.addDataItem(new CourseTitleDelegate(getString(R.string.course_related_count, length)));
            mAdapter.addDataList(delegates);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setOnCourseClickListener (CourseAdapter.OnCourseClickListener listener) {
        mCourseClickListener = listener;
        if (mAdapter != null) {
            mAdapter.setOnCourseClickListener(mCourseClickListener);
        }
    }

    public void notifyDataSetChanged () {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.clearDataList();
            mAdapter = null;
        }
    }
}

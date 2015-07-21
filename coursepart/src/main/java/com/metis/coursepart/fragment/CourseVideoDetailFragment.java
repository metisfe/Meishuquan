package com.metis.coursepart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.fragment.BaseFragment;
import com.metis.base.utils.Log;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.AlbumAdapter;
import com.metis.coursepart.adapter.decoration.UserInDetailDecoration;
import com.metis.coursepart.adapter.decoration.VideoItemSmallDecoration;
import com.metis.coursepart.adapter.delegate.AlbumSmallDelegate;
import com.metis.coursepart.adapter.delegate.ItemTitleDelegate;
import com.metis.coursepart.adapter.delegate.UserInDetailDelegate;
import com.metis.coursepart.module.Course;
import com.metis.coursepart.module.CourseAlbum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/8.
 */
public class CourseVideoDetailFragment extends BaseFragment {

    private static final String TAG = CourseVideoDetailFragment.class.getSimpleName();

    private static CourseVideoDetailFragment sFragment = new CourseVideoDetailFragment();

    public static CourseVideoDetailFragment getInstance () {
        return sFragment;
    }

    private RecyclerView mDetailRv = null;

    private AlbumAdapter mAdapter = null;

    private List<CourseAlbum> mRelatedCourseList = null;

    private CourseAlbum mCourseAlbum = null;
    private UserInDetailDelegate mUserDelegate = null;
    private Course mCurrentCourse = null;

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

        mAdapter = new AlbumAdapter(getActivity());

        mDetailRv.setAdapter(mAdapter);
        mDetailRv.addItemDecoration(new UserInDetailDecoration());
        mDetailRv.addItemDecoration(new VideoItemSmallDecoration());

        setCourseAlbum(mCourseAlbum);
        setCurrentCourse(mCurrentCourse);
    }

    public void setRelatedCourses (List<CourseAlbum> albumList) {
        if (albumList == null) {
            return;
        }
        if (mRelatedCourseList == null) {
            mRelatedCourseList = albumList;
            List<AlbumSmallDelegate> delegates = new ArrayList<AlbumSmallDelegate>();
            final int length = albumList.size();
            for (int i = 0; i < length; i++) {
                delegates.add(new AlbumSmallDelegate(albumList.get(i)));
            }
            mAdapter.addDataItem(new ItemTitleDelegate(getString(R.string.course_related)));
            mAdapter.addDataList(delegates);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setCourseAlbum(CourseAlbum album) {
        mCourseAlbum = album;
        if (!isAlive()) {
            return;
        }
        if (mAdapter != null && mUserDelegate == null) {
            mUserDelegate = new UserInDetailDelegate(mCourseAlbum);
            mAdapter.addDataItem(0, mUserDelegate);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setCurrentCourse(Course course) {
        mCurrentCourse = course;
        if (!isAlive()) {
            return;
        }
        Log.v(TAG, "mUserDelegate != null -- " + (mUserDelegate != null) + " course=" + course);
        if (mUserDelegate != null && course != null) {
            mUserDelegate.setWebContent(course.webContent);
            mUserDelegate.setCurrentCourse(course);
            mUserDelegate.setContentItemList(course.getContentItemList());
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

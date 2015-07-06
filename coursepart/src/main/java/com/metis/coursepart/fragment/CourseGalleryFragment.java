package com.metis.coursepart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.coursepart.R;

/**
 * Created by Beak on 2015/7/6.
 */
public class CourseGalleryFragment extends Fragment {
    private static CourseGalleryFragment sFragment = null;

    public static CourseGalleryFragment getInstance () {
        if (sFragment == null) {
            sFragment = new CourseGalleryFragment();
        }
        return sFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_gallery, null, true);
    }
}

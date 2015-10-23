package com.metis.coursepart.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.fragment.AbsPagerFragment;
import com.metis.base.fragment.BaseFragment;
import com.metis.coursepart.R;

/**
 * Created by Beak on 2015/10/17.
 */
public class CourseVideoDiscussFragment extends AbsPagerFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_video_discuss, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public CharSequence getTitle(Context context) {
        return context.getString(R.string.tab_discuss);
    }
}

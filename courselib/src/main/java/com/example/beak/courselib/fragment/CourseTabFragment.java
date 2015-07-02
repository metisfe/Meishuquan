package com.example.beak.courselib.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.beak.courselib.R;
import com.metis.base.fragment.DockFragment;
import com.metis.base.widget.dock.DockBar;

/**
 * Created by Beak on 2015/7/2.
 */
public class CourseTabFragment extends DockFragment {

    private static CourseTabFragment sFragment = new CourseTabFragment();

    public static CourseTabFragment getInstance () {
        return sFragment;
    }

    private DockBar.Dock mDock = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_tab, null, true);
    }

    @Override
    public DockBar.Dock getDock(Context context) {
        if (mDock == null) {
            mDock = new DockBar.Dock(context, 1, android.R.drawable.alert_dark_frame, android.R.string.cancel, this);
        }
        return mDock;
    }
}

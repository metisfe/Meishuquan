package com.example.beak.courselib.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

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

    @Override
    public DockBar.Dock getDock(Context context) {
        return new DockBar.Dock(context, 1, android.R.drawable.alert_dark_frame, android.R.string.cancel, this);
    }
}

package com.metis.coursepart.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.metis.base.fragment.DockFragment;
import com.metis.base.framework.NetProxy;
import com.metis.base.widget.dock.DockBar;
import com.metis.coursepart.R;
import com.metis.coursepart.modules.Channel;
import com.metis.coursepart.modules.ChannelCollection;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;

/**
 * Created by Beak on 2015/7/2.
 */
public class CourseTabFragment extends DockFragment {

    private static final String TAG = CourseTabFragment.class.getSimpleName();

    private static CourseTabFragment sFragment = new CourseTabFragment();

    public static CourseTabFragment getInstance () {
        return sFragment;
    }

    private DockBar.Dock mDock = null;

    Button mTestBtn = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_tab, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTestBtn = (Button)view.findViewById(R.id.course_test);
        mTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public DockBar.Dock getDock(Context context) {
        if (mDock == null) {
            mDock = new DockBar.Dock(context, 1, android.R.drawable.alert_dark_frame, android.R.string.cancel, this);
        }
        return mDock;
    }
}

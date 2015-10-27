package com.metis.coursepart.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.metis.base.fragment.AbsPagerFragment;
import com.metis.base.fragment.BaseFragment;
import com.metis.coursepart.R;

/**
 * Created by Beak on 2015/10/17.
 */
public class CourseVideoDiscussFragment extends AbsPagerFragment implements View.OnClickListener{

    private RecyclerView mDiscussionRv = null;

    private ImageView mDownloadIv, mSubscriptIv, mShareIv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_video_discuss, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDiscussionRv = (RecyclerView)view.findViewById(R.id.discussion_recycler_view);

        mDiscussionRv.setLayoutManager(new LinearLayoutManager(getContext()));

        mDownloadIv = (ImageView)view.findViewById(R.id.discussion_control_download);
        mSubscriptIv = (ImageView)view.findViewById(R.id.discussion_control_subscript);
        mShareIv = (ImageView)view.findViewById(R.id.discussion_control_share);

        mDownloadIv.setOnClickListener(this);
        mSubscriptIv.setOnClickListener(this);
        mShareIv.setOnClickListener(this);
    }

    @Override
    public CharSequence getTitle(Context context) {
        return context.getString(R.string.tab_discuss);
    }

    @Override
    public void onClick(View v) {

    }
}

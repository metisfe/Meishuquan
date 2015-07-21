package com.metis.coursepart.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import com.metis.base.fragment.BaseFragment;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.callback.OnScrollBottomListener;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.AlbumAdapter;
import com.metis.coursepart.module.CourseType;
import com.metis.coursepart.module.StudioInfo;

import java.util.List;

/**
 * Created by Beak on 2015/7/14.
 */
public abstract class BaseFilterFragment extends BaseFragment {

    private static final String TAG = BaseFilterFragment.class.getSimpleName();

    private FilterPanelFragment mFilterPanelFragment = null;
    private RecyclerView mDataRv = null;
    private FrameLayout mFragmentContainer = null;

    private List<CourseType> mCourseTypeList = null;
    private List<StudioInfo> mStudioList = null;

    private long mState = 1;

    private AlbumAdapter mAdapter = null;

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        private int mHeight = 0;
        private int mLastDy = 0;

        private int mTotalDy = 0;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mHeight == 0) {
                mHeight = mFragmentContainer.getHeight();
            }
            ObjectAnimator animator = null;
            switch (newState) {
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    if (animator != null && animator.isRunning()) {
                        animator.cancel();
                    }
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    break;
                case RecyclerView.SCROLL_STATE_IDLE:
                    final float transY = mFragmentContainer.getTranslationY();
                    if (transY == 0 || transY == -mHeight) {
                        return;
                    }
                    if (mLastDy > 0) {
                        animator = animateTranslationY(transY, -mHeight);
                        return;
                    }
                    if (mLastDy < 0) {
                        animator = animateTranslationY(transY, 0);
                    }
                    break;
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mTotalDy += dy;
            final float transY = mFragmentContainer.getTranslationY();
            if (transY == 0 && dy < 0) {
                return;
            }
            if (transY == -mHeight && dy > 0) {
                return;
            }
            if (transY < -mHeight) {
                mFragmentContainer.setTranslationY(-mHeight);
            } else if (transY > 0) {
                mFragmentContainer.setTranslationY(0);
            } else {
                if (dy > 0) {
                    if (mTotalDy > mHeight) {
                        mFragmentContainer.setTranslationY(transY - dy);
                    }
                } else {
                    mFragmentContainer.setTranslationY(transY - dy);
                }
            }
            mLastDy = dy;
            //Log.v(TAG, "mTotalDy=" + mTotalDy + " " + recyclerView.getSc);
        }

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_filter, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentContainer = (FrameLayout)view.findViewById(R.id.filter_fragment_container);
        mFilterPanelFragment = (FilterPanelFragment)getChildFragmentManager().findFragmentById(R.id.filter_fragment);
        mDataRv = (RecyclerView)view.findViewById(R.id.filter_recycler_view);
        mDataRv.setLayoutManager(getLayoutManager());
        mDataRv.setAdapter(getAdapter());
        mDataRv.addOnScrollListener(new OnScrollBottomListener() {
            @Override
            public void onScrollBottom(RecyclerView recyclerView, int newState) {
                BaseFilterFragment.this.onScrollBottom();
            }
        });
        mDataRv.addOnScrollListener(mScrollListener);

        setCourseTypeList(mCourseTypeList);
        setStudioList(mStudioList);
    }

    private ObjectAnimator animateTranslationY(float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFragmentContainer, "translationY", from, to);
        animator.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
        return animator;
    }

    public void showPanel () {
        animateTranslationY(mFragmentContainer.getTranslationY(), 0);
    }

    public FilterPanelFragment getFilterPanelFragment () {
        return mFilterPanelFragment;
    }

    public RecyclerView getRecyclerView () {
        return mDataRv;
    }

    public void onScrollBottom () {

    }

    public void setCourseTypeList (List<CourseType> courseTypeList) {
        mCourseTypeList = courseTypeList;
        if (getFilterPanelFragment() != null && courseTypeList != null) {
            getFilterPanelFragment().setCourseTypeList(courseTypeList);
        }
    }

    public void setStudioList (List<StudioInfo> studioList) {
        mStudioList = studioList;
        if (getFilterPanelFragment() != null && studioList != null) {
            getFilterPanelFragment().setStudioList(studioList);
        }
    }

    public void setCurrentState (long state) {
        mState = state;
        if (getFilterPanelFragment() != null) {
            getFilterPanelFragment().setCurrentState(state);
        }
    }

    public long getCurrentState () {
        return mState;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAdapter() != null) {
            getAdapter().clearDataList();
        }
    }

    public abstract RecyclerView.LayoutManager getLayoutManager ();
    public abstract DelegateAdapter getAdapter ();
}

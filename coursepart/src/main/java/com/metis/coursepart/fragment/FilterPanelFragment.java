package com.metis.coursepart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.fragment.BaseFragment;
import com.metis.base.module.User;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.FilterAdapter;
import com.metis.base.widget.adapter.lookup.FilterSpanSizeLookup;
import com.metis.coursepart.adapter.delegate.CourseTypeFilterDelegate;
import com.metis.coursepart.adapter.delegate.FilterDelegate;
import com.metis.coursepart.adapter.delegate.StudioFilterDelegate;
import com.metis.coursepart.module.CourseType;
import com.metis.coursepart.module.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyunfei on 15/7/12.
 */
public class FilterPanelFragment extends BaseFragment {

    private static final String TAG = FilterPanelFragment.class.getSimpleName();

    private Filter[] mStateFilterArray = {new Filter(1, R.string.filter_recommend), new Filter(2, R.string.filter_new), new Filter(3, R.string.filter_hot)};
    private Filter[] mChargeFilterArray = {Filter.ALL, new Filter(1, R.string.filter_free), new Filter(2, R.string.filter_charge)};

    private RecyclerView mStateFilterRv, mCategoryFilterRv, mStudioFilterRv, mChargeRuleFilterRv;

    private FilterAdapter mStateAdapter, mCategoryAdapter, mStudioAdapter, mChargeRuleAdapter;

    private OnFilterChangeListener mFilterChangeListener = null;

    private long mCurrentState = 1, mCurrentCategory, mCurrentStudio, mCurrentCharge;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_panel_filter, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStateFilterRv = (RecyclerView)view.findViewById(R.id.filter_state_recycler_view);
        mCategoryFilterRv = (RecyclerView)view.findViewById(R.id.filter_category_recycler_view);
        mStudioFilterRv = (RecyclerView)view.findViewById(R.id.filter_studio_recycler_view);
        mChargeRuleFilterRv = (RecyclerView)view.findViewById(R.id.filter_charge_rule_recycler_view);

        GridLayoutManager categoryGlm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        categoryGlm.setSpanSizeLookup(new FilterSpanSizeLookup());

        GridLayoutManager studioGlm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        studioGlm.setSpanSizeLookup(new FilterSpanSizeLookup());

        mStateFilterRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mCategoryFilterRv.setLayoutManager(categoryGlm);
        mStudioFilterRv.setLayoutManager(studioGlm);
        mChargeRuleFilterRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initStateRv();
        initChargeRv();
    }

    public void setCurrentState (long state) {
        mCurrentState = state;
        if (mStateAdapter != null) {
            mStateAdapter.setSelectedFilterId(state);
        }
    }

    private void initStateRv () {
        mStateAdapter = new FilterAdapter(getActivity());
        List<FilterDelegate> stateDelegates = new ArrayList<FilterDelegate>();
        final int stateLength = mStateFilterArray.length;
        for (int i = 0; i < stateLength; i++) {
            FilterDelegate delegate = new FilterDelegate(mStateFilterArray[i]);
            delegate.setSelected(i == 0);
            stateDelegates.add(delegate);
        }
        mStateAdapter.addDataList(stateDelegates);
        mStateFilterRv.setAdapter(mStateAdapter);
        setCurrentState(mCurrentState);
        mCurrentState = mStateAdapter.getSelectedFilterId();
        mStateAdapter.setOnFilterSelectedListener(new FilterAdapter.OnFilterSelectedListener() {
            @Override
            public void onSelected(int position, long id) {
                if (id != mCurrentState) {
                    mCurrentState = id;
                    onChange();
                }
            }
        });
    }

    private void initChargeRv () {
        mChargeRuleAdapter = new FilterAdapter(getActivity());
        List<FilterDelegate> delegates = new ArrayList<FilterDelegate>();
        final int length = mChargeFilterArray.length;
        for (int i = 0; i < length; i++) {
            FilterDelegate delegate = new FilterDelegate(mChargeFilterArray[i]);
            delegate.setSelected(i == 0);
            delegates.add(delegate);
        }
        mChargeRuleAdapter.addDataList(delegates);
        mChargeRuleFilterRv.setAdapter(mChargeRuleAdapter);
        mCurrentCharge = mChargeRuleAdapter.getSelectedFilterId();
        mChargeRuleAdapter.setOnFilterSelectedListener(new FilterAdapter.OnFilterSelectedListener() {
            @Override
            public void onSelected(int position, long id) {
                if (id != mCurrentCharge) {
                    mCurrentCharge = id;
                    onChange();
                }
            }
        });
    }

    public void setCourseTypeList (List<CourseType> courseTypeList) {
        if (courseTypeList == null || courseTypeList.isEmpty()) {
            return;
        }
        final int length = courseTypeList.size();
        mCategoryFilterRv.setVisibility(View.VISIBLE);
        mCategoryAdapter = new FilterAdapter(getActivity());

        List<CourseTypeFilterDelegate> delegates = new ArrayList<CourseTypeFilterDelegate>();
        for (int i = 0; i < length; i++) {
            CourseTypeFilterDelegate delegate = new CourseTypeFilterDelegate(courseTypeList.get(i));
            delegates.add(delegate);
        }
        FilterDelegate allDelegate = new FilterDelegate(Filter.ALL);
        allDelegate.setSelected(true);
        mCategoryAdapter.addDataItem(allDelegate);
        mCategoryAdapter.addDataList(delegates);
        mCategoryFilterRv.setAdapter(mCategoryAdapter);
        mCurrentCategory = mCategoryAdapter.getSelectedFilterId();
        mCategoryAdapter.setOnFilterSelectedListener(new FilterAdapter.OnFilterSelectedListener() {
            @Override
            public void onSelected(int position, long id) {
                if (id != mCurrentCategory) {
                    mCurrentCategory = id;
                    onChange();
                }
            }
        });
    }

    public void setStudioList (List<User> studioList) {
        if (studioList == null || studioList.isEmpty()) {
            return;
        }
        mStudioFilterRv.setVisibility(View.VISIBLE);
        final int length = studioList.size();
        mStudioAdapter = new FilterAdapter(getActivity());
        List<StudioFilterDelegate> delegates = new ArrayList<StudioFilterDelegate>();
        for (int i = 0; i < length; i++) {
            StudioFilterDelegate delegate = new StudioFilterDelegate(studioList.get(i));
            delegates.add(delegate);
        }
        FilterDelegate allDelegate = new FilterDelegate(Filter.ALL);
        allDelegate.setSelected(true);
        mStudioAdapter.addDataItem(allDelegate);
        mStudioAdapter.addDataList(delegates);
        mStudioFilterRv.setAdapter(mStudioAdapter);
        mCurrentStudio = mStudioAdapter.getSelectedFilterId();
        mStudioAdapter.setOnFilterSelectedListener(new FilterAdapter.OnFilterSelectedListener() {
            @Override
            public void onSelected(int position, long id) {
                if (id != mCurrentStudio) {
                    mCurrentStudio = id;
                    onChange();
                }
            }
        });
    }

    public long getCurrentState() {
        return mCurrentState;
    }

    public long getCurrentCategory() {
        return mCurrentCategory;
    }

    public long getCurrentStudio() {
        return mCurrentStudio;
    }

    public long getCurrentCharge() {
        return mCurrentCharge;
    }

    private void onChange () {
        if (mFilterChangeListener != null) {
            mFilterChangeListener.onFilterChanged(mCurrentState, mCurrentCategory, mCurrentStudio, mCurrentCharge);
        }
    }

    public void setOnFilterChangeListener (OnFilterChangeListener listener) {
        mFilterChangeListener = listener;
    }

    public static interface OnFilterChangeListener {
        public void onFilterChanged (long state, long category, long studio, long charge);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mStateAdapter != null) {
            mStateAdapter.clearDataList();
            mStateAdapter = null;
        }
        if (mCategoryAdapter != null) {
            mCategoryAdapter.clearDataList();
            mCategoryAdapter = null;
        }
        if (mStudioAdapter != null) {
            mStudioAdapter.clearDataList();
            mStudioAdapter = null;
        }
        if (mChargeRuleAdapter != null) {
            mChargeRuleAdapter.clearDataList();
            mChargeRuleAdapter = null;
        }
    }
}

package com.metis.coursepart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.debug.StringAdapter;
import com.metis.base.widget.adapter.debug.StringDelegate;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.FilterSpanSizeLookup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyunfei on 15/7/12.
 */
public class FilterFragment extends Fragment {

    private String[]
            mFakeDataState = {"推荐", "最新", "最热"},
            mFakeDataCategory = {
                    "全部", "素描头像", "素描半身像",
                    "素描静物", "色彩静物", "素描集合体",
                    "色彩头像", "哆啦A梦", "大明劫",
                    "杀破狼", "大圣归来", "阴阳路",
                    "古天乐", "雪国列车", "奉俊昊",
            };

    private RecyclerView mStateFilterRv, mCategoryFilterRv, mStudioFilterRv, mChargeRuleFilterRv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, null, true);
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

        GridLayoutManager studioGlm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        studioGlm.setSpanSizeLookup(new FilterSpanSizeLookup());

        mStateFilterRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mCategoryFilterRv.setLayoutManager(categoryGlm);
        mStudioFilterRv.setLayoutManager(studioGlm);
        mChargeRuleFilterRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<StringDelegate> delegateList = new ArrayList<StringDelegate>();
        for (String s : mFakeDataState) {
            delegateList.add(new StringDelegate(s));
        }
        StringAdapter stringAdapter = new StringAdapter(getActivity());
        stringAdapter.addDataList(delegateList);
        mStateFilterRv.setAdapter(stringAdapter);

        List<StringDelegate> categoryList = new ArrayList<StringDelegate>();
        for (String s : mFakeDataCategory) {
            categoryList.add(new StringDelegate(s));
        }
        StringAdapter categoryAdapter = new StringAdapter(getActivity());
        categoryAdapter.addDataList(categoryList);
        mCategoryFilterRv.setAdapter(categoryAdapter);
    }
}

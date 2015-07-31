package com.metis.commentpart.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.metis.base.activity.TitleBarActivity;
import com.metis.base.widget.adapter.FilterAdapter;
import com.metis.base.widget.adapter.lookup.FilterSpanSizeLookup;
import com.metis.base.widget.filter.Filter;
import com.metis.base.widget.filter.FilterProvider;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.StatusAdapter;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends TitleBarActivity {

    private RecyclerView mStateRv, mCategoryRv, mAreaRv;
    private RecyclerView mFilterRv = null;

    private StatusAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fliter);

        mStateRv = (RecyclerView)findViewById(R.id.comment_state);
        mCategoryRv = (RecyclerView)findViewById(R.id.comment_category);
        mAreaRv = (RecyclerView)findViewById(R.id.comment_area);

        mFilterRv = (RecyclerView)findViewById(R.id.filter_recycler_view);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Filter[] stateArray = {
                new Filter(0, getString(R.string.filter_all)),
                new Filter(1, getString(R.string.filter_has_commented)),
                new Filter(2, getString(R.string.filter_not_comment)),
                new Filter(3, getString(R.string.filter_hot_comment))
        };
        List<FilterProvider> stateProviders = new ArrayList<FilterProvider>();
        for (int i = 0; i < stateArray.length; i++) {
            StateFilter stateFilter = new StateFilter(stateArray[i]);
            stateFilter.setChecked(i == 0);
            stateProviders.add(stateFilter);
        }
        FilterAdapter stateAdapter = new FilterAdapter(this, stateProviders);
        GridLayoutManager stateLayoutManager = new GridLayoutManager(this, 1);
        stateLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mStateRv.setLayoutManager(stateLayoutManager);
        mStateRv.setAdapter(stateAdapter);
        stateAdapter.setOnFilterChangedListener(new FilterAdapter.OnFilterChangedListener() {
            @Override
            public void onFilterChanged(FilterProvider provider) {
                Toast.makeText(FilterActivity.this, provider.getFilter().getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        GridLayoutManager categoryLayoutManager = new GridLayoutManager(this, 2);
        categoryLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        categoryLayoutManager.setSpanSizeLookup(new FilterSpanSizeLookup());
        mCategoryRv.setLayoutManager(categoryLayoutManager);

        GridLayoutManager areaLayoutManager = new GridLayoutManager(this, 2);
        areaLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        areaLayoutManager.setSpanSizeLookup(new FilterSpanSizeLookup());
        mAreaRv.setLayoutManager(areaLayoutManager);

        mFilterRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StatusAdapter(this);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.title_activity_fliter);
    }

    public class StateFilter implements FilterProvider {

        private Filter mFilter = null;
        private boolean isChecked = false;

        public StateFilter (Filter filter) {
            mFilter = filter;
        }

        @Override
        public Filter getFilter() {
            return mFilter;
        }

        @Override
        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        @Override
        public boolean isChecked() {
            return isChecked;
        }

        @Override
        public void toggle() {
            isChecked = !isChecked;
        }
    }
}

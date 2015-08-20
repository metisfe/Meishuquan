package com.metis.commentpart.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.metis.base.activity.TitleBarActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.Footer;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.FilterAdapter;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.base.widget.adapter.lookup.FilterSpanSizeLookup;
import com.metis.base.widget.callback.OnScrollBottomListener;
import com.metis.base.widget.callback.TopTrackListener;
import com.metis.base.widget.filter.Filter;
import com.metis.base.widget.filter.FilterProvider;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.FilterDecoration;
import com.metis.commentpart.adapter.StatusAdapter;
import com.metis.commentpart.adapter.delegate.StatusDelegate;
import com.metis.commentpart.manager.StatusManager;
import com.metis.commentpart.module.AreaFilter;
import com.metis.commentpart.module.AssessCRegionItem;
import com.metis.commentpart.module.AssessChannel;
import com.metis.commentpart.module.AssessStatesItem;
import com.metis.commentpart.module.CategoryFilter;
import com.metis.commentpart.module.ChannelItem;
import com.metis.commentpart.module.StateFilter;
import com.metis.commentpart.module.Status;
import com.metis.commentpart.module.StatusList;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends TitleBarActivity implements FilterAdapter.OnFilterChangedListener {

    private static final String TAG = FilterActivity.class.getSimpleName();

    private LinearLayout mFilterPanel = null;
    private RecyclerView mStateRv, mCategoryRv, mAreaRv;
    private RecyclerView mFilterRv = null;

    private FilterAdapter mStateAdapter, mCategoryAdapter, mAreaAdapter;
    private StatusAdapter mAdapter = null;

    private AssessChannel mChannel = null;

    private Footer mFooter = null;
    private FooterDelegate mFooterDelegate = null;

    private String mLastRequestId = null;

    private boolean isLoading = false;

    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fliter);

        mFilterPanel = (LinearLayout)findViewById(R.id.filter_panel);
        mStateRv = (RecyclerView)findViewById(R.id.comment_state);
        mCategoryRv = (RecyclerView)findViewById(R.id.comment_category);
        mAreaRv = (RecyclerView)findViewById(R.id.comment_area);

        mFilterRv = (RecyclerView)findViewById(R.id.filter_recycler_view);
        mFilterRv.addItemDecoration(new FilterDecoration());
        mFilterRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StatusAdapter(this);
        mFilterRv.setAdapter(mAdapter);
        mFilterRv.addOnScrollListener(new OnScrollBottomListener() {
            @Override
            public void onScrollBottom(RecyclerView recyclerView, int newState) {
                if (!isLoading) {
                    loadData(mIndex + 1);
                }
            }
        });
        mFilterRv.addOnScrollListener(new TopTrackListener(mFilterPanel));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mChannel = StatusManager.getInstance(this).getAssessChannel();
        if (mChannel != null) {
            fillFilters(mChannel);
        } else {
            StatusManager.getInstance(this).getChannelList(new RequestCallback<AssessChannel>() {
                @Override
                public void callback(ReturnInfo<AssessChannel> returnInfo, String callbackId) {
                    if (returnInfo.isSuccess()) {
                        fillFilters(returnInfo.getData());
                    }
                }
            });
            //TODO
        }
    }

    private void fillFilters (AssessChannel channel) {
        fillStateFilter(channel.assessStates);
        fillCategoryFilters(channel.assessChannel);
        fillAreaFilters(channel.assessCRegions);

        mFooter = new Footer(Footer.STATE_WAITTING);
        mFooterDelegate = new FooterDelegate(mFooter);

        loadData(1);
    }

    private void fillStateFilter (List<AssessStatesItem> statesItems) {
        List<StateFilter> stateFilters = new ArrayList<StateFilter>();
        final int length = statesItems.size();
        for (int i = 0; i < length; i++) {
            StateFilter filter = new StateFilter(statesItems.get(i));
            filter.setChecked(i == 0);
            stateFilters.add(filter);
        }
        mStateAdapter = new FilterAdapter(this, stateFilters);
        GridLayoutManager stateLayoutManager = new GridLayoutManager(this, 1);
        stateLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mStateRv.setLayoutManager(stateLayoutManager);
        mStateRv.setAdapter(mStateAdapter);
        mStateAdapter.setOnFilterChangedListener(this);

    }

    private void fillCategoryFilters (List<ChannelItem> categoryList) {
        GridLayoutManager categoryLayoutManager = new GridLayoutManager(this, 2);
        categoryLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        categoryLayoutManager.setSpanSizeLookup(new FilterSpanSizeLookup());
        mCategoryRv.setLayoutManager(categoryLayoutManager);
        mCategoryRv.setHasFixedSize(true);
        List<CategoryFilter> categoryFilters = new ArrayList<CategoryFilter>();
        ChannelItem allCategoryItem = new ChannelItem();
        allCategoryItem.channelId = 0;
        allCategoryItem.name = getString(R.string.filter_all);
        CategoryFilter allCategory = new CategoryFilter(allCategoryItem);
        allCategory.setChecked(true);
        categoryFilters.add(allCategory);
        final int length = categoryList.size();
        for (int i = 0; i < length; i++) {
            categoryFilters.add(new CategoryFilter(categoryList.get(i)));
        }
        mCategoryAdapter = new FilterAdapter(this, categoryFilters);
        mCategoryRv.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setOnFilterChangedListener(this);
    }

    private void fillAreaFilters (List<AssessCRegionItem> itemList) {
        GridLayoutManager areaLayoutManager = new GridLayoutManager(this, 2);
        areaLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        areaLayoutManager.setSpanSizeLookup(new FilterSpanSizeLookup());
        mAreaRv.setLayoutManager(areaLayoutManager);
        mAreaRv.setHasFixedSize(true);
        AssessCRegionItem allArea = new AssessCRegionItem();
        allArea.codeid = 0;
        allArea.cityName = getString(R.string.filter_all);
        AreaFilter allAreaFilter = new AreaFilter(allArea);
        allAreaFilter.setChecked(true);
        List<AreaFilter> areaFilters = new ArrayList<AreaFilter>();
        areaFilters.add(allAreaFilter);
        final int length = itemList.size();
        for (int i = 0; i < length; i++) {
            areaFilters.add(new AreaFilter(itemList.get(i)));
        }
        mAreaAdapter = new FilterAdapter(this, areaFilters);
        mAreaRv.setAdapter(mAreaAdapter);
        mAreaAdapter.setOnFilterChangedListener(this);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.title_activity_fliter);
    }

    @Override
    public void onFilterChanged(FilterProvider provider) {
        onChage();
    }

    private void onChage () {
        loadData(1);
    }

    private void loadData (final int index) {
        User me = AccountManager.getInstance(this).getMe();
        if (me == null) {
            return;
        }

        if (index == 1) {
            mAdapter.clearDataList();
            mAdapter.addDataItem(mFooterDelegate);
        }
        mFooter.setState(Footer.STATE_WAITTING);
        mAdapter.notifyDataSetChanged();
        FilterProvider state = mStateAdapter.getSelectedFilterProvider();
        FilterProvider category = mCategoryAdapter.getSelectedFilterProvider();
        FilterProvider area = mAreaAdapter.getSelectedFilterProvider();
        isLoading = true;
        mLastRequestId = StatusManager.getInstance(this).getAssessList(state.getFilterId(), category.getFilterId(), area.getFilterId(), index, me.getCookie(), new RequestCallback<StatusList>() {
            @Override
            public void callback(ReturnInfo<StatusList> returnInfo, String callbackId) {
                isLoading = false;
                if (!callbackId.equals(mLastRequestId)) {
                    return;
                }
                if (returnInfo.isSuccess()) {
                    StatusList statusList = returnInfo.getData();
                    List<Status> statuses = statusList.assessList;
                    if (statuses != null && !statuses.isEmpty()) {
                        List<StatusDelegate> statusDelegates = new ArrayList<StatusDelegate>();
                        final int length = statuses.size();
                        for (int i = 0; i < length; i++) {
                            statusDelegates.add(new StatusDelegate(statuses.get(i)));
                        }
                        final int itemCount = mAdapter.getItemCount();
                        if (itemCount > 0) {
                            mAdapter.addDataList(itemCount - 1, statusDelegates);
                        } else {
                            mAdapter.addDataList(statusDelegates);
                        }
                        mFooter.setState(Footer.STATE_SUCCESS);
                    } else {
                        mFooter.setState(Footer.STATE_NO_MORE);
                    }
                    mIndex = index;
                } else {
                    mFooter.setState(Footer.STATE_FAILED);
                }
                mAdapter.notifyDataSetChanged();
            }
        });

    }
}

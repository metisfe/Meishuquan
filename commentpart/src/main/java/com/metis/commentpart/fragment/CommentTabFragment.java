package com.metis.commentpart.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.fragment.DockFragment;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.CacheManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.Footer;
import com.metis.base.module.User;
import com.metis.base.widget.TitleBar;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.base.widget.callback.OnScrollBottomListener;
import com.metis.base.widget.dock.DockBar;
import com.metis.commentpart.ActivityDispatcher;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.StatusAdapter;
import com.metis.commentpart.adapter.StatusItemDecoration;
import com.metis.commentpart.adapter.delegate.StatusDelegate;
import com.metis.commentpart.adapter.delegate.TeacherContainerDelegate;
import com.metis.commentpart.manager.StatusManager;
import com.metis.commentpart.module.Status;
import com.metis.commentpart.module.StatusList;
import com.metis.commentpart.module.Teacher;
import com.metis.commentpart.widget.OnScrollViewFlipperListener;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/24.
 */
public class CommentTabFragment extends DockFragment implements SwipeRefreshLayout.OnRefreshListener, AccountManager.OnUserChangeListener{

    private static final String TAG = CommentTabFragment.class.getSimpleName();

    private DockBar.Dock mDock = null;

    private TitleBar mTitleBar = null;
    private RecyclerView mStatusRv = null;
    private SwipeRefreshLayout mSrl = null;

    private StatusAdapter mAdapter = null;

    private Footer mFooter = null;
    private FooterDelegate mFooterDelegate = null;
    private boolean isLoading = false;
    private int mIndex = 1;

    private CacheManager mCacheManager = null;

    @Override
    public DockBar.Dock getDock(Context context) {
        if (mDock == null) {
            mDock = new DockBar.Dock(context, 1, android.R.drawable.btn_star, R.string.tab_title_center, this);
        }
        return mDock;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment_tab, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleBar = (TitleBar)view.findViewById(R.id.comment_tab_title_bar);
        mStatusRv = (RecyclerView)view.findViewById(R.id.comment_tab_recycler_view);
        mSrl = (SwipeRefreshLayout)view.findViewById(R.id.comment_swipe_refresh_layout);

        mTitleBar.setTitleCenter(R.string.tab_title_center);
        mTitleBar.setDrawableResourceLeft(R.drawable.ic_filter);

        mTitleBar.setOnLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.filterActivity(getActivity());
            }
        });

        mTitleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatusRv.smoothScrollToPosition(0);
            }
        });

        mStatusRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mStatusRv.addOnScrollListener(new OnScrollViewFlipperListener());
        mStatusRv.addItemDecoration(new StatusItemDecoration());
        mAdapter = new StatusAdapter(getActivity());
        mStatusRv.setAdapter(mAdapter);
        mStatusRv.addOnScrollListener(new OnScrollBottomListener() {
            @Override
            public void onScrollBottom(RecyclerView recyclerView, int newState) {
                if (!isLoading) {
                    loadData(mIndex + 1);
                }
            }
        });


        mSrl.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_purple
        );
        mSrl.setOnRefreshListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFooter = new Footer(Footer.STATE_WAITTING);
        mFooterDelegate = new FooterDelegate(mFooter);

        mCacheManager = CacheManager.getInstance(getActivity());

        List<Status> statusList = mCacheManager.readUserDataAtDatabase(Status.class, "assessList.db");
        List<Teacher> teacherList = mCacheManager.readUserDataAtDatabase(Teacher.class, "teacherList.db");

        parseData(1, statusList, teacherList);
        mAdapter.notifyDataSetChanged();

        loadData(1);
        mSrl.post(new Runnable() {
            @Override
            public void run() {
                mSrl.setRefreshing(true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        User me = AccountManager.getInstance(getActivity()).getMe();
        checkUser(me);
        AccountManager.getInstance(getActivity()).registerOnUserChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        AccountManager.getInstance(getActivity()).unregisterOnUserChangeListener(this);
    }

    private void checkUser (final User me) {
        if (me == null || (me.userRole == User.USER_ROLE_PARENTS || me.userRole == User.USER_ROLE_STUDENT || me.userRole == User.USER_ROLE_FANS)) {
            mTitleBar.setDrawableResourceRight(R.drawable.ic_new_status);
            mTitleBar.setOnRightBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (me == null) {
                        com.metis.base.ActivityDispatcher.loginActivity(getActivity());
                    } else {
                        ActivityDispatcher.publishStatusActivity(getActivity());
                    }
                }
            });
            mTitleBar.setRightEnable(true);
        } else {
            mTitleBar.setDrawableRight(null);
            mTitleBar.setOnRightBtnClickListener(null);
            mTitleBar.setRightEnable(false);
        }
        /*mTitleBar.setDrawableResourceRight(R.drawable.ic_new_status);
        mTitleBar.setOnRightBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (me != null && (me.userRole == User.USER_ROLE_PARENTS || me.userRole == User.USER_ROLE_STUDENT || me.userRole == User.USER_ROLE_FANS)) {
                    ActivityDispatcher.publishStatusActivity(getActivity());
                } else {
                    com.metis.base.ActivityDispatcher.loginActivity(getActivity());
                }
            }
        });*/
    }

    @Override
    public void onRefresh() {
        loadData(1);
    }

    private void loadData (final int index) {
        User me = AccountManager.getInstance(getActivity()).getMe();
        String session = "";
        if (me != null) {
            session = me.getCookie();
        }
        if (index > 1) {
            mFooter.setState(Footer.STATE_WAITTING);
            mAdapter.notifyDataSetChanged();
        }
        isLoading = true;
        StatusManager.getInstance(getActivity()).getAssessList(0, 0, 0, index, session, new RequestCallback<StatusList>() {
            @Override
            public void callback(ReturnInfo<StatusList> returnInfo, String callbackId) {
                if (!isAlive()) {
                    return;
                }
                if (returnInfo.isSuccess()) {

                    List<Status> statuses = returnInfo.getData().assessList;
                    List<Teacher> teacherList = returnInfo.getData().teacherList;

                    if (index == 1) {
                        mCacheManager.saveAllUserDataAtDatabase(statuses, "assessList.db", Status.class, true);
                        mCacheManager.saveAllUserDataAtDatabase(teacherList, "teacherList.db", Teacher.class, true);
                    }

                    parseData(index, statuses, teacherList);
                    mIndex = index;
                } else {
                    mFooter.setState(Footer.STATE_FAILED);
                    if (index == 0 && mAdapter.getItemCount() == 0) {
                        mAdapter.addDataItem(mFooterDelegate);
                    }
                }
                mAdapter.notifyDataSetChanged();
                isLoading = false;
                mSrl.setRefreshing(false);
            }
        });

    }

    private void parseData (final int index, List<Status> statuses, List<Teacher> teacherList) {
        if (statuses == null) {
            return;
        }
        List<BaseDelegate> list = new ArrayList<BaseDelegate>();
        final int length = statuses.size();
        for (int i = 0; i < length; i++) {
            list.add(new StatusDelegate(statuses.get(i)));
        }
        if (index == 1) {
            if (list.size() > 2) {
                //teacherList = returnInfo.getData().teacherList;
                if (teacherList != null && !teacherList.isEmpty()) {
                    TeacherContainerDelegate containerDelegate = new TeacherContainerDelegate(teacherList);
                    list.add(1, containerDelegate);
                }
            }
            mAdapter.clearDataList();
            mAdapter.addDataItem(mFooterDelegate);
        }
        final int itemCount = mAdapter.getItemCount();
        if (itemCount > 0) {
            mAdapter.addDataList(itemCount - 1, list);
        } else {
            mAdapter.addDataList(list);
        }
        if (length == 0) {
            mFooter.setState(Footer.STATE_NO_MORE);
        } else {
            mFooter.setState(Footer.STATE_SUCCESS);
        }
    }

    @Override
    public void onUserChanged(User user, boolean onLine) {
        checkUser(user);
    }
}

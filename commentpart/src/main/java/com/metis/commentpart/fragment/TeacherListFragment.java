package com.metis.commentpart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.fragment.BaseFragment;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.Footer;
import com.metis.base.module.User;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.base.widget.callback.OnScrollBottomListener;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.StatusAdapter;
import com.metis.commentpart.adapter.TeacherDecoration;
import com.metis.commentpart.adapter.delegate.TeacherBtnDelegate;
import com.metis.commentpart.manager.StatusManager;
import com.metis.commentpart.module.Teacher;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/8/4.
 */
public class TeacherListFragment extends BaseFragment {

    private RecyclerView mTeacherRv = null;

    private StatusAdapter mAdapter = null;

    private int mTeacherFilter = 0;
    private int mIndex = 1;

    private Footer mFooter = null;
    private FooterDelegate mFooterDelegate = null;

    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teacher_list, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTeacherRv = (RecyclerView)view.findViewById(R.id.teacher_recycler_view);

        mTeacherRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTeacherRv.addItemDecoration(new TeacherDecoration());
        mAdapter = new StatusAdapter(getActivity());
        mTeacherRv.setAdapter(mAdapter);
        mTeacherRv.addOnScrollListener(new OnScrollBottomListener() {
            @Override
            public void onScrollBottom(RecyclerView recyclerView, int newState) {
                if (!isLoading) {
                    loadData(mIndex + 1);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFooter = new Footer(Footer.STATE_WAITTING);
        mFooterDelegate = new FooterDelegate(mFooter);
        mAdapter.addDataItem(mFooterDelegate);
        mAdapter.notifyDataSetChanged();
        loadData(1);
    }

    public void setTeacherFilter (int filter) {
        mTeacherFilter = filter;
    }

    public void refresh () {

    }

    private void loadData (final int index) {
        User me = AccountManager.getInstance(getActivity()).getMe();
        if (me == null) {
            return;
        }
        mFooter.setState(Footer.STATE_WAITTING);
        mAdapter.notifyDataSetChanged();
        isLoading = true;
        StatusManager.getInstance(getActivity()).getAssessTeacher(mTeacherFilter, "", me.getCookie(), index, new RequestCallback<List<Teacher>>() {
            @Override
            public void callback(ReturnInfo<List<Teacher>> returnInfo, String callbackId) {
                isLoading = false;
                if (!isAlive()) {
                    return;
                }
                if (returnInfo.isSuccess()) {
                    List<Teacher> teacherList = returnInfo.getData();
                    List<TeacherBtnDelegate> delegateList = new ArrayList<TeacherBtnDelegate>();
                    if (teacherList != null && !teacherList.isEmpty()) {
                        final int length = teacherList.size();
                        for (int i = 0; i < length; i++) {
                            delegateList.add(new TeacherBtnDelegate(teacherList.get(i)));
                        }
                        mAdapter.addDataList(mAdapter.getItemCount() - 1, delegateList);
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

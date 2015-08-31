package com.metis.commentpart.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.metis.base.activity.TitleBarActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.Footer;
import com.metis.base.module.User;
import com.metis.base.widget.SearchView;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.TeacherCbAdapter;
import com.metis.commentpart.adapter.TeacherDecoration;
import com.metis.commentpart.adapter.delegate.TeacherCbDelegate;
import com.metis.commentpart.manager.StatusManager;
import com.metis.commentpart.manager.TeacherManager;
import com.metis.commentpart.module.Teacher;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;
import java.util.List;

public class TeacherSearchActivity extends TitleBarActivity implements SearchView.OnSearchListener{

    private String mRequestId = null;

    private RecyclerView mTeacherRv = null;
    private SearchView mSearchView = null;

    private TeacherCbAdapter mTeacherCbAdapter = null;

    private Footer mFooter = null;
    private FooterDelegate mFooterDelegate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_search);

        mSearchView = new SearchView(this);
        mSearchView.setOnSearchListener(this);
        getTitleBar().setCenterView(mSearchView);

        mTeacherRv = (RecyclerView)findViewById(R.id.teacher_list_recycler_view);
        mTeacherRv.setLayoutManager(new LinearLayoutManager(this));
        mTeacherRv.addItemDecoration(new TeacherDecoration());
        mTeacherCbAdapter = new TeacherCbAdapter(this);
        mTeacherRv.setAdapter(mTeacherCbAdapter);

        mFooter = new Footer(Footer.STATE_IDLE);
        mFooterDelegate = new FooterDelegate(mFooter);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public void onSearchHappened(String content) {
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, R.string.toast_empty_search_content, Toast.LENGTH_SHORT).show();
            return;
        }
        User me = AccountManager.getInstance(this).getMe();
        if (me == null) {
            com.metis.base.ActivityDispatcher.loginActivityWhenAlreadyIn(this);
            return;
        }
        mTeacherCbAdapter.clearDataList();
        mFooter.setState(Footer.STATE_WAITTING);
        mTeacherCbAdapter.addDataItem(mFooterDelegate);
        mTeacherCbAdapter.notifyDataSetChanged();
        StatusManager.getInstance(this).getAssessTeacher(1, content, me.getCookie(), 1, new RequestCallback<List<Teacher>>() {
            @Override
            public void callback(ReturnInfo<List<Teacher>> returnInfo, String callbackId) {
                mSearchView.requestInputFocus();
                if (returnInfo.isSuccess()) {
                    List<Teacher> teacherList = returnInfo.getData();
                    if (teacherList != null && !teacherList.isEmpty()) {
                        final int length = teacherList.size();
                        List<TeacherCbDelegate> delegates = new ArrayList<TeacherCbDelegate>();
                        for (int i = 0; i < length; i++) {
                            TeacherCbDelegate delegate = new TeacherCbDelegate(teacherList.get(i));
                            delegate.setChecked(TeacherManager.getInstance(TeacherSearchActivity.this).hasSelected(delegate));
                            delegates.add(delegate);
                        }
                        mTeacherCbAdapter.addDataList(0, delegates);
                    }
                }
                mFooter.setState(mTeacherCbAdapter.getItemCount() > 1 ? Footer.STATE_SUCCESS : Footer.STATE_NO_MORE);
                mTeacherCbAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onSearchClear() {
        mTeacherCbAdapter.clearDataList();
        mFooter.setState(Footer.STATE_IDLE);
        mTeacherCbAdapter.addDataItem(mFooterDelegate);
        mTeacherCbAdapter.notifyDataSetChanged();
    }
}

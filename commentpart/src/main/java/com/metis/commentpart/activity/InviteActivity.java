package com.metis.commentpart.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.base.activity.TitleBarActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.delegate.DividerDelegate;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.StatusAdapter;
import com.metis.commentpart.adapter.TeacherSelectedAdapter;
import com.metis.commentpart.adapter.delegate.TeacherCbDelegate;
import com.metis.commentpart.manager.StatusManager;
import com.metis.commentpart.manager.TeacherManager;
import com.metis.commentpart.module.Teacher;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;
import java.util.List;

public class InviteActivity extends TitleBarActivity implements TeacherManager.OnTeachersListener{

    private static final String TAG = InviteActivity.class.getSimpleName();

    private static final String DB_NAME = "recent_teachers";

    private RecyclerView mSelectedRv, mRv;

    private TeacherSelectedAdapter mSelectedAdapter = null;
    private StatusAdapter mAdapter = null;

    private TeacherManager mTeacherManager = null;
    private AccountManager mAccountManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        mAccountManager = AccountManager.getInstance(this);
        mTeacherManager = TeacherManager.getInstance(this);

        mTeacherManager.registerOnTeachersListener(this);

        mSelectedRv = (RecyclerView)findViewById(R.id.invite_selected_recycler_view);
        mRv = (RecyclerView)findViewById(R.id.invite_recycler_view);

        mSelectedRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mSelectedAdapter = new TeacherSelectedAdapter(this, mTeacherManager.getSelectedTeachers());
        mSelectedRv.setAdapter(mSelectedAdapter);

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StatusAdapter(this);
        mRv.setAdapter(mAdapter);

        getTitleBar().setDrawableResourceRight(R.drawable.ic_check);
        getTitleBar().setOnRightBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mTeacherManager.commitRecentTeachers();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        List<TeacherCbDelegate> recentTeachers = mTeacherManager.getRecentTeachers();
        /*if (recentTeachers != null) {
            mAdapter.addDataItem(new DividerDelegate(getString(R.string.invite_recent_teachers)));
            mAdapter.addDataList(recentTeachers);
            mAdapter.notifyDataSetChanged();
        } else {*/
            StatusManager.getInstance(this).getAssessTeacher(StatusManager.TEACHER_TYPE_RECENT, "", mAccountManager.getMe().getCookie(), new RequestCallback<List<Teacher>>() {
                @Override
                public void callback(ReturnInfo<List<Teacher>> returnInfo, String callbackId) {
                    if (returnInfo.isSuccess()) {
                        List<Teacher> teacherList = returnInfo.getData();

                        if (teacherList == null || teacherList.isEmpty()) {
                            return;
                        }
                        List<TeacherCbDelegate> delegates = new ArrayList<TeacherCbDelegate>();
                        final int length = teacherList.size();
                        for (int i = 0; i < length; i++) {
                            delegates.add(new TeacherCbDelegate(teacherList.get(i)));
                        }
                        mAdapter.addDataItem(new DividerDelegate(getString(R.string.invite_recent_teachers)));
                        mAdapter.addDataList(delegates);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        //}

        StatusManager.getInstance(this).getAssessTeacher(StatusManager.TEACHER_TYPE_MOST_COMMENTS, "", mAccountManager.getMe().getCookie(), new RequestCallback<List<Teacher>>() {
            @Override
            public void callback(ReturnInfo<List<Teacher>> returnInfo, String callbackId) {
                if (returnInfo.isSuccess()) {
                    List<Teacher> teacherList = returnInfo.getData();
                    if (teacherList == null || teacherList.isEmpty()) {
                        return;
                    }
                    List<TeacherCbDelegate> delegates = new ArrayList<TeacherCbDelegate>();
                    final int length = teacherList.size();
                    for (int i = 0; i < length; i++) {
                        delegates.add(new TeacherCbDelegate(teacherList.get(i)));
                    }
                    mAdapter.addDataItem(new DividerDelegate(getString(R.string.invite_active_teachers)));
                    mAdapter.addDataList(delegates);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTeacherManager.unregisterOnTeachersListener(this);
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.title_activity_invite);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public void onConfirmed(List<Teacher> teachers) {

    }

    @Override
    public void onSelected(TeacherManager manager, Teacher teacher) {
        mSelectedAdapter.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUnSelected(TeacherManager manager, Teacher teacher) {
        mSelectedAdapter.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();
    }
}

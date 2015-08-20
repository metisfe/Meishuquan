package com.metis.commentpart.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.metis.base.activity.TitleBarActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.Footer;
import com.metis.base.widget.adapter.delegate.DividerDelegate;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.base.widget.callback.OnScrollBottomListener;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.TeacherCbAdapter;
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
    private TeacherCbAdapter mAdapter = null;

    private TeacherManager mTeacherManager = null;
    private AccountManager mAccountManager = null;

    private boolean isRecentLoaded = false;
    private boolean isActiveFirstLoad = true;
    private boolean isLoading = false;
    private int mIndex = 1;

    private Footer mFooter = null;
    private FooterDelegate mFooterDelegate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_invite);

        mAccountManager = AccountManager.getInstance(this);
        mTeacherManager = TeacherManager.getInstance(this);

        mTeacherManager.registerOnTeachersListener(this);

        mSelectedRv = (RecyclerView)findViewById(R.id.invite_selected_recycler_view);
        mRv = (RecyclerView)findViewById(R.id.invite_recycler_view);

        mSelectedRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mSelectedAdapter = new TeacherSelectedAdapter(this);
        mSelectedRv.setAdapter(mSelectedAdapter);

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.addOnScrollListener(new OnScrollBottomListener() {
            @Override
            public void onScrollBottom(RecyclerView recyclerView, int newState) {
                if (!isLoading) {
                    loadData(mIndex + 1);
                }
            }
        });
        mAdapter = new TeacherCbAdapter(this);
        mRv.setAdapter(mAdapter);

        /*getTitleBar().setDrawableResourceRight(R.drawable.ic_search);
        getTitleBar().setOnRightBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                Intent it = new Intent(InviteActivity.this, TeacherSearchActivity.class);
                startActivity(it);
            }
        });*/
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mFooter = new Footer(Footer.STATE_WAITTING);
        mFooterDelegate = new FooterDelegate(mFooter);
        mAdapter.addDataItem(mFooterDelegate);
        mAdapter.notifyDataSetChanged();
        loadData(1);
    }

    private void loadData (final int index) {
        if (!isRecentLoaded) {
            List<TeacherCbDelegate> recentTeachers = mTeacherManager.getRecentTeachers();
            StatusManager.getInstance(this).getAssessTeacher(StatusManager.TEACHER_TYPE_RECENT, "", mAccountManager.getMe().getCookie(), 1, new RequestCallback<List<Teacher>>() {
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
                            TeacherCbDelegate delegate = new TeacherCbDelegate(teacherList.get(i));
                            delegate.setChecked(mTeacherManager.hasSelected(delegate));
                            delegates.add(delegate);
                        }
                        if (mAdapter.getItemCount() > 0) {
                            mAdapter.addDataList(0, delegates);
                        } else {
                            mAdapter.addDataList(delegates);
                        }
                        mAdapter.addDataItem(0, new DividerDelegate(getString(R.string.invite_recent_teachers)));
                        mAdapter.notifyDataSetChanged();
                        isRecentLoaded = true;
                    }
                }
            });
        }
        isLoading = true;
        mFooter.setState(Footer.STATE_WAITTING);
        mAdapter.notifyDataSetChanged();
        StatusManager.getInstance(this).getAssessTeacher(StatusManager.TEACHER_TYPE_MOST_COMMENTS, "", mAccountManager.getMe().getCookie(), index, new RequestCallback<List<Teacher>>() {
            @Override
            public void callback(ReturnInfo<List<Teacher>> returnInfo, String callbackId) {
                if (returnInfo.isSuccess()) {
                    List<Teacher> teacherList = returnInfo.getData();
                    /*if (teacherList == null || teacherList.isEmpty()) {
                        return;
                    }*/
                    List<TeacherCbDelegate> delegates = new ArrayList<TeacherCbDelegate>();
                    final int length = teacherList.size();
                    for (int i = 0; i < length; i++) {
                        TeacherCbDelegate delegate = new TeacherCbDelegate(teacherList.get(i));
                        delegate.setChecked(mTeacherManager.hasSelected(delegate));
                        delegates.add(delegate);
                    }
                    if (isActiveFirstLoad) {
                        mAdapter.addDataItem(mAdapter.getItemCount() - 1, new DividerDelegate(getString(R.string.invite_active_teachers)));
                        isActiveFirstLoad = false;
                    }
                    if (length > 0) {
                        mFooter.setState(Footer.STATE_SUCCESS);
                    } else {
                        mFooter.setState(Footer.STATE_NO_MORE);
                    }
                    mAdapter.addDataList(mAdapter.getItemCount() - 1, delegates);
                    mIndex = index;
                } else {
                    mFooter.setState(Footer.STATE_FAILED);
                }
                mAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTeacherManager.commitRecentTeachers();
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
        if (manager.getSelectedCount() == 1) {
            animationDown();
        }
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUnSelected(TeacherManager manager, Teacher teacher) {
        mSelectedAdapter.notifyDataSetChanged();
        mAdapter.unSelected(teacher);
        if (manager.getSelectedCount() == 0) {
            animationBack();
        }
    }

    private void animationDown () {
        final int targetTransY = getResources().getDimensionPixelSize(R.dimen.invite_selected_rv_height);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mRv, "translationY", 0, targetTransY);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRv.setPadding(0, 0, 0, targetTransY);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mRv.setTranslationY(targetTransY);
                mRv.setPadding(0, 0, 0, targetTransY);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void animationBack () {
        float fromTransY = mRv.getTranslationY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mRv, "translationY", fromTransY, 0);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRv.setPadding(0, 0, 0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mRv.setTranslationY(0);
                mRv.setPadding(0, 0, 0, 0);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
}

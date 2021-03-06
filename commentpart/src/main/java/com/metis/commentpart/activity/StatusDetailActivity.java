package com.metis.commentpart.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.base.activity.TitleBarActivity;
import com.metis.base.framework.NetProxy;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.manager.UploadManager;
import com.metis.base.manager.VoiceManager;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.commentpart.ActivityDispatcher;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.CommentCardAdapter;
import com.metis.commentpart.adapter.CommentCardDecoration;
import com.metis.commentpart.adapter.CommentListAdapter;
import com.metis.commentpart.adapter.CommentListDecoration;
import com.metis.commentpart.adapter.delegate.CardFooterDelegate;
import com.metis.commentpart.adapter.delegate.CardHeaderDelegate;
import com.metis.commentpart.adapter.delegate.CommentItemDelegate;
import com.metis.commentpart.adapter.delegate.StatusDelegate;
import com.metis.commentpart.adapter.delegate.StatusDetailTabDelegate;
import com.metis.commentpart.fragment.ChatInputFragment;
import com.metis.commentpart.fragment.VoiceFragment;
import com.metis.commentpart.manager.StatusManager;
import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.CommentCollection;
import com.metis.commentpart.module.Status;
import com.metis.commentpart.module.StatusDetailTabItem;
import com.metis.commentpart.module.TeacherComment;
import com.metis.commentpart.module.Voice;
import com.metis.commentpart.utils.CommentFactory;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class StatusDetailActivity extends TitleBarActivity implements
        CardFooterDelegate.OnCommentFooterClickListener,
        View.OnClickListener, VoiceFragment.VoiceDispatcher, ChatInputFragment.Controller,
        CommentItemDelegate.OnCommentActionListener, SwipeRefreshLayout.OnRefreshListener
        /*AccountManager.OnUserChangeListener*/{

    private static final String TAG = StatusDetailActivity.class.getSimpleName();

    public static final int MODE_NORMAL = 0, MODE_WITH_COMMENT = 1;

    private Status mStatus = null;
    private TextView mReplyTextTv;
    private ImageView mReplyVoiceIv;
    private SwipeRefreshLayout mSrl = null;
    private RecyclerView mDetailRv = null;
    private CommentCardAdapter mCardAdapter = null;
    private CommentListAdapter mListAdapter = null;

    private StatusDetailTabItem mTabItem = null;

    private CommentCardDecoration mCardDecoration = null;
    private CommentListDecoration mListDecoration = null;
    private RelativeLayout mFragmentContainer = null;
    private ChatInputFragment mChatFragment = null;
    private RelativeLayout mReplyingContainer = null;
    private TextView mReplyingInfoTv = null;
    private ImageView mReplyingCloseIv = null;

    private Comment mReplyingComment = null;

    private int mMode = MODE_NORMAL;

    private StatusDelegate mStatusDelegate = null;
    private StatusDetailTabDelegate mTabDelegate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);

        mStatus = (Status)getIntent().getSerializableExtra(ActivityDispatcher.KEY_STATUS);
        mMode = getIntent().getIntExtra(ActivityDispatcher.KEY_MODE, MODE_NORMAL);
        Log.v(TAG, "mStatus.desc=" + mStatus.desc);

        mReplyingContainer = (RelativeLayout)findViewById(R.id.replying_container);
        mReplyingInfoTv = (TextView)findViewById(R.id.replying_comment_info);
        mReplyingCloseIv = (ImageView)findViewById(R.id.replying_comment_close_btn);

        mFragmentContainer = (RelativeLayout)findViewById(R.id.fragment_container);
        mChatFragment = (ChatInputFragment)getSupportFragmentManager().findFragmentById(R.id.detail_reply_container);

        mReplyTextTv = (TextView)findViewById(R.id.reply_text);
        mReplyVoiceIv = (ImageView)findViewById(R.id.reply_voice);

        mSrl = (SwipeRefreshLayout)findViewById(R.id.detail_swipe_refresh_layout);
        mDetailRv = (RecyclerView)findViewById(R.id.detail_recycler_view);

        mCardDecoration = new CommentCardDecoration();
        mListDecoration = new CommentListDecoration();

        mDetailRv.setLayoutManager(new LinearLayoutManager(this));
        mCardAdapter = new CommentCardAdapter(this);
        mListAdapter = new CommentListAdapter(this);

        switchToCard();

        mChatFragment.setVoiceDispatcher(this);
        mChatFragment.setController(this);

        mReplyTextTv.setOnClickListener(this);
        mReplyVoiceIv.setOnClickListener(this);

        mReplyingCloseIv.setOnClickListener(this);

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
    protected void onResume() {
        super.onResume();
        /*User me = AccountManager.getInstance(this).getMe();
        checkUser(me);*/
//        AccountManager.getInstance(this).registerOnUserChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (VoiceManager.getInstance(this).isPlaying()) {
            VoiceManager.getInstance(this).stopPlay();
        }
//        AccountManager.getInstance(this).unregisterOnUserChangeListener(this);
    }

    /*private void checkUser (User me) {
        if (me == null) {
            mChatFragment.showMask();
            mChatFragment.setInputHint(getString(R.string.hint_comment_need_login));
        } else {
            mChatFragment.hideMask();
            if (me.userRole == User.USER_ROLE_STUDIO) {
                mChatFragment.setEnable(false);
                mChatFragment.setInputHint(getString(R.string.hint_comment_not_for_studio));
            } else {
                mChatFragment.setEnable(false);
                mChatFragment.setInputHint(getString(R.string.status_item_publish_comment));
            }
        }
    }*/

    @Override
    public void onRefresh() {
        loadData();
    }

    private void switchToCard () {
        mDetailRv.setAdapter(mCardAdapter);
        mDetailRv.removeItemDecoration(mListDecoration);
        mDetailRv.addItemDecoration(mCardDecoration);
    }

    private void switchToList() {
        mDetailRv.setAdapter(mListAdapter);
        mDetailRv.removeItemDecoration(mCardDecoration);
        mDetailRv.addItemDecoration(mListDecoration);
        if (VoiceManager.getInstance(this).isPlaying()) {
            VoiceManager.getInstance(this).stopPlay();
        }
    }

    private void loadData () {
        mCardAdapter.clearDataList();
        mListAdapter.clearDataList();

        mCardAdapter.addDataItem(mStatusDelegate);
        mListAdapter.addDataItem(mStatusDelegate);

        mCardAdapter.addDataItem(mTabDelegate);
        mCardAdapter.notifyDataSetChanged();

        mListAdapter.addDataItem(mTabDelegate);
        mListAdapter.notifyDataSetChanged();
        mChatFragment.setEnable(false);
        User me = AccountManager.getInstance(this).getMe();
        String session = "";
        if (me == null) {
            mSrl.setRefreshing(false);
        } else {
            session = me.getCookie();
        }

        StatusManager.getInstance(this).getCommentList(mStatus.id, session, new RequestCallback<CommentCollection>() {
            @Override
            public void callback(ReturnInfo<CommentCollection> returnInfo, String callbackId) {
                mSrl.setRefreshing(false);
                mChatFragment.setEnable(returnInfo.isSuccess());
                if (returnInfo.isSuccess()) {
                    CommentCollection collection = returnInfo.getData();
                    List<TeacherComment> teacherList = collection.teacherCommentList;

                    List<BaseDelegate> teacherDelegates = new ArrayList<BaseDelegate>();
                    if (teacherList != null && !teacherList.isEmpty()) {
                        final int length = teacherList.size();
                        for (int i = 0; i < length; i++) {
                            TeacherComment teacherComment = teacherList.get(i);
                            Comment comment = teacherComment.mainComment;
                            CardHeaderDelegate headerDelegate = new CardHeaderDelegate(comment);
                            teacherDelegates.add(headerDelegate);
                            BaseDelegate textTDelegate = CommentFactory.makeCommentDelegate(comment);
                            if (textTDelegate != null) {
                                teacherDelegates.add(textTDelegate);
                            }

                            if (teacherComment.hasSubCommentList()) {
                                List<Comment> subList = teacherComment.subCommentList;
                                final int subLength = subList.size();
                                for (int k = 0; k < subLength; k++) {
                                    Comment innerComment = subList.get(k);
                                    BaseDelegate innerDelegate = CommentFactory.makeCommentDelegate(innerComment);
                                    if (innerDelegate != null) {
                                        teacherDelegates.add(innerDelegate);
                                    }
                                }
                            }

                            CardFooterDelegate footerDelegate = new CardFooterDelegate(comment, mStatus);
                            footerDelegate.setOnCommentFooterClickListener(StatusDetailActivity.this);
                            teacherDelegates.add(footerDelegate);

                        }
                        mTabItem.setTextLeft(getString(R.string.status_detail_tab_teacher, length));
                        mCardAdapter.addDataList(teacherDelegates);
                    } else {
                        //TODO
                    }
                    mCardAdapter.notifyDataSetChanged();

                    List<Comment> studentList = collection.studentCommentList;
                    if (studentList != null && !studentList.isEmpty()) {
                        final int length = studentList.size();
                        List<BaseDelegate> itemDelegateList = new ArrayList<BaseDelegate>();
                        for (int i = 0; i < length; i++) {
                            Comment comment = studentList.get(i);
                            CommentItemDelegate itemDelegate = new CommentItemDelegate(comment, mStatus);
                            itemDelegate.setOnCommentActionListener(StatusDetailActivity.this);
                            itemDelegateList.add(itemDelegate);
                        }
                        mTabItem.setTextRight(getString(R.string.status_detail_tab_student, length));
                        mListAdapter.addDataList(itemDelegateList);
                    } else {
                        //TODO
                    }
                    mListAdapter.notifyDataSetChanged();
                    if (mMode == MODE_WITH_COMMENT) {
                        mChatFragment.askToInput();
                        mMode = MODE_NORMAL;
                    }
                }

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mStatusDelegate = new StatusDelegate(mStatus);
        mStatusDelegate.setIsInDetails(true);

        mTabItem = new StatusDetailTabItem();
        mTabItem.setTextLeft(getString(R.string.status_detail_tab_teacher, 0));
        mTabItem.setTextRight(getString(R.string.status_detail_tab_student, 0));
        mTabItem.setOnTabSelectListener(new StatusDetailTabItem.OnTabSelectListener() {
            @Override
            public void onLeftSelected() {
                switchToCard();
            }

            @Override
            public void onRightSelected() {
                switchToList();
            }
        });
        mTabDelegate = new StatusDetailTabDelegate(mTabItem);

        mSrl.post(new Runnable() {
            @Override
            public void run() {
                mSrl.setRefreshing(true);
            }
        });
        loadData();
    }

    @Override
    public void onBackPressed() {
        if (mChatFragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.title_activity_comment_detail);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public void onClick(View view, Comment comment, int replyType) {
        replyingComment(comment);
        switch (replyType) {
            case CardFooterDelegate.REPLY_TYPE_TEXT:
                mChatFragment.askToInput();
                break;
            case CardFooterDelegate.REPLY_TYPE_VOICE:
                mChatFragment.askToRecord();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        User me = AccountManager.getInstance(this).getMe();
        if (me == null) {
            com.metis.base.ActivityDispatcher.loginActivityWhenAlreadyIn(this);
            return;
        }
        Comment comment = mCardAdapter.getMyCardHeader(me);
        final int id = v.getId();
        if (id == mReplyTextTv.getId()) {
            if (comment != null) {
                Log.v(TAG, "comment id=" + comment + " text=" + comment.content);
            } else {

            }
        } else if (id == mReplyVoiceIv.getId()) {
            if (comment != null) {
            } else {
            }
        } else if (id == mReplyingCloseIv.getId()) {
            dismissComment();
        }
    }

    @Override
    public void onGiveUp(String path) {

    }

    @Override
    public void onUse(String path, final int duration) {
        final User me = AccountManager.getInstance(this).getMe();
        if (me == null) {
            //TODO
            return;
        }
        if (path == null) {
            return;
        }
        File file = new File (path);
        if (!file.exists()) {
            //TODO
            return;
        }
        final Comment comment = me.userRole == User.USER_ROLE_TEACHER ? mCardAdapter.getMyCardHeader(me.userId) : mReplyingComment;
        int commentSource = 1;/*CommentFactory.getCommentSource(me);*/
        long replyUserId = 0;
        long replyCid = 0;
        if (comment != null) {
            replyUserId = comment.user.userId;
            replyCid = comment.id;
        }
        if (me.equals(mStatus.user)) {
            if (comment != null) {
                commentSource = CommentFactory.getCommentSource(me, comment.user);
            }
        } else if (me.userRole == User.USER_ROLE_TEACHER) {
            commentSource = 0;
        } else if (me.userRole == User.USER_ROLE_STUDENT || me.userRole == User.USER_ROLE_FANS) {
            commentSource = 1;
        }
        final long useReplyUserId = replyUserId;
        final long useReplyCid = replyCid;
        final int useCommentSource = commentSource;
        UploadManager.getInstance(this).uploadFile(file, NetProxy.TYPE_VOICE, me.getCookie(), new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<List<Voice>> voiceResult = new Gson().fromJson(result, new TypeToken<ReturnInfo<List<Voice>>>() {
                }.getType());
                if (voiceResult.isSuccess()) {
                    StatusManager.getInstance(StatusDetailActivity.this).pushComment(mStatus.id, me.userId, useReplyUserId, "", useReplyCid, null, voiceResult.getData().get(0).voiceUrl, duration, 2, useCommentSource, me.getCookie(), new RequestCallback<Comment>() {
                        @Override
                        public void callback(ReturnInfo<Comment> returnInfo, String callbackId) {
                            if (!returnInfo.isSuccess()) {
                                //TODO
                                Toast.makeText(StatusDetailActivity.this, getString(R.string.toast_comment_failed, returnInfo.getMessage()), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(StatusDetailActivity.this, R.string.toast_comment_success, Toast.LENGTH_SHORT).show();
                            Comment newComment = returnInfo.getData();
                            Log.v(TAG, "pushComment newComment=" + newComment);
                            mCardAdapter.addCommentFollow(mStatus, comment, newComment);
                            mTabItem.setTextLeft(getString(R.string.status_detail_tab_teacher, mCardAdapter.getCommentCardCount()));
                            mCardAdapter.notifyDataSetChanged();
                            dismissComment();
                        }
                    });
                }

            }
        });
    }

    @Override
    public boolean onSend(String content) {
        final User me = AccountManager.getInstance(this).getMe();
        if (me == null) {
            com.metis.base.ActivityDispatcher.loginActivityWhenAlreadyIn(this);
            return false;
        }
        final Comment comment = me.userRole == User.USER_ROLE_TEACHER ? mCardAdapter.getMyCardHeader(me.userId) : mReplyingComment;
        int commentSource = 1;/*CommentFactory.getCommentSource(me);*/
        long replyUserId = 0;
        long replyCid = 0;
        if (comment != null) {
            replyUserId = comment.user.userId;
            replyCid = comment.id;
        }
        if (me.equals(mStatus.user)) {
            if (comment != null) {
                commentSource = CommentFactory.getCommentSource(me, comment.user);
            }
        } else if (me.userRole == User.USER_ROLE_TEACHER) {
            commentSource = 0;
        } else if (me.userRole == User.USER_ROLE_STUDENT || me.userRole == User.USER_ROLE_FANS) {
            commentSource = 1;
        }

        //final Comment comment = commentSource == 0 ? mCardAdapter.getMyCardHeader(me.userId) : mReplyingComment;

        /*User replyUser = comment != null ? comment.user : null;
        Log.v(TAG, "onSend " + content + " comment=" + comment);*/
        final int useCommentSource = commentSource;
        StatusManager.getInstance(StatusDetailActivity.this).pushComment(mStatus.id, me.userId, replyUserId, content, replyCid, null, null, 0, 3, commentSource, me.getCookie(), new RequestCallback<Comment>() {
            @Override
            public void callback(ReturnInfo<Comment> returnInfo, String callbackId) {
                if (!returnInfo.isSuccess()) {
                    Toast.makeText(StatusDetailActivity.this, getString(R.string.toast_comment_failed, returnInfo.getMessage()), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(StatusDetailActivity.this, R.string.toast_comment_success, Toast.LENGTH_SHORT).show();
                if (useCommentSource == CommentFactory.COMMENT_SOURCE_TEACHER) {
                    Comment newComment = returnInfo.getData();
                    Log.v(TAG, "pushComment newComment=" + newComment);
                    mCardAdapter.addCommentFollow(mStatus, comment, newComment);
                    mTabItem.setTextLeft(getString(R.string.status_detail_tab_teacher, mCardAdapter.getCommentCardCount()));
                    mCardAdapter.notifyDataSetChanged();
                    //mDetailRv.smoothScrollToPosition(mCardAdapter.getItemCount() - 1);
                } else {
                    mListAdapter.addDataItem(new CommentItemDelegate(returnInfo.getData(), mStatus));
                    mTabItem.setTextRight(getString(R.string.status_detail_tab_student, mListAdapter.getItemCount() - 2));
                    mListAdapter.notifyDataSetChanged();
                    mDetailRv.smoothScrollToPosition(mListAdapter.getItemCount() - 1);
                }
                dismissComment();
            }
        });
        return me != null && me.userRole == User.USER_ROLE_TEACHER;
    }

    @Override
    public void onInputMaskClick(View view) {
        User me = AccountManager.getInstance(this).getMe();
        if (me == null) {
            com.metis.base.ActivityDispatcher.loginActivityWhenAlreadyIn(this);
        }
    }

    @Override
    public void onClick(View v, Comment comment, Status status) {
        replyingComment(comment);
        mChatFragment.askToInput();
    }

    @Override
    public void onLongClick(View v, Comment comment, Status status) {

    }

    private void replyingComment (Comment comment) {
        mReplyingComment = comment;
        mReplyingContainer.setVisibility(View.VISIBLE);
        User user = comment.user;
        User me = AccountManager.getInstance(this).getMe();
        if (user != null) {
            mReplyingInfoTv.setText(getString(R.string.replying_to, me != null && me.userRole == User.USER_ROLE_TEACHER ? mStatus.user.name : user.name));
        }
    }

    private void dismissComment () {
        mReplyingComment = null;
        mReplyingContainer.setVisibility(View.GONE);
    }

    /*@Override
    public void onUserChanged(User user, boolean onLine) {
        checkUser(user);
    }*/
}

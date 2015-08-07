package com.metis.commentpart.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.activity.TitleBarActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
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
import com.metis.commentpart.manager.StatusManager;
import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.CommentCollection;
import com.metis.commentpart.module.Status;
import com.metis.commentpart.module.StatusDetailTabItem;
import com.metis.commentpart.module.TeacherComment;
import com.metis.commentpart.utils.CommentFactory;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.ArrayList;
import java.util.List;


public class StatusDetailActivity extends TitleBarActivity implements CardFooterDelegate.OnCommentFooterClickListener, View.OnClickListener {

    private static final String TAG = StatusDetailActivity.class.getSimpleName();

    private Status mStatus = null;
    private TextView mReplyTextTv;
    private ImageView mReplyVoiceIv;
    private RecyclerView mDetailRv = null;
    private CommentCardAdapter mCardAdapter = null;
    private CommentListAdapter mListAdapter = null;

    private StatusDetailTabItem mTabItem = null;

    private CommentCardDecoration mCardDecoration = null;
    private CommentListDecoration mListDecoration = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);

        mStatus = (Status)getIntent().getSerializableExtra(ActivityDispatcher.KEY_STATUS);

        mReplyTextTv = (TextView)findViewById(R.id.reply_text);
        mReplyVoiceIv = (ImageView)findViewById(R.id.reply_voice);

        mDetailRv = (RecyclerView)findViewById(R.id.detail_recycler_view);

        mCardDecoration = new CommentCardDecoration();
        mListDecoration = new CommentListDecoration();

        mDetailRv.setLayoutManager(new LinearLayoutManager(this));
        mCardAdapter = new CommentCardAdapter(this);
        mListAdapter = new CommentListAdapter(this);

        switchToCard();

        mReplyTextTv.setOnClickListener(this);
        mReplyVoiceIv.setOnClickListener(this);

    }

    private void switchToCard () {
        mDetailRv.removeItemDecoration(mListDecoration);
        mDetailRv.addItemDecoration(mCardDecoration);
        mDetailRv.setAdapter(mCardAdapter);
    }

    private void switchToList () {
        mDetailRv.removeItemDecoration(mCardDecoration);
        mDetailRv.addItemDecoration(mListDecoration);
        mDetailRv.setAdapter(mListAdapter);
        Log.v(TAG, "switchToList ");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        StatusDelegate statusDelegate = new StatusDelegate(mStatus);
        statusDelegate.setIsInDetails(true);
        mCardAdapter.addDataItem(statusDelegate);
        mListAdapter.addDataItem(statusDelegate);

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
        StatusDetailTabDelegate tabDelegate = new StatusDetailTabDelegate(mTabItem);

        mCardAdapter.addDataItem(tabDelegate);
        mCardAdapter.notifyDataSetChanged();

        mListAdapter.addDataItem(tabDelegate);
        mListAdapter.notifyDataSetChanged();

        User me = AccountManager.getInstance(this).getMe();
        if (me == null) {
            //TODO
            return;
        }
        StatusManager.getInstance(this).getCommentList(mStatus.id, me.getCookie(), new RequestCallback<CommentCollection>() {
            @Override
            public void callback(ReturnInfo<CommentCollection> returnInfo, String callbackId) {
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
                            teacherDelegates.add(textTDelegate);

                            if (teacherComment.hasSubCommentList()) {
                                List<Comment> subList = teacherComment.subCommentList;
                                final int subLength = subList.size();
                                for (int k = 0; k < subLength; k++) {
                                    Comment innerComment = subList.get(k);
                                    BaseDelegate innerDelegate = CommentFactory.makeCommentDelegate(innerComment);
                                    teacherDelegates.add(innerDelegate);
                                    //TODO
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
                            itemDelegateList.add(itemDelegate);
                        }
                        mTabItem.setTextRight(getString(R.string.status_detail_tab_student, length));
                        mListAdapter.addDataList(itemDelegateList);
                    } else {
                        //TODO
                    }
                    mListAdapter.notifyDataSetChanged();
                }

            }
        });
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
        //Toast.makeText(this, comment.id + "", Toast.LENGTH_SHORT).show();
        ActivityDispatcher.replyActivity(this, mStatus, comment, replyType);
    }

    @Override
    public void onClick(View v) {
        User me = AccountManager.getInstance(this).getMe();
        if (me == null) {
            //TODO
            return;
        }
        Comment comment = mCardAdapter.getMyCardHeader(me);
        final int id = v.getId();
        if (id == mReplyTextTv.getId()) {
            if (comment != null) {
                Log.v(TAG, "comment id=" + comment + " text=" + comment.content);
                ActivityDispatcher.replyActivity(this, mStatus, comment, ReplyActivity.REPLY_TYPE_TEXT);
            } else {
                ActivityDispatcher.replyActivity(this, mStatus, ReplyActivity.REPLY_TYPE_TEXT);
            }
        } else if (id == mReplyVoiceIv.getId()) {
            if (comment != null) {
                ActivityDispatcher.replyActivity(this, mStatus, comment, ReplyActivity.REPLY_TYPE_VOICE);
            } else {
                ActivityDispatcher.replyActivity(this, mStatus, ReplyActivity.REPLY_TYPE_VOICE);
            }
        }
    }
}

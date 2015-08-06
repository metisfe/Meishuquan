package com.metis.commentpart.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.metis.base.activity.TitleBarActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.commentpart.ActivityDispatcher;
import com.metis.commentpart.R;
import com.metis.commentpart.manager.StatusManager;
import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.Status;
import com.metis.commentpart.utils.CommentFactory;

public class ReplyActivity extends TitleBarActivity {

    private static final String TAG = ReplyActivity.class.getSimpleName();

    public static final int MODE_STATUS = 0, MODE_COMMENT = 1;

    public static final int REPLY_TYPE_TEXT = 0, REPLY_TYPE_VOICE = 1;

    private Status mStatus = null;
    private Comment mComment = null;
    private User mUser = null;
    private int mMode = 0;
    private int mReplyType = REPLY_TYPE_TEXT;

    private EditText mReplyEt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        mStatus = (Status)getIntent().getSerializableExtra(ActivityDispatcher.KEY_STATUS);
        mReplyType = getIntent().getIntExtra(ActivityDispatcher.KEY_REPLY_TYPE, REPLY_TYPE_TEXT);
        mUser = mStatus.user;
        if (isCommentMode()) {
            mComment = (Comment)getIntent().getSerializableExtra(ActivityDispatcher.KEY_COMMENT);
            mUser = mComment.user;
        }

        mReplyEt = (EditText)findViewById(R.id.reply_text);
        mReplyEt.setHint(getString(R.string.reply_to, mUser.name));
        getTitleBar().setDrawableResourceRight(R.drawable.ic_send);
        getTitleBar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User me = AccountManager.getInstance(ReplyActivity.this).getMe();
                if (me == null) {
                    //TODO
                    return;
                }
                String content = mReplyEt.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    //TODO
                    return;
                }
                Log.v(TAG, "inCommentMode=" + isCommentMode());
                if (isCommentMode()) {
                    StatusManager.getInstance(ReplyActivity.this).pushComment(mStatus.id, 0, mComment.user.userId, content, mComment.id, null, null, null, mReplyType, CommentFactory.getCommentSource(me), me.getCookie());
                } else {
                    StatusManager.getInstance(ReplyActivity.this).pushComment(mStatus.id, 0, 0, content, 0, null, null, null, mReplyType,  CommentFactory.getCommentSource(me), me.getCookie());
                }
                finish();
            }
        });
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.title_activity_reply);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    public boolean isCommentMode() {
        mMode = getIntent().getIntExtra(ActivityDispatcher.KEY_MODE, MODE_STATUS);
        return mMode == MODE_COMMENT;
    }

}

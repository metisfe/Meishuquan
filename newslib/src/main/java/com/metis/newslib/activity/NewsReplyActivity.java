package com.metis.newslib.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.base.activity.BaseActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.base.utils.TimeUtils;
import com.metis.msnetworklib.contract.ReturnInfo;
import com.metis.newslib.ActivityDispatcher;
import com.metis.newslib.R;
import com.metis.newslib.manager.NewsManager;
import com.metis.newslib.module.NewsCommentItem;
import com.metis.newslib.module.NewsDetails;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsReplyActivity extends BaseActivity {

    private static final String TAG = NewsReplyActivity.class.getSimpleName();

    private static final int MAX_COUNT = 200;

    private View mBg = null;

    private EditText mReplyEt = null;
    private TextView mReplyCountTv = null;
    private TextView mReplyBtn = null;
    private TextView mReplyToTv = null;

    private NewsDetails mDetails = null;
    private NewsCommentItem mItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_reply);

        mDetails = (NewsDetails)getIntent().getSerializableExtra(ActivityDispatcher.KEY_NEWS_DETAIL);
        mItem = (NewsCommentItem)getIntent().getSerializableExtra(ActivityDispatcher.KEY_NEWS_COMMENT_ITEM);

        mReplyToTv = (TextView)findViewById(R.id.news_reply_to);
        mReplyEt = (EditText)findViewById(R.id.news_reply_input);
        mReplyCountTv = (TextView)findViewById(R.id.news_reply_count);
        mReplyBtn = (TextView)findViewById(R.id.news_reply_send_btn);

        if (mItem != null) {
            mReplyToTv.setVisibility(View.VISIBLE);
            mReplyToTv.setText(getString(R.string.text_reply_to, mItem.user.name));
        }

        mReplyCountTv.setText(getString(R.string.text_reply_max_count, MAX_COUNT));

        mBg = findViewById(R.id.reply_bg);
        mBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mReplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = mReplyEt.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(NewsReplyActivity.this, R.string.toast_reply_not_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (content.length() > MAX_COUNT) {
                    Toast.makeText(NewsReplyActivity.this, R.string.toast_reply_too_long, Toast.LENGTH_SHORT).show();
                    return;
                }
                User me = AccountManager.getInstance(NewsReplyActivity.this).getMe();
                if (me == null) {
                    //TODO
                    com.metis.base.ActivityDispatcher.loginActivityWhenAlreadyIn(NewsReplyActivity.this);
                    return;
                }
                if (mDetails == null) {
                    Log.v(TAG, "mDetails == null");
                    return;
                }
                NewsManager.getInstance(NewsReplyActivity.this).postComment(mItem != null ? mItem.user.userId : 0, mDetails.newsId, content, mItem != null ? mItem.id : 0, me.getCookie(), new RequestCallback<Integer>() {
                    @Override
                    public void callback(ReturnInfo<Integer> returnInfo, String callbackId) {
//                        Toast.makeText(NewsReplyActivity.this, "pushComemnt " + returnInfo.isSuccess(), Toast.LENGTH_SHORT).show();
                        if (returnInfo.isSuccess()) {
                            NewsCommentItem item = new NewsCommentItem();
                            item.id = returnInfo.getData();
                            item.content = content;
                            if (mItem != null) {
                                item.replyUser = mItem.user;
                            }
                            item.user = AccountManager.getInstance(NewsReplyActivity.this).getMe();
                            item.commentDateTime = TimeUtils.STD_DATE_FORMAT.format(new Date());
                            Intent it = new Intent();
                            it.putExtra(ActivityDispatcher.KEY_NEWS_COMMENT_ITEM, item);
                            setResult(RESULT_OK, it);
                            finish();
                            Toast.makeText(NewsReplyActivity.this, R.string.toast_reply_success, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NewsReplyActivity.this, R.string.toast_reply_failed, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
        mReplyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = MAX_COUNT - s.length();
                mReplyCountTv.setText(getString(R.string.text_reply_max_count, count));
                if (count < 0) {
                    mReplyCountTv.setTextColor(getResources().getColor(R.color.color_c1));
                } else {
                    mReplyCountTv.setTextColor(getResources().getColor(R.color.color_c2));
                }
            }
        });
    }

}

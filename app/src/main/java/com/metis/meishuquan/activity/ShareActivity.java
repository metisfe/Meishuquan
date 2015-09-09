package com.metis.meishuquan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.metis.base.ActivityDispatcher;
import com.metis.base.R;
import com.metis.base.activity.BaseActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.meishuquan.manager.ShareManager;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class ShareActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ShareActivity.class.getSimpleName();

    private View mShareBg = null;

    private View mWeChatBtn, mShareMomentsBtn, mSinaWeiboBtn, mQqBtn, mQzoneBtn, mCircelBtn;

    private String mTitle, mText, mImageUrl, mUrl;

    private PlatformActionListener mActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            mShareBg.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ShareActivity.this, R.string.toast_share_success, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.v(TAG, "onError throwable=" + throwable.getMessage());
            mShareBg.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ShareActivity.this, R.string.toast_share_failed, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onCancel(Platform platform, int i) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        mTitle = getIntent().getStringExtra(ActivityDispatcher.KEY_TITLE);
        mText = getIntent().getStringExtra(ActivityDispatcher.KEY_TEXT);
        mImageUrl = getIntent().getStringExtra(ActivityDispatcher.KEY_IMAGE_URL);
        mUrl = getIntent().getStringExtra(ActivityDispatcher.KEY_URL);

        mShareBg = findViewById(R.id.share_bg);
        mShareBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mWeChatBtn = findViewById(R.id.share_we_chat);
        mQqBtn = findViewById(R.id.share_qq);
        mQzoneBtn = findViewById(R.id.share_qzone);
        mSinaWeiboBtn = findViewById(R.id.share_sina);
        mShareMomentsBtn = findViewById(R.id.share_moments);
        mCircelBtn = findViewById(R.id.share_circel);

        mWeChatBtn.setOnClickListener(this);
        mQqBtn.setOnClickListener(this);
        mQzoneBtn.setOnClickListener(this);
        mSinaWeiboBtn.setOnClickListener(this);
        mShareMomentsBtn.setOnClickListener(this);
        mCircelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        ShareManager manager = ShareManager.getInstance(this);
        if (id == mWeChatBtn.getId()) {
            manager.weChatShare(mTitle, mText, mImageUrl, mUrl, mActionListener);
        } else if (id == mQqBtn.getId()) {
            manager.qqShare(mTitle, mText, mImageUrl, mUrl, mActionListener);
        } else if (id == mQzoneBtn.getId()) {
            manager.qZoneShare(mTitle, mText, mImageUrl, mUrl, mActionListener);
        } else if (id == mSinaWeiboBtn.getId()) {
            manager.sinaWeiboShare(mTitle, mText, mImageUrl, mUrl, mActionListener);
        } else if (id == mShareMomentsBtn.getId()) {
            manager.weChatMomentsShare(mTitle, mText, mImageUrl, mUrl, mActionListener);
        } else if (id == mCircelBtn.getId()) {
            User me = AccountManager.getInstance(this).getMe();
            if (me == null) {
                //TODO
                return;
            }
            manager.circleShare(mTitle, mText, mImageUrl, mUrl, me.getCookie(), new RequestCallback() {

                @Override
                public void callback(ReturnInfo returnInfo, String callbackId) {
                    if (returnInfo.isSuccess()) {
                        Toast.makeText(ShareActivity.this, R.string.toast_share_success, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ShareActivity.this, R.string.toast_share_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

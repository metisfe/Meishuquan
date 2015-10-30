package com.metis.base.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.metis.base.ActivityDispatcher;
import com.metis.base.R;
import com.metis.base.fragment.CcPlayFragment;
import com.metis.base.widget.VideoMaskView;

public class PlayerActivity extends BaseActivity {

    private CcPlayFragment mPlayFragment = null;
    private VideoMaskView mMaskView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

        mPlayFragment = (CcPlayFragment)getSupportFragmentManager().findFragmentById(R.id.player_fragment);
        mMaskView = (VideoMaskView)findViewById(R.id.player_video_mask);
        mMaskView.fullScreenEnable(false);
        mMaskView.setOnButtonListener(new VideoMaskView.OnButtonListener() {
            @Override
            public void onBackBtnPressed(@VideoMaskView.State int state) {
                PlayerActivity.this.onBackPressed();
            }

            @Override
            public void onPlayOrPauseBtnPressed(@VideoMaskView.State int state) {
                if (state == VideoMaskView.STATE_PAUSED) {
                    mPlayFragment.resumePlay();
                } else {
                    mPlayFragment.pausePlay();
                }

            }

            @Override
            public void onFullScreenPressed(@VideoMaskView.State int state) {

            }

            @Override
            public void onSeekStart(SeekBar seekBar) {
                mPlayFragment.setSeeking(true);
            }

            @Override
            public void onSeekStop(SeekBar seekBar) {
                mPlayFragment.seekTo(mPlayFragment.getDuration() / 100 * seekBar.getProgress());
                mPlayFragment.setSeeking(false);
            }
        });

        mPlayFragment.registerPlayerCallback(mMaskView);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        final String url = getIntent().getStringExtra(ActivityDispatcher.KEY_URL);
        mMaskView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPlayFragment.startRemotePlayWithUrl(url);
            }
        }, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayFragment.unregisterPlayerCallback(mMaskView);
    }
}

package com.metis.playerlib;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.cyberplayer.core.BVideoView;

/**
 * Created by gaoyunfei on 15/7/18.
 */
public class VideoWrapperFragment extends Fragment implements
        PlayCallback, VideoFragment.OnFullScreenListener,
        VideoFragment.OnPositionListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    private static final String TAG = VideoWrapperFragment.class.getSimpleName();

    private VideoFragment mVideoFragment = null;

    private String mSource = null;
    private String mTitle = null;

    private PlayCallback mPlayCallback = null;

    private VideoFragment.OnFullScreenListener mFullScreenListener = null;

    private VideoFragment.OnPositionListener mPositionListener = null;

    private RelativeLayout mControllerLayout, mTopControllerLayout, mBottomControllerLayout;
    private ImageView mBackIv, mStartIv, mFullScreenIv, mPreviewImageIv;
    private TextView mPositionTv, mDurationTv, mTitleTv;
    private SeekBar mSeekBar = null;

    private boolean isSeeking = false;
    private boolean isControllerShowing = true;

    private Runnable mAutoHideRunnable = new Runnable() {
        @Override
        public void run() {
            if (isSeeking) {
                mControllerLayout.postDelayed(this, 5000);
            } else {
                if (isControllerShowing) {
                    hideControllerViews();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_wrapper, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVideoFragment = (VideoFragment)getChildFragmentManager().findFragmentById(R.id.video_wrapper_fragment);

        mControllerLayout = (RelativeLayout)view.findViewById(R.id.player_controller);
        mTopControllerLayout = (RelativeLayout)view.findViewById(R.id.player_controller_top_bar);
        mBottomControllerLayout = (RelativeLayout)view.findViewById(R.id.player_controller_bottom_bar);
        mBackIv = (ImageView)view.findViewById(R.id.player_wrapper_back_btn);
        mStartIv = (ImageView)view.findViewById(R.id.player_wrapper_play_btn);
        mFullScreenIv = (ImageView)view.findViewById(R.id.player_wrapper_full_screen);
        mPreviewImageIv = (ImageView)view.findViewById(R.id.player_wrapper_preview_image);
        mPositionTv = (TextView)view.findViewById(R.id.player_current);
        mDurationTv = (TextView)view.findViewById(R.id.player_duration);
        mTitleTv = (TextView)view.findViewById(R.id.player_sub_title);
        mSeekBar = (SeekBar)view.findViewById(R.id.player_seek_bar);

        mControllerLayout.setOnClickListener(this);
        mBackIv.setOnClickListener(this);
        mStartIv.setOnClickListener(this);
        mFullScreenIv.setOnClickListener(this);

        setSource(mSource);

        mSeekBar.setOnSeekBarChangeListener(this);
        mVideoFragment.setPlayCallback(this);
        mVideoFragment.setOnFullScreenListener(this);
        mVideoFragment.setOnPositionListener(this);
    }

    public ImageView getPreviewImageView () {
        return mPreviewImageIv;
    }

    public void setSource (String source) {
        mSource = source;
        if (mVideoFragment != null) {
            mVideoFragment.setSource(source);
        }
    }

    public void setTitle (String title) {
        mTitle = title;
        if (mTitleTv != null) {
            mTitleTv.setText(title);
        }
    }

    public boolean isStarted () {
        return mVideoFragment != null
                && (mVideoFragment.getState() == VideoFragment.State.PAUSED
                || mVideoFragment.getState() == VideoFragment.State.PLAYING);
    }

    public void startPlay () {
        if (mVideoFragment != null) {
            mVideoFragment.startPlay();
        }
    }

    public void pausePlay () {
        if (mVideoFragment != null) {
            mVideoFragment.pausePlay();
        }
    }

    public void resumePlay () {
        if (mVideoFragment != null) {
            mVideoFragment.resumePlay();
        }
    }

    public void stopPlay (VideoFragment.OnStopCallback stopCallback) {
        if (mVideoFragment != null) {
            mVideoFragment.stopPlay(stopCallback);
        }
    }

    public boolean isFullScreen () {
        return mVideoFragment != null && mVideoFragment.isFullScreen();
    }

    public void setFullScreen (boolean isFullScreen) {
        if (mVideoFragment != null) {
            mVideoFragment.setFullScreen(isFullScreen);
        }
    }

    public void setPlayCallback (PlayCallback callback) {
        mPlayCallback = callback;

    }

    public void setOnFullScreenListener (VideoFragment.OnFullScreenListener listener) {
        mFullScreenListener = listener;
    }

    public void hideControllerViews () {
        if (!isFullScreen()) {
            return;
        }
        if (isControllerShowing) {
            mControllerLayout.removeCallbacks(mAutoHideRunnable);
            ObjectAnimator topAnimator = ObjectAnimator.ofFloat(mTopControllerLayout, "translationY", 0, -mTopControllerLayout.getHeight());
            topAnimator.start();
            ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(mBottomControllerLayout, "translationY", 0, mTopControllerLayout.getHeight());
            bottomAnimator.start();
            isControllerShowing = false;
        }

    }

    public void showControllerViews (boolean autoHide) {
        if (!isFullScreen()) {
            return;
        }
        if (!isControllerShowing) {
            mControllerLayout.removeCallbacks(mAutoHideRunnable);
            ObjectAnimator topAnimator = ObjectAnimator.ofFloat(mTopControllerLayout, "translationY", -mTopControllerLayout.getHeight(), 0);
            topAnimator.start();
            ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(mBottomControllerLayout, "translationY", mTopControllerLayout.getHeight(), 0);
            bottomAnimator.start();
            isControllerShowing = true;
            if (autoHide) {
                mControllerLayout.postDelayed(mAutoHideRunnable, 5000);
            }
        }

    }

    public void setOnPositionListener (VideoFragment.OnPositionListener listener) {
        mPositionListener = listener;
    }

    @Override
    public void onFullScreen(boolean isFullScreen) {
        mFullScreenIv.setSelected(isFullScreen);
        showControllerViews(isFullScreen);
        if (mFullScreenListener != null) {
            mFullScreenListener.onFullScreen(isFullScreen);
        }
    }

    @Override
    public void onUpdate(int current, int total) {
        if (!isSeeking) {
//            mPositionTv.setText(TimerHelper.formatTime(current));
//            mDurationTv.setText(TimerHelper.formatTime(total));
            mSeekBar.setMax(total);
            mSeekBar.setProgress(current);
        }

        if (mPositionListener != null) {
            mPositionListener.onUpdate(current, total);
        }
    }

    @Override
    public void onStarted(BVideoView bVideoView) {
        mStartIv.setSelected(true);
        mPreviewImageIv.setVisibility(View.GONE);
        if (mPlayCallback != null) {
            mPlayCallback.onStarted(bVideoView);
        }
    }

    @Override
    public void onPaused(BVideoView bVideoView) {
        mStartIv.setSelected(false);
        if (mPlayCallback != null) {
            mPlayCallback.onPaused(bVideoView);
        }
    }

    @Override
    public void onResumed(BVideoView bVideoView) {
        mStartIv.setSelected(true);
        if (mPlayCallback != null) {
            mPlayCallback.onResumed(bVideoView);
        }
    }

    @Override
    public void onCompleted(BVideoView bVideoView) {
        mStartIv.setSelected(false);
        mPreviewImageIv.setVisibility(View.VISIBLE);
        showControllerViews(false);
        if (mPlayCallback != null) {
            mPlayCallback.onCompleted(bVideoView);
        }
    }

    @Override
    public boolean onError(BVideoView bVideoView, int what, int extra) {
        bVideoView.post(new Runnable() {
            @Override
            public void run() {
                mStartIv.setSelected(false);
                mPreviewImageIv.setVisibility(View.VISIBLE);
            }
        });

        if (mPlayCallback != null) {
            return mPlayCallback.onError(bVideoView, what, extra);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == mBackIv.getId()) {
            if (mVideoFragment.isFullScreen()) {
                mVideoFragment.setFullScreen(false);
            } else {
                getActivity().finish();
            }
        } else if (id == mStartIv.getId()) {
            if (mVideoFragment == null || TextUtils.isEmpty(mVideoFragment.getSource())) {
                Toast.makeText(getActivity(), R.string.video_source_not_set, Toast.LENGTH_SHORT).show();
                return;
            }
            if (mVideoFragment.isPlaying()) {
                Log.v(TAG, "pause Btn cause pausePlay");
                mVideoFragment.pausePlay();
            } else {
                mVideoFragment.startPlay();
            }
        } else if (id == mFullScreenIv.getId()) {
            mVideoFragment.setFullScreen(!mVideoFragment.isFullScreen());
        } else if (id == mControllerLayout.getId()) {
            if (isControllerShowing) {
                hideControllerViews();
            } else {
                showControllerViews(true);
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mPositionTv.setText(TimerHelper.formatTime(progress));
        mDurationTv.setText(TimerHelper.formatTime(seekBar.getMax()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isSeeking = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isSeeking = false;
        mVideoFragment.seekTo(seekBar.getProgress());
    }
}

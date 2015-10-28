package com.metis.base.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.metis.base.R;
import com.metis.base.fragment.CcPlayFragment;
import com.metis.base.manager.DisplayManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DecimalFormat;

/**
 * Created by Beak on 2015/10/19.
 */
public class VideoMaskView extends RelativeLayout implements CcPlayFragment.PlayerCallback, View.OnClickListener{

    private static final int MINUTES_LENGTH = 60 * 1000;
    public static final int
            STATE_IDLE = 0, STATE_STARTED = 1, STATE_PAUSED = 2, STATE_STOPPED = 3;

    /** @hide */
    @IntDef({STATE_IDLE, STATE_STARTED, STATE_PAUSED, STATE_STOPPED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}

    private int mState = STATE_IDLE;

    private View mTopLayout, mBottomLayout, mGestureLayout;
    private ImageView mBackIv = null, mPlayPauseIv = null, mFullScreenIv = null, mCoverIv = null;
    private SeekBar mSeekBar = null;
    private TextView mTitleTv, mCurrentTv, mDurationTv;
    private ProgressBar mLoadingBar = null;

    private OnButtonListener mBtnListener = null;

    private int mLastDuration = 0;
    private DecimalFormat mTimeFormat = new DecimalFormat("00");

    private boolean isControlShowing = true;
    private AnimatorSet mAnimationSet = null;

    private Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hideTopBottom();
        }
    };

    public VideoMaskView(Context context) {
        this(context, null);
    }

    public VideoMaskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initThis(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoMaskView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initThis(context);
    }

    private void initThis (Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_video_mask, this);

        mTopLayout = findViewById(R.id.video_mask_top_container);
        mBottomLayout = findViewById(R.id.video_mask_bottom_container);
        mGestureLayout = findViewById(R.id.video_mask_gesture_panel);

        mBackIv = (ImageView)findViewById(R.id.video_mask_back);
        mPlayPauseIv = (ImageView)findViewById(R.id.video_mask_play_or_pause);
        mFullScreenIv = (ImageView)findViewById(R.id.video_mask_video_fullscreen_switch);
        mCoverIv = (ImageView)findViewById(R.id.video_mask_cover_iv);
        mSeekBar = (SeekBar)findViewById(R.id.video_mask_seek_bar);
        mTitleTv = (TextView)findViewById(R.id.video_mask_title);
        mCurrentTv = (TextView)findViewById(R.id.video_mask_current_position);
        mDurationTv = (TextView)findViewById(R.id.video_mask_duration);
        mLoadingBar = (ProgressBar)findViewById(R.id.video_mask_loading);

        mBackIv.setOnClickListener(this);
        mPlayPauseIv.setOnClickListener(this);
        mFullScreenIv.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mBtnListener != null) {
                    mBtnListener.onSeekStart(seekBar);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mBtnListener != null) {
                    mBtnListener.onSeekStop(seekBar);
                }
            }
        });
        mGestureLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnimationSet != null && mAnimationSet.isRunning()) {
                    return;
                }
                if (isControlShowing) {
                    hideTopBottom();
                } else {
                    showTopBottom();
                }
            }
        });
    }

    public void setTitle (CharSequence title) {
        mTitleTv.setText(title);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == mPlayPauseIv.getId()) {
            if (mBtnListener != null) {
                mBtnListener.onPlayOrPauseBtnPressed(mState);
            }
        } else if (id == mBackIv.getId()) {
            if (mBtnListener != null) {
                mBtnListener.onBackBtnPressed(mState);
            }
        } else if (id == mFullScreenIv.getId()) {
            if (mBtnListener != null) {
                mBtnListener.onFullScreenPressed(mState);
            }
        }
    }

    public void setFullScreen (boolean fullScreen) {
        mFullScreenIv.setSelected(fullScreen);
    }

    @Override
    public void onPlayerStarted() {
        mState = STATE_STARTED;
        mPlayPauseIv.setSelected(mState == STATE_STARTED);
        mCoverIv.setVisibility(GONE);
        mSeekBar.setSecondaryProgress(0);
        mLoadingBar.setVisibility(VISIBLE);
        delayHide();
    }

    @Override
    public void onPlayerPrepared(int width, int height) {
        mLoadingBar.setVisibility(GONE);
    }

    @Override
    public void onPlayerPaused() {
        mState = STATE_PAUSED;
        mPlayPauseIv.setSelected(mState == STATE_STARTED);
        delayHide();
    }

    @Override
    public void onPlayerResumed() {
        mState = STATE_STARTED;
        mPlayPauseIv.setSelected(mState == STATE_STARTED);
        delayHide();
    }

    @Override
    public void onPlayerStopped() {
        mState = STATE_STOPPED;
        mPlayPauseIv.setSelected(mState == STATE_STARTED);
    }

    @Override
    public void onPlayerCompleted() {
        mState = STATE_IDLE;
        mPlayPauseIv.setSelected(mState == STATE_STARTED);
        showTopBottom();
        mCurrentTv.setText(mTimeFormat.format(0) + ":" + mTimeFormat.format(0));
        mSeekBar.setProgress(0);
        mSeekBar.setSecondaryProgress(0);
    }

    @Override
    public void onUpdateProgress(int current, int duration, int progress) {
        mSeekBar.setProgress(progress);
        int currentMins = current / MINUTES_LENGTH;
        int currentSecs = current % MINUTES_LENGTH / 1000;
        if (mLastDuration != duration) {
            int durationMins = duration / MINUTES_LENGTH;
            int durationSecs = duration % MINUTES_LENGTH / 1000;

            mDurationTv.setText(mTimeFormat.format(durationMins) + ":" + mTimeFormat.format(durationSecs));

            mLastDuration = duration;
        }


        mCurrentTv.setText(mTimeFormat.format(currentMins) + ":" + mTimeFormat.format(currentSecs));
    }

    @Override
    public void onBuffering(int percent) {
        mSeekBar.setSecondaryProgress(percent);
    }

    public void delayHide () {
        this.removeCallbacks(mHideRunnable);
        postDelayed(mHideRunnable, 5000);
    }

    private void hideTopBottom () {
        if (!isControlShowing) {
            return;
        }
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator hideTop = ObjectAnimator.ofFloat(mTopLayout, "translationY", 0, -mTopLayout.getHeight());
        ObjectAnimator hideBottom = ObjectAnimator.ofFloat(mBottomLayout, "translationY", 0, mBottomLayout.getHeight());
        set.play(hideTop).with(hideBottom);
        set.start();
        mAnimationSet = set;
        isControlShowing = false;
    }

    private void showTopBottom () {
        if (isControlShowing) {
            return;
        }
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator showTop = ObjectAnimator.ofFloat(mTopLayout, "translationY", -mTopLayout.getHeight(), 0);
        ObjectAnimator showBottom = ObjectAnimator.ofFloat(mBottomLayout, "translationY", mBottomLayout.getHeight(), 0);
        set.play(showTop).with(showBottom);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                delayHide();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
        mAnimationSet = set;
        isControlShowing = true;
    }

    public void fullScreenEnable (boolean enable) {
        if (enable) {
            mFullScreenIv.setVisibility(VISIBLE);
        } else {
            mFullScreenIv.setVisibility(GONE);
        }
    }

    public void setCover (String url) {
        DisplayManager.getInstance(getContext()).display(url, mCoverIv);
    }

    public void setOnButtonListener (OnButtonListener listener) {
        mBtnListener = listener;
    }

    public interface OnButtonListener {
        public void onBackBtnPressed (@State int state);
        public void onPlayOrPauseBtnPressed (@State int state);
        public void onFullScreenPressed (@State int state);
        public void onSeekStart (SeekBar seekBar);
        public void onSeekStop (SeekBar seekBar);
    }
}

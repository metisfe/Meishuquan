package com.metis.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.metis.base.R;
import com.metis.base.manager.PlayerManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Beak on 2015/10/19.
 */
public class VideoMaskView extends RelativeLayout implements PlayerManager.PlayerCallback, View.OnClickListener{

    public static final int
            STATE_IDLE = 0, STATE_STARTED = 1, STATE_PAUSED = 2, STATE_STOPPED = 3;

    /** @hide */
    @IntDef({STATE_IDLE, STATE_STARTED, STATE_PAUSED, STATE_STOPPED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}

    private int mState = STATE_IDLE;

    private ImageView mBackIv = null, mPlayPauseIv = null;

    private OnButtonListener mBtnListener = null;

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
        mBackIv = (ImageView)findViewById(R.id.video_mask_back);
        mPlayPauseIv = (ImageView)findViewById(R.id.video_mask_play_or_pause);

        mBackIv.setOnClickListener(this);

        mPlayPauseIv.setOnClickListener(this);
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
        }
    }

    @Override
    public void onPlayerStarted() {
        mState = STATE_STARTED;
        mPlayPauseIv.setSelected(mState == STATE_STARTED);
    }

    @Override
    public void onPlayerPrepared(int width, int height) {

    }

    @Override
    public void onPlayerPaused() {
        mState = STATE_PAUSED;
        mPlayPauseIv.setSelected(mState == STATE_STARTED);
    }

    @Override
    public void onPlayerResumed() {
        mState = STATE_STARTED;
        mPlayPauseIv.setSelected(mState == STATE_STARTED);
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
    }

    public void setOnButtonListener (OnButtonListener listener) {
        mBtnListener = listener;
    }

    public interface OnButtonListener {
        public void onBackBtnPressed (@State int state);
        public void onPlayOrPauseBtnPressed (@State int state);
        public void onFullScreenPressed (@State int state);
    }
}

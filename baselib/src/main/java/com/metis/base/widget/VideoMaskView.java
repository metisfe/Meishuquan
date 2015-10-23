package com.metis.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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
public class VideoMaskView extends RelativeLayout implements PlayerManager.PlayerCallback{

    public static final int
            STATE_IDLE = 0, STATE_STARTED = 1, STATE_PAUSED = 2, STATE_STOPPED = 3;

    /** @hide */
    @IntDef({STATE_IDLE, STATE_STARTED, STATE_PAUSED, STATE_STOPPED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}


    private ImageView mBackIv = null, mPlayPauseIv = null;

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
    }

    @Override
    public void onPlayerStarted() {
        mPlayPauseIv.setSelected(true);
    }

    @Override
    public void onPlayerPrepared(int width, int height) {

    }

    @Override
    public void onPlayerPaused() {

    }

    @Override
    public void onPlayerResumed() {

    }

    @Override
    public void onPlayerStopped() {

    }

    @Override
    public void onPlayerCompleted() {

    }

    public interface OnButtonListener {
        public void onBackBtnPressed (@State int state);
        public void onPlayOrPauseBtnPressed (@State int state);
        public void onFullScreenPressed (@State int state);
    }
}

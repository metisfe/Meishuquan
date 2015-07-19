package com.metis.playerlib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

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

    private PlayCallback mPlayCallback = null;

    private VideoFragment.OnFullScreenListener mFullScreenListener = null;

    private VideoFragment.OnPositionListener mPositionListener = null;

    private ImageView mBackIv, mStartIv, mFullScreenIv;
    private TextView mPositionTv, mDurationTv;
    private SeekBar mSeekBar = null;

    private boolean isSeeking = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_wrapper, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVideoFragment = (VideoFragment)getChildFragmentManager().findFragmentById(R.id.video_wrapper_fragment);

        mBackIv = (ImageView)view.findViewById(R.id.player_back_btn);
        mStartIv = (ImageView)view.findViewById(R.id.player_play_btn);
        mFullScreenIv = (ImageView)view.findViewById(R.id.player_full_screen);
        mPositionTv = (TextView)view.findViewById(R.id.player_current);
        mDurationTv = (TextView)view.findViewById(R.id.player_duration);
        mSeekBar = (SeekBar)view.findViewById(R.id.player_seek_bar);

        mBackIv.setOnClickListener(this);
        mStartIv.setOnClickListener(this);
        mFullScreenIv.setOnClickListener(this);

        setSource(mSource);

        mSeekBar.setOnSeekBarChangeListener(this);
        mVideoFragment.setPlayCallback(this);
        mVideoFragment.setOnFullScreenListener(this);
        mVideoFragment.setOnPositionListener(this);
    }

    public void setSource (String source) {
        mSource = source;
        if (mVideoFragment != null) {
            mVideoFragment.setSource(source);
        }
    }

    public void setPlayCallback (PlayCallback callback) {
        mPlayCallback = callback;

    }

    public void setOnFullScreenListener (VideoFragment.OnFullScreenListener listener) {
        mFullScreenListener = listener;

    }

    public void setOnPositionListener (VideoFragment.OnPositionListener listener) {
        mPositionListener = listener;
    }

    @Override
    public void onFullScreen(boolean isFullScreen) {
        mFullScreenIv.setSelected(isFullScreen);
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
        if (mPlayCallback != null) {
            mPlayCallback.onCompleted(bVideoView);
        }
    }

    @Override
    public void onError(BVideoView bVideoView) {
        mStartIv.setSelected(false);
        if (mPlayCallback != null) {
            mPlayCallback.onError(bVideoView);
        }
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
            if (mVideoFragment.isPlaying()) {
                Log.v(TAG, "pause Btn cause pausePlay");
                mVideoFragment.pausePlay();
            } else {
                mVideoFragment.startPlay();
            }
        } else if (id == mFullScreenIv.getId()) {
            mVideoFragment.setFullScreen(!mVideoFragment.isFullScreen());
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

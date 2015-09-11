package com.metis.newslib.adapter.holder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.cyberplayer.core.BVideoView;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.newslib.R;
import com.metis.newslib.adapter.delegate.NewsDetailsVdoDelegate;
import com.metis.newslib.module.NewsDetails;
import com.metis.playerlib.TimerHelper;

/**
 * Created by Beak on 2015/9/8.
 */
public class NewsDetailsVdoHolder extends AbsViewHolder<NewsDetailsVdoDelegate> implements
        BVideoView.OnCompletionListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    private static final String TAG = NewsDetailsVdoHolder.class.getSimpleName();

    //public VideoWrapperFragment wrapperFragment = null;
    private BVideoView mVideoView = null;
    private View mControllerView = null;
    private ImageView mPlayPauseIv = null;
    private SeekBar mSeekBar = null;
    private TextView mCurrentTv, mTotalTv;

    private NewsDetailsVdoDelegate mDelegate = null;

    private boolean isPlaying = false;

    private boolean isPaused = false;

    private int mUpdateTimes = 0;
    private boolean isSeeking = false;
    private Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying && !isPaused) {
                if (mVideoView.getDuration() > 0 && !isSeeking) {
                    mSeekBar.setMax(mVideoView.getDuration());
                    mSeekBar.setProgress(mVideoView.getCurrentPosition());
                }
                mCurrentTv.setText(TimerHelper.formatTime(mVideoView.getCurrentPosition()));
                mTotalTv.setText(TimerHelper.formatTime(mVideoView.getDuration()));
                if (mUpdateTimes >= 3 && isControlsShowing) {
                    hideControls();
                }
                mUpdateTimes = (mUpdateTimes + 1) % 4;
                mVideoView.postDelayed(this, 1000);
            }
        }
    };

    private boolean isControlsShowing = true;

    public NewsDetailsVdoHolder(View itemView) {
        super(itemView);
        mVideoView = (BVideoView)itemView.findViewById(R.id.news_details_video_view);
        mControllerView = itemView.findViewById(R.id.news_details_controller);
        mPlayPauseIv = (ImageView)itemView.findViewById(R.id.news_details_play_or_pause);
        mSeekBar = (SeekBar)itemView.findViewById(R.id.news_details_progress);
        mCurrentTv = (TextView)itemView.findViewById(R.id.news_details_current);
        mTotalTv = (TextView)itemView.findViewById(R.id.news_details_total);
        /*Context context = itemView.getContext();
        if (context instanceof BaseActivity) {
            wrapperFragment = (VideoWrapperFragment)((BaseActivity) context).getSupportFragmentManager().findFragmentById(R.id.news_details_video_fragment);
            wrapperFragment.setFullScreenVisibility(View.GONE);
            wrapperFragment.setBackVisibility(View.GONE);
        }*/
    }

    @Override
    public void bindData(Context context, NewsDetailsVdoDelegate newsDetailsVdoDelegate, RecyclerView.Adapter adapter, int position) {
        mDelegate = newsDetailsVdoDelegate;
        final NewsDetails.Item item = newsDetailsVdoDelegate.getSource();
        mVideoView.setAKSK("lxKtIjyHewUpBcUawwolofKr", "9fUZnbBO9mijcnriif6aFrElAaponPEg");
        Uri uri = Uri.parse(item.data.URL);
        mVideoView.setVideoPath(uri.toString());
        mVideoView.showCacheInfo(true);
        mVideoView.seekTo(newsDetailsVdoDelegate.getCurrent());
        mVideoView.setRetainLastFrame(true);
        mVideoView.setCacheBufferSize(1024 * 1024);
        mVideoView.setOnCompletionListener(this);


        mVideoView.setOnClickListener(this);
        mControllerView.setOnClickListener(this);
        mPlayPauseIv.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(this);

        //wrapperFragment.setSource(item.data.URL);

    }

    private void startPlayback () {
        mVideoView.start();
        isPlaying = true;
        isPaused = false;
        mUpdateTimes = 0;
        mVideoView.removeCallbacks(mUpdateRunnable);
        mVideoView.post(mUpdateRunnable);
        mPlayPauseIv.setImageResource(R.drawable.ic_pause_center);
        mVideoView.setKeepScreenOn(true);
    }

    private void pausePlayback () {
        mVideoView.pause();
        isPaused = true;
        mPlayPauseIv.setImageResource(R.drawable.ic_play_center);
    }

    private void resumePlayback () {
        mVideoView.resume();
        isPaused = false;
        mPlayPauseIv.setImageResource(R.drawable.ic_pause_center);
        mVideoView.removeCallbacks(mUpdateRunnable);
        mVideoView.post(mUpdateRunnable);
    }

    @Override
    public void onViewDetachedFromWindow() {
        super.onViewDetachedFromWindow();
        mDelegate.setCurrent(mVideoView.getCurrentPosition());
        stopPlayback();
    }

    private void stopPlayback () {
        mVideoView.post(new Runnable() {
            @Override
            public void run() {
                mVideoView.stopPlayback();
                mVideoView.setKeepScreenOn(false);
                mPlayPauseIv.setImageResource(R.drawable.ic_play_center);
            }
        });
        isPlaying = false;
    }

    @Override
    public void onCompletion() {
        stopPlayback();
    }

    private void hideControls () {
        mPlayPauseIv.setVisibility(View.GONE);
        isControlsShowing = false;
    }

    private void showControls () {
        mPlayPauseIv.setVisibility(View.VISIBLE);
        isControlsShowing = true;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == mPlayPauseIv.getId()) {
            Log.v(TAG, "onClick isPlaying=" + isPlaying + " " + isPaused);
            if (isPlaying) {
                if (isPaused) {
                    resumePlayback();
                } else {
                    pausePlayback();
                }
            } else {
                startPlayback();
            }
        } else if (id == mVideoView.getId() || id == mControllerView.getId()) {
            if (isControlsShowing) {
                hideControls();
            } else {
                showControls();
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isSeeking = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (isPlaying) {
            isSeeking = false;
            mVideoView.seekTo(seekBar.getProgress());
        } else {
            seekBar.setProgress(0);
        }
    }
}

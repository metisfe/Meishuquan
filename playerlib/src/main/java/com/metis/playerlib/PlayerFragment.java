package com.metis.playerlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.utils.VersionManager;

/**
 * Created by gaoyunfei on 15/7/11.
 */
public class PlayerFragment extends Fragment
        implements BVideoView.OnPreparedListener,BVideoView.OnCompletionListener,
        BVideoView.OnErrorListener, BVideoView.OnInfoListener, BVideoView.OnPlayingBufferCacheListener,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener,
        AudioManager.OnAudioFocusChangeListener {

    private static final String TAG = PlayerFragment.class.getSimpleName();

    private static final int FULL_SCREEN_UI_OPTIONS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_FULLSCREEN : View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    private View mDecorView = null;

    private RelativeLayout mCtrlContainer = null;
    private BVideoView mBvv = null;
    private ImageView mBackBtn = null;
    private TextView mTitleTv = null;
    private CheckBox mPlayPauseBox = null;
    private TextView mCurrentTv = null;
    private SeekBar mSeekbar = null;
    private TextView mDurationTv = null;
    private CheckBox mFullScreenBox = null;

    private RelativeLayout mTopBar, mBottomBar;

    private String mTitle = null;
    private String mDataSource = null;

    private boolean isStarted = false;
    private boolean isSeekBarDragging = false;
    private boolean isControllerShowing = true;

    private boolean isFragmentAlive = false;

    private AudioManager mAudioManager = null;

    private Runnable mSystemUiRunnable = new Runnable() {
        @Override
        public void run() {
            if (isFullScreen()) {
                hideSystemUi();
            }
        }
    };

    private Runnable mProgressUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            int duration = mBvv.getDuration();
            //Log.v(TAG, "duration = " + duration);
            //duration = mBvv.getDuration(BVideoView.DECODE_SW);
            int current = mBvv.getCurrentPosition();
            if (duration > 0 && !isSeekBarDragging) {
                int progress = current * 100 / duration;
                mSeekbar.setProgress(progress);
                mCurrentTv.setText(TimerHelper.formatTime(current));
            }
            mDurationTv.setText(TimerHelper.formatTime(duration));
            mBvv.postDelayed(this, 1000);
        }
    };

    private Runnable mControllerUiRunnable = new Runnable() {
        @Override
        public void run() {
            if (isControllerShowing && !isSeekBarDragging) {
                hideController();
            }
        }
    };

    private BroadcastReceiver mNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pausePlay();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFragmentAlive = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String cpuVersion = VersionManager.getInstance().getCurrentVersion();
        Log.v(TAG, "cpuVersion=" + cpuVersion);
        return inflater.inflate(R.layout.fragment_player, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCtrlContainer = (RelativeLayout)view.findViewById(R.id.player_controller);
        mBvv = (BVideoView)view.findViewById(R.id.player_view);
        mTitleTv = (TextView)view.findViewById(R.id.player_sub_title);
        mBackBtn = (ImageView)view.findViewById(R.id.player_back_btn);
        mCurrentTv = (TextView)view.findViewById(R.id.player_current);
        mSeekbar = (SeekBar)view.findViewById(R.id.player_seek_bar);
        mDurationTv = (TextView)view.findViewById(R.id.player_duration);
        mPlayPauseBox = (CheckBox)view.findViewById(R.id.player_play_btn);
        mFullScreenBox = (CheckBox)view.findViewById(R.id.player_full_screen);

        mTopBar = (RelativeLayout)view.findViewById(R.id.player_controller_top_bar);
        mBottomBar = (RelativeLayout)view.findViewById(R.id.player_controller_bottom_bar);

        mBvv.setAKSK("lxKtIjyHewUpBcUawwolofKr", "9fUZnbBO9mijcnriif6aFrElAaponPEg");
        //mBvv.setAKSK("lxKtIjyHewUpBcUa", "9fUZnbBO9mijcnri");

        mBvv.setOnPreparedListener(this);
        mBvv.setOnCompletionListener(this);
        mBvv.setOnErrorListener(this);
        mBvv.setOnInfoListener(this);
        mBvv.setOnPlayingBufferCacheListener(this);

        mBvv.setDecodeMode(BVideoView.DECODE_SW);
        mBvv.setCacheBufferSize(1 * 1024 * 1024);

        mCtrlContainer.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mPlayPauseBox.setOnCheckedChangeListener(this);
        mFullScreenBox.setOnCheckedChangeListener(this);
        mSeekbar.setOnSeekBarChangeListener(this);

        mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        setTitle(mTitle);

        mDecorView = getActivity().getWindow().getDecorView();
        mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (isFullScreen() && visibility == 0) {
                    mDecorView.removeCallbacks(mSystemUiRunnable);
                    mDecorView.postDelayed(mSystemUiRunnable, 5000);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mNoisyReceiver);
        pausePlay();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mNoisyReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));
        if (isStarted) {
            resumePlay();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlay();
        isFragmentAlive = false;
    }

    public void setDataSource (String uri) {
        mDataSource = uri;
    }

    public void setTitle (String title) {
        mTitle = title;
        if (mTitleTv != null) {
            mTitleTv.setText(mTitle);
        }
    }

    public void startPlay () {
        if (mDataSource == null) {
            return;
        }
        //String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Movies" + File.separator + "TR.mp4";
        /*File file = new File (path);
        Log.v(TAG, "file is exist = " + file.exists());*/
        mBvv.setVideoPath(Uri.parse(mDataSource).toString());
        mBvv.showCacheInfo(true);

        mBvv.start();
        mBvv.post(mProgressUpdateRunnable);
        mBvv.setKeepScreenOn(true);
        mDurationTv.setText(TimerHelper.formatTime(mBvv.getDuration()));
        mSeekbar.setMax(100);
        requestAudioFocus();
        isStarted = true;
    }

    public void pausePlay () {
        mBvv.pause();
        mBvv.removeCallbacks(mProgressUpdateRunnable);
        mBvv.setKeepScreenOn(false);
        if (mPlayPauseBox.isChecked()) {
            mPlayPauseBox.setChecked(false);
        }
        releaseAudioFocus();
    }

    public void resumePlay () {
        mBvv.resume();
        mBvv.post(mProgressUpdateRunnable);
        mBvv.setKeepScreenOn(true);
        if (!mPlayPauseBox.isChecked()) {
            mPlayPauseBox.setChecked(true);
        }
        requestAudioFocus();
    }

    private OnStopCompleteListener mStopListener = null;
    public void stopPlay (OnStopCompleteListener listener) {
        mStopListener = listener;
        mBvv.stopPlayback();
        mBvv.setKeepScreenOn(false);
        if (mPlayPauseBox.isChecked()) {
            mPlayPauseBox.setChecked(false);
        }
        releaseAudioFocus();
    }

    public void stopPlay () {
        stopPlay(null);
    }

    private void requestAudioFocus () {
        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
    }

    private void releaseAudioFocus () {
        mAudioManager.abandonAudioFocus(this);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            pausePlay();
        }
    }

    public boolean isStarted () {
        return isStarted;
    }

    @Override
    public void onCompletion() {
        isStarted = false;
        mBvv.post(new Runnable() {
            @Override
            public void run() {
                setFullScreen(false);
                mBvv.setKeepScreenOn(false);
                if (mPlayPauseBox.isChecked()) {
                    mPlayPauseBox.setChecked(false);
                }
                releaseAudioFocus();
                if (mStopListener != null) {
                    mStopListener.onStopped();
                    mStopListener = null;
                }
                Toast.makeText(getActivity(), "onCompletion", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onError(int i, int i1) {
        return false;
    }

    @Override
    public boolean onInfo(int i, int i1) {
        return false;
    }

    @Override
    public void onPlayingBufferCache(int i) {

    }

    @Override
    public void onPrepared() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            Log.v(TAG, TAG + " onConfigurationChanged SCREEN_ORIENTATION_LANDSCAPE");
            hideController();
            //mFullScreenBox.setChecked(true);
        } else {
            showController();
            //mFullScreenBox.setChecked(false);
        }
        Log.v(TAG, TAG + " onConfigurationChanged " + newConfig.orientation);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        final int id = buttonView.getId();
        if (id == R.id.player_full_screen) {
            getActivity().setRequestedOrientation(isChecked ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (isChecked) {
                hideSystemUi();
            } else {
                showSystemUi();
            }
        } else if (id == R.id.player_play_btn) {
            if (isChecked) {
                if (isStarted) {
                    resumePlay();
                } else {
                    startPlay();
                }
            } else {
                //isStarted
                if (mBvv.isPlaying()) {
                    pausePlay();
                }
            }
        }

    }

    private void hideSystemUi () {
        mDecorView.setSystemUiVisibility(FULL_SCREEN_UI_OPTIONS);
        mDecorView.removeCallbacks(mSystemUiRunnable);
    }

    private void showSystemUi () {
        mDecorView.setSystemUiVisibility(0);
    }

    private void hideController () {
        if (!isFragmentAlive) {
            return;
        }
        if (isControllerShowing) {
            Animation topAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.hide_top);
            Animation bottomAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.hide_bottom);
            mTopBar.startAnimation(topAnim);
            mBottomBar.startAnimation(bottomAnim);
            isControllerShowing = false;
        }
        mCtrlContainer.removeCallbacks(mControllerUiRunnable);
    }

    private void showController () {
        if (!isFragmentAlive) {
            return;
        }
        if (!isControllerShowing) {
            Animation topAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.show_top);
            Animation bottomAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.show_bottom);
            mTopBar.startAnimation(topAnim);
            mBottomBar.startAnimation(bottomAnim);
            isControllerShowing = true;

        }
        mCtrlContainer.removeCallbacks(mControllerUiRunnable);
        mCtrlContainer.postDelayed(mControllerUiRunnable, 5000);
    }

    public void setFullScreen (boolean fullScreen) {
        mFullScreenBox.setChecked(fullScreen);
    }

    public boolean isFullScreen () {
        return mFullScreenBox.isChecked();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.player_back_btn) {
            if (isFullScreen()) {
                setFullScreen(false);
                return;
            }
            getActivity().finish();
        } else if (id == R.id.player_controller) {
            if (isControllerShowing) {
                hideController();
            } else {
                showController();
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mCurrentTv.setText(TimerHelper.formatTime(mBvv.getDuration() * seekBar.getProgress() / 100));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isSeekBarDragging = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isSeekBarDragging = false;
        mBvv.seekTo(mBvv.getDuration() * seekBar.getProgress() / 100);
        showController();
    }

    public static interface OnStopCompleteListener {
        public void onStopped ();
    }

}

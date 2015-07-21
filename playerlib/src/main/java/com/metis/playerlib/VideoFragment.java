package com.metis.playerlib;

import android.app.Activity;
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

import com.baidu.cyberplayer.core.BVideoView;

/**
 * Created by gaoyunfei on 15/7/17.
 */
public class VideoFragment extends Fragment implements
        BVideoView.OnCompletionListener,
        BVideoView.OnErrorListener,
        BVideoView.OnPlayingBufferCacheListener{

    private static final String TAG = VideoFragment.class.getSimpleName();

    private static final int FULL_SCREEN_UI_OPTIONS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_FULLSCREEN : View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    private View mDecorView = null;

    public static final long BUFFER_SIZE_DEFAULT = 1024 * 1024 * 2;

    private static final int
            MODE_FULL_SCREEN = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
            MODE_NORMAL = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    public final class State {
        public static final int IDLE =0 , PLAYING = 1, PAUSED = 2, ERROR = 3;
    }

    private int mState = State.IDLE;

    private PlayCallback mPlayCallback = null;

    private OnStopCallback mStopCallback = null;

    private OnFullScreenListener mFullScreenListener = null;

    private OnPositionListener mPositionListener = null;

    private long mBufferSize = BUFFER_SIZE_DEFAULT;

    private BVideoView mBvv = null;

    private String mSource = null;

    private boolean isFullScreen = false;

    private int mPausePosition = -1;

    private AudioManager mAudioManager = null;

    private BroadcastReceiver mNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "mNoisyReceiver cause pausePlay");
            pausePlay();
        }
    };

    private Runnable mPlayingUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            final int position = mBvv.getCurrentPosition();
            final int total = mBvv.getDuration();
            if (mPositionListener != null) {
                mPositionListener.onUpdate(position, total);
            }
            if (mState == State.PLAYING) {
                mBvv.postDelayed(this, 1000);
            }
        }
    };

    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.v(TAG, "onAudioFocusChange focusChange=" + focusChange);
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                Log.v(TAG, "focusChange cause pausePlay");
                pausePlay();
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAudioManager = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
        mDecorView = activity.getWindow().getDecorView();
        mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    mDecorView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideSystemUi();
                        }
                    }, 5000);
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(mNoisyReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBvv = (BVideoView)view.findViewById(R.id.video_view);

        initBVideoView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPausePosition > 0) {
            startPlay();
        }
        /*Log.v(TAG, "onResume " + mState);
        if (mState == State.PAUSED) {
            startPlay();
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mState == State.PLAYING) {
            Log.v(TAG, "onPause cause pausePlay");
            mPausePosition = pausePlay();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mNoisyReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initBVideoView () {
        mBvv.setAKSK("lxKtIjyHewUpBcUawwolofKr", "9fUZnbBO9mijcnriif6aFrElAaponPEg");
        mBvv.setOnCompletionListener(this);
        mBvv.setOnErrorListener(this);
        mBvv.setOnPlayingBufferCacheListener(this);
    }

    /**
     *
     * @param source url or local file path
     */
    public void setSource (String source) {
        mSource = source;
    }

    public String getSource () {
        return mSource;
    }

    /**
     *
     * @param bufferSize cache buffer size in byte, default is {@link #BUFFER_SIZE_DEFAULT}
     */
    public void setBufferSize (long bufferSize) {
        mBufferSize = bufferSize;
    }

//    private void startPlay (String source, boolean userDoIt) {
//        setSource(source);
//        if (source == null) {
//            throw new IllegalArgumentException("source shouldn't be null");
//        }
//        mBvv.setVideoPath(Uri.parse(mSource).toString());
//        mBvv.setCacheBufferSize(mBufferSize);
//        mBvv.start();
//
//        if (mPlayCallback != null) {
//            mPlayCallback.onStarted(mBvv, userDoIt);
//        }
//    }

    /**
     *
     * @param source source url or local file path, can't not be null
     */
    public void startPlay (String source) {
        if (source == null) {
            throw new IllegalArgumentException("source shouldn't be null");
        }
        if (source.equals(mSource)) {
            if (mState == State.PAUSED) {
                resumePlay();
                return;
            }
        } else {
            setSource(source);
            stopPlay(new OnStopCallback() {
                @Override
                public void onStopped() {
                    doStart();
                }
            });
        }
        doStart();

    }

    private void doStart () {
        mBvv.setVideoPath(Uri.parse(mSource).toString());
        mBvv.setCacheBufferSize(mBufferSize);
        mBvv.showCacheInfo(true);
        if (mPausePosition > 0) {
            mBvv.seekTo(mPausePosition);
        }
        mBvv.start();
        setState(State.PLAYING);
        if (mPlayCallback != null) {
            mPlayCallback.onStarted(mBvv);
        }
        mBvv.post(mPlayingUpdateRunnable);
        setKeepScreenOn(true);
        askAudioFocus();
    }

    public void startPlay () {
        if (mSource == null) {
            throw new IllegalArgumentException("you should set a non-null data source with setSource before calling startPlay()");
        }
        startPlay(mSource);
    }

    public int pausePlay () {
        if (!isPlaying()) {
            return -1;
        }

        mBvv.pause();
        setState(State.PAUSED);
        if (mPlayCallback != null) {
            mPlayCallback.onPaused(mBvv);
        }
        setKeepScreenOn(false);
        releaseAudioFocus();
        return mBvv.getCurrentPosition();
    }

    public void resumePlay () {
        if (isPlaying()) {
            return;
        }
        mBvv.resume();
        setState(State.PLAYING);
        if (mPlayCallback != null) {
            mPlayCallback.onResumed(mBvv);
        }
        setKeepScreenOn(true);
        askAudioFocus();
        mBvv.post(mPlayingUpdateRunnable);
    }

    public void stopPlay (OnStopCallback callback) {
        mStopCallback = callback;
        mBvv.stopPlayback();
        setKeepScreenOn(false);
        releaseAudioFocus();
    }

    public void stopPlay() {
        stopPlay(null);
    }

    public void seekTo (int position) {
        mBvv.seekTo(position);
    }

    public void setFullScreen (boolean fullScreen) {
        isFullScreen = fullScreen;
        getActivity().setRequestedOrientation(fullScreen ? MODE_FULL_SCREEN : MODE_NORMAL);
        if (fullScreen) {
            hideSystemUi();
        } else {
            showSystemUi();
        }
    }

    private void showSystemUi () {
        mDecorView.setSystemUiVisibility (0);
    }

    private void hideSystemUi () {
        mDecorView.setSystemUiVisibility (FULL_SCREEN_UI_OPTIONS);
    }

    public boolean isFullScreen () {
        return isFullScreen;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mFullScreenListener != null) {
            mFullScreenListener.onFullScreen(isFullScreen);
        }
        Log.v(TAG, "onConfigurationChanged " + newConfig.orientation);
    }

    @Override
    public void onCompletion() {

        mBvv.post(new Runnable() {
            @Override
            public void run() {
                reset();
                if (mPlayCallback != null) {
                    mPlayCallback.onCompleted(mBvv);
                }
                if (mStopCallback != null) {
                    mStopCallback.onStopped();
                    mStopCallback = null;
                }
            }
        });
    }

    private void reset() {
        setState(State.IDLE);
        setKeepScreenOn(false);
        releaseAudioFocus();
        //mPausePosition = 0;
    }

    private void setKeepScreenOn (boolean screenOn) {
        mBvv.setKeepScreenOn(screenOn);
    }

    private void askAudioFocus () {
        mAudioManager.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
    }

    private void releaseAudioFocus () {
        mAudioManager.abandonAudioFocus(mAudioFocusListener);
    }

    @Override
    public boolean onError(int i, int i1) {
        if (mPlayCallback != null) {
            return mPlayCallback.onError(mBvv, i, i1);
        }
        return false;
    }

    @Override
    public void onPlayingBufferCache(int i) {
        Log.v(TAG, "onPlayingBufferCache " + i);
    }

    /**
     *
     * @return true if playing
     */
    public boolean isPlaying () {
        return mBvv != null && mBvv.isPlaying();
    }

    /**
     * @return the current, {@link com.metis.playerlib.VideoFragment.State}
     */
    public int getState () {
        return mState;
    }

    private void setState (int state) {
        mState = state;
    }

    public void setPlayCallback (PlayCallback playCallback) {
        mPlayCallback = playCallback;
    }

    public void setOnFullScreenListener (OnFullScreenListener listener) {
        mFullScreenListener = listener;
    }

    public void setOnPositionListener (OnPositionListener listener) {
        mPositionListener = listener;
    }

    public static interface OnStopCallback {
        public void onStopped ();
    }

    public static interface OnFullScreenListener {
        /**
         * @param isFullScreen true if fullScreen happened,
         *                     you need hide ui elements except this fragment
         */
        public void onFullScreen (boolean isFullScreen);
    }

    public static interface OnPositionListener {
        public void onUpdate (int current, int total);
    }

}

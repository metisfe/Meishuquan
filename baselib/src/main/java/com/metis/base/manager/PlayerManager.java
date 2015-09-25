package com.metis.base.manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;
import android.view.Surface;

import com.bokecc.sdk.mobile.drm.DRMServer;
import com.bokecc.sdk.mobile.play.DWMediaPlayer;
import com.bokecc.sdk.mobile.util.HttpUtil;
import com.metis.base.fragment.PlayerProperty;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Beak on 2015/9/24.
 */
public class PlayerManager extends AbsManager implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener{

    private static final String TAG = PlayerManager.class.getSimpleName();

    private static PlayerManager sManager = null;

    public static synchronized PlayerManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new PlayerManager(context.getApplicationContext());
        }
        return sManager;
    }

    private boolean isSufaceViewCreated = false, isStarted = false;

    private PlayerProperty mProperty = null;

    private DWMediaPlayer mPlayer = null;

    private DRMServer mDrmServer = null;

    private Surface mSurface = null;

    private Context mAppContext = null;

    private PowerManager mPowerManager = null;
    private PowerManager.WakeLock mWakeLock = null;

    private AudioManager mAudioManager = null;

    private PlayerCallback mPlayerCallback = null;

    private AudioManager.OnAudioFocusChangeListener mAudioListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                pausePlay();
            }
        }
    };

    private PlayerManager(Context context) {
        super(context);
        mAppContext = context.getApplicationContext();
        mProperty = PlayerProperty.getInstance(context);

        mPowerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
        mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void setSurface (Surface surface) {
        mSurface = surface;
    }

    public void startRemotePlay (String videoId) {
        ensurePlayer();
        //TODO
        Log.v(TAG, TAG + " user_id=" + mProperty.getUser_id() + " api_key=" + mProperty.getApi_key());
        mPlayer.setVideoPlayInfo(videoId, mProperty.getUser_id(), mProperty.getApi_key(), mAppContext);
        Map<String, Integer> definitions = mPlayer.getDefinitions();
        if (definitions != null) {
            mPlayer.setDefaultDefinition(definitions.get("definitionInfo"));
        } else {
            mPlayer.setDefaultDefinition(DWMediaPlayer.NORMAL_DEFINITION);
        }

        mPlayer.prepareAsync();
        if (mPlayerCallback != null) {
            mPlayerCallback.onPlayerStarted();
        }
    }

    public void startLocalPlay (String path) {
        ensurePlayer();
        try {
            Log.v(TAG, TAG + " startLocalPlay path=" + path);
            //mPlayer.setDataSource(path);
            mPlayer.setDRMVideoPath(path, mAppContext);
            mPlayer.prepareAsync();
            if (mPlayerCallback != null) {
                mPlayerCallback.onPlayerStarted();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensurePlayer() {
        HttpUtil.LOG_LEVEL = HttpUtil.HttpLogLevel.DETAIL;
        if (mSurface == null) {
            throw new IllegalStateException("no surface was set to MediaPlayer, call setSurface before startPlay");
        }
        startServerIfNeed();
        if (mPlayer == null) {
            mPlayer = new DWMediaPlayer();
            mPlayer.setDRMServerPort(mDrmServer.getPort());
            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setSurface(mSurface);
            requestAudioFocus();
        }

    }

    public void stopPlay () {
        if (mPlayer == null) {
            return;
        }
        mPlayer.stop();
        isStarted = false;
        if (mPlayerCallback != null) {
            mPlayerCallback.onPlayerStopped();
        }
    }

    public void pausePlay () {
        if (mPlayer != null && isStarted && mPlayer.isPlaying()) {
            mPlayer.pause();
            keepScreenOn(false);
            releaseAudioFocus();
            if (mPlayerCallback != null) {
                mPlayerCallback.onPlayerPaused();
            }
        }
    }

    public void resumePlay () {
        if (mPlayer != null && isStarted && !mPlayer.isPlaying()) {
            mPlayer.setSurface(mSurface);
            mPlayer.start();
            keepScreenOn(true);
            requestAudioFocus();
            if (mPlayerCallback != null) {
                mPlayerCallback.onPlayerResumed();
            }
        }
    }

    public void release () {
        if (mPlayer != null) {
            stopServerIfNeed();
            mPlayer.release();
            mPlayer = null;
            mSurface = null;
            releaseAudioFocus();
        }
    }

    private void startServerIfNeed () {
        if (mDrmServer == null) {
            mDrmServer = new DRMServer();
            mDrmServer.start();
        }
    }

    private void stopServerIfNeed () {
        if (mDrmServer != null) {
            mDrmServer.stop();
            mDrmServer = null;

        }
    }

    private void keepScreenOn (boolean screenOn) {
        if (screenOn) {
            mWakeLock.acquire();
        } else {
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
            }
        }
    }

    private void requestAudioFocus() {
        mAudioManager.requestAudioFocus(mAudioListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    private void releaseAudioFocus () {
        mAudioManager.abandonAudioFocus(mAudioListener);
    }

    public boolean isStarted () {
        return isStarted;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.v(TAG, TAG + " onCompletion");
        keepScreenOn(false);
        if (mPlayerCallback != null) {
            mPlayerCallback.onPlayerCompleted();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public int[] getSize () {
        if (mPlayer != null && isStarted) {
            int[] array = {mPlayer.getVideoWidth(), mPlayer.getVideoHeight()};
            return array;
        }
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer.start();
        isStarted = true;
        int height = mPlayer.getVideoHeight();
        int width = mPlayer.getVideoWidth();
        Log.v(TAG, TAG + " height=" + height + " width=" + width);
        keepScreenOn(true);

        if (mPlayerCallback != null) {
            mPlayerCallback.onPlayerPrepared(width, height);
        }
    }

    public void setPlayerCallback (PlayerCallback callback) {
        mPlayerCallback = callback;
    }

    public interface PlayerCallback {
        public void onPlayerStarted ();
        public void onPlayerPrepared (int width, int height);
        public void onPlayerPaused ();
        public void onPlayerResumed ();
        public void onPlayerStopped ();
        public void onPlayerCompleted ();
    }
}

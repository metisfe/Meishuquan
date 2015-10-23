package com.metis.base.manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.util.Log;
import android.view.Surface;

import com.bokecc.sdk.mobile.drm.DRMServer;
import com.bokecc.sdk.mobile.play.DWMediaPlayer;
import com.bokecc.sdk.mobile.util.HttpUtil;
import com.metis.base.fragment.PlayerProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    //private PlayerCallback mPlayerCallback = null;

    private List<PlayerCallback> mCallbackList = new ArrayList<PlayerCallback>();

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
        doMuchPlayerStarted();
    }

    public void startRemotePlayWithUrl (String url) {
        ensurePlayer();
        try {
            Log.v(TAG, TAG + " startRemotePlayWithUrl path=" + url);
            //mPlayer.setDataSource(path);
            mPlayer.setDataSource(getContext(), Uri.parse(url));
            mPlayer.prepareAsync();
            doMuchPlayerStarted();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            stopPlay();
            release();
            startRemotePlayWithUrl(url);
        }
    }

    public void startLocalPlay (String path) {
        ensurePlayer();
        try {
            Log.v(TAG, TAG + " startLocalPlay path=" + path);
            //mPlayer.setDataSource(path);
            mPlayer.setDRMVideoPath(path, mAppContext);
            mPlayer.prepareAsync();
            doMuchPlayerStarted();
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
        doMuchPlayerStopped();
    }

    public void pausePlay () {
        if (mPlayer != null && isStarted && mPlayer.isPlaying()) {
            mPlayer.pause();
            keepScreenOn(false);
            releaseAudioFocus();
            doMuchPlayerPaused();
        }
    }

    public void resumePlay () {
        if (mPlayer != null && isStarted && !mPlayer.isPlaying()) {
            mPlayer.setSurface(mSurface);
            mPlayer.start();
            keepScreenOn(true);
            requestAudioFocus();
            doMuchPlayerResumed();
        }
    }

    public void release () {
        if (mPlayer != null) {
            stopServerIfNeed();
            mPlayer.release();
            mPlayer = null;
            mSurface = null;
            releaseAudioFocus();
            mCallbackList.clear();
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

    public boolean isPlaying () {
        return mPlayer != null && mPlayer.isPlaying();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.v(TAG, TAG + " onCompletion");
        keepScreenOn(false);
        doMuchPlayerCompleted();
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

        doMuchPlayerPrepared(width, height);
    }

    /*public void setPlayerCallback (PlayerCallback callback) {
        mPlayerCallback = callback;
    }*/

    public void registerPlayerCallback (PlayerCallback callback) {
        if (mCallbackList.contains(callback)) {
            return;
        }
        mCallbackList.add(callback);
    }

    public void unregisterPlayerCallback (PlayerCallback callback) {
        if (!mCallbackList.contains(callback)) {
            return;
        }
        mCallbackList.remove(callback);
    }

    private void doMuchPlayerPrepared (int width, int height) {
        final int length = mCallbackList.size();
        for (int i = 0; i < length; i++) {
            PlayerCallback callback = mCallbackList.get(i);
            callback.onPlayerPrepared(width, height);
        }
    }

    private void doMuchPlayerStarted () {
        final int length = mCallbackList.size();
        for (int i = 0; i < length; i++) {
            PlayerCallback callback = mCallbackList.get(i);
            callback.onPlayerStarted();
        }
    }

    private void doMuchPlayerPaused () {
        final int length = mCallbackList.size();
        for (int i = 0; i < length; i++) {
            PlayerCallback callback = mCallbackList.get(i);
            callback.onPlayerPaused();
        }
    }

    private void doMuchPlayerResumed () {
        final int length = mCallbackList.size();
        for (int i = 0; i < length; i++) {
            PlayerCallback callback = mCallbackList.get(i);
            callback.onPlayerResumed();
        }
    }

    private void doMuchPlayerStopped () {
        final int length = mCallbackList.size();
        for (int i = 0; i < length; i++) {
            PlayerCallback callback = mCallbackList.get(i);
            callback.onPlayerStopped();
        }
    }

    private void doMuchPlayerCompleted () {
        final int length = mCallbackList.size();
        for (int i = 0; i < length; i++) {
            PlayerCallback callback = mCallbackList.get(i);
            callback.onPlayerCompleted();
        }
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

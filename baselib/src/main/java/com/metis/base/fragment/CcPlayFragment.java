package com.metis.base.fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.bokecc.sdk.mobile.drm.DRMServer;
import com.bokecc.sdk.mobile.play.DWMediaPlayer;
import com.bokecc.sdk.mobile.util.HttpUtil;
import com.metis.base.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Beak on 2015/9/24.
 */
public class CcPlayFragment extends Fragment implements
        SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener{

    private static final String TAG = CcPlayFragment.class.getSimpleName();

    private static CcPlayFragment sFragment = null;

    public static CcPlayFragment getInstance () {
        if (sFragment == null) {
            sFragment = new CcPlayFragment();
        }
        return sFragment;
    }

    private boolean isSeeking = false;
    private Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isSeeking && mPlayer != null && mPlayer.isPlaying()) {
                final int currentPosition = mPlayer.getCurrentPosition();
                final int duration = mPlayer.getDuration();
                int progress = currentPosition * 100 / duration;
                doMuchPlayerUpdateProgress(currentPosition, duration, progress);
                mUpdateHandler.postDelayed(this, 1000);
                Log.v(TAG, "update progress " + currentPosition);
            }
        }
    };

    private static Handler mUpdateHandler = new Handler();

    private SurfaceView mCcPlaySurfaceView = null;

    private Surface mSurface = null;

    private Context mAppContext = null;

    private boolean isSufaceViewCreated = false, isStarted = false;

    private OnFullScreenCallback mFullScreenCallback = null;

    private PlayerProperty mProperty = null;

    private DWMediaPlayer mPlayer = null;

    private DRMServer mDrmServer = null;

    private AudioManager mAudioManager = null;

    private List<PlayerCallback> mCallbackList = new ArrayList<PlayerCallback>();

    private int mBufferingPercent = 0;

    private boolean onPausedFirst = false;

    private AudioManager.OnAudioFocusChangeListener mAudioListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                pausePlay();
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppContext = context.getApplicationContext();
        mProperty = PlayerProperty.getInstance(context);
        mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cc_play, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCcPlaySurfaceView = (SurfaceView)view.findViewById(R.id.cc_play_surface_view);
        mCcPlaySurfaceView.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isSufaceViewCreated = true;
        mSurface = holder.getSurface();

        Log.v(TAG, TAG + " surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(TAG, TAG + " surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isSufaceViewCreated = false;
        mSurface = null;
        Log.v(TAG, TAG + " surfaceDestroyed");
    }

    public void setFullScreen(boolean fullScreen) {
        View view = getActivity().getWindow().getDecorView();
        if (fullScreen) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LOW_PROFILE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            view.setSystemUiVisibility(0);
        }
        if (mFullScreenCallback != null) {
            mFullScreenCallback.onFullScreen(fullScreen);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!onPausedFirst) {
            onPausedFirst = true;
            return;
        }
        pausePlay();
    }

    @Override
    public void onResume() {
        super.onResume();
        //resumePlay();
    }

    public boolean isFullScreen () {
        return getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCcPlaySurfaceView.getHolder().removeCallback(this);
        if (isStarted) {
            stopPlay();
            release();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAudioManager = null;
    }

    public void setSeeking (boolean seeking) {
        isSeeking = seeking;
        if (isSeeking) {
            mUpdateHandler.removeCallbacks(mUpdateRunnable);
        } else {
            mUpdateHandler.post(mUpdateRunnable);
        }
    }

    public int getDuration () {
        if (mPlayer == null) {
            return 0;
        }
        return mPlayer.getDuration();
    }

    public void seekTo (int position) {
        if (mPlayer == null || !mPlayer.isPlaying() || position < 0 || position > mPlayer.getDuration()) {
            return;
        }
        mPlayer.seekTo(position);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer.start();
        isStarted = true;
        int height = mPlayer.getVideoHeight();
        int width = mPlayer.getVideoWidth();
        Log.v(TAG, TAG + " height=" + height + " width=" + width);
        keepScreenOn(true);
        mUpdateHandler.post(mUpdateRunnable);

        doMuchPlayerPrepared(width, height);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.v(TAG, TAG + " onCompletion");
        keepScreenOn(false);
        doMuchPlayerCompleted();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (mBufferingPercent != percent) {
            Log.v(TAG, "onBufferingUpdate " + percent);
            doMuchPlayerBuffering(percent);
            mBufferingPercent = percent;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
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
        }
        mPlayer.setDRMServerPort(mDrmServer.getPort());
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnBufferingUpdateListener(this);
        mPlayer.setSurface(mSurface);
        requestAudioFocus();

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
            mUpdateHandler.post(mUpdateRunnable);
        }
    }

    public void release () {
        if (mPlayer != null) {
            stopServerIfNeed();
            mPlayer.release();
            mPlayer = null;
            //mSurface = null;
            releaseAudioFocus();
            //mCallbackList.clear();
        }
    }

    private void keepScreenOn (boolean screenOn) {
        mCcPlaySurfaceView.setKeepScreenOn(screenOn);
        /*if (screenOn) {
            mWakeLock.acquire();
        } else {
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
            }
        }*/
    }

    private void requestAudioFocus() {
        mAudioManager.requestAudioFocus(mAudioListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    private void releaseAudioFocus () {
        mAudioManager.abandonAudioFocus(mAudioListener);
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

    private void doMuchPlayerUpdateProgress (int current, int duration, int progress) {
        final int length = mCallbackList.size();
        for (int i = 0; i < length; i++) {
            PlayerCallback callback = mCallbackList.get(i);
            callback.onUpdateProgress(current, duration, progress);
        }
    }

    private void doMuchPlayerBuffering (int percent) {
        final int length = mCallbackList.size();
        for (int i = 0; i < length; i++) {
            PlayerCallback callback = mCallbackList.get(i);
            callback.onBuffering(percent);
        }
    }

    public void setOnFullScreenCallback (OnFullScreenCallback callback) {
        mFullScreenCallback = callback;
    }

    public interface OnFullScreenCallback {
        public void onFullScreen (boolean fullScreen);
    }

    public interface PlayerCallback {
        public void onPlayerStarted ();
        public void onPlayerPrepared (int width, int height);
        public void onPlayerPaused ();
        public void onPlayerResumed ();
        public void onPlayerStopped ();
        public void onPlayerCompleted ();

        public void onUpdateProgress (int current, int duration, int progress);
        public void onBuffering (int percent);
    }
}

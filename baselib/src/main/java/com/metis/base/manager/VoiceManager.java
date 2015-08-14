package com.metis.base.manager;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Toast;

import com.metis.base.utils.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/8/7.
 */
public class VoiceManager extends AbsManager implements AudioManager.OnAudioFocusChangeListener{

    private static final String TAG = VoiceManager.class.getSimpleName();

    private static VoiceManager sManager = null;

    public static synchronized VoiceManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new VoiceManager(context.getApplicationContext());
        }
        return sManager;
    }

    private AudioManager mAudioManager = null;

    private MediaRecorder mRecorder = null;
    private boolean isRecording = false;
    private OnRecordListener mRecordListener = null;
    private String mRecordPath = null;
    private long mStartTime = 0;

    private MediaPlayer mPlayer = null;
    private boolean isPlaying = false;
    private String mPlayPath = null;
    private OnPlayListener mPlayListener = null;

    private MediaRecorder.OnErrorListener mRecordErrorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Log.v(TAG, "onError what=" + what + " extra=" + extra);
            stopRecord();
        }
    };

    private MediaPlayer.OnPreparedListener mPlayerPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            isPlaying = true;
            mUpdateHandler.postDelayed(mPlayUpdateRunnable, 1000);
            if (mPlayListener != null) {
                mPlayListener.onPlayStart(mp);
            }
            /*final int length = mPlayListenerList.size();
            for (int i = 0; i < length; i++) {
                OnPlayListener listener = mPlayListenerList.get(i);
                if (listener != null) {
                    listener.onPlayStart(mp);
                }
            }*/
        }
    };

    private MediaPlayer.OnCompletionListener mPlayerCompetionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            stopPlay();
        }
    };

    private Runnable mRecordUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (mRecordListener != null && isRecording) {
                mRecordListener.onRecording(mRecordPath, mRecorder, SystemClock.elapsedRealtime() - mStartTime);
            }
            if (isRecording) {
                mUpdateHandler.postDelayed(this, 1000);
            }
        }
    };

    private Runnable mPlayUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPlayListener != null && isPlaying) {
                mPlayListener.onPlaying(mPlayPath, mPlayer, mPlayer.getCurrentPosition());//TODO
            }
            if (isPlaying) {
                mUpdateHandler.postDelayed(this, 1000);
            }
        }
    };

    private Handler mUpdateHandler = new Handler();

    private VoiceManager(Context context) {
        super(context);

        mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        mRecorder = null;
    }

    public void startRecord (String path) {
        mRecorder = new MediaRecorder();
        //mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 14800, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_8BIT, 1024 * 128);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setOutputFile(path);
        mRecordPath = path;
        mRecorder.setOnErrorListener(mRecordErrorListener);
        /*mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {

            }
        });*/

        int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //TODO couldn't get audio focus
            Toast.makeText(getContext(), "startRecord result failed", Toast.LENGTH_SHORT).show();
            return;
        }
        doRecord();
        Log.v(TAG, "startRecord ");
    }

    public void stopRecord () {
        mRecorder.stop();
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;
        mAudioManager.abandonAudioFocus(this);
        isRecording = false;
        mUpdateHandler.removeCallbacks(mRecordUpdateRunnable);
        if (mRecordListener != null) {
            mRecordListener.onRecordStop(mRecordPath, SystemClock.elapsedRealtime() - mStartTime);
        }
        mRecordPath = null;
        mStartTime = 0;
    }

    private void doRecord () {
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            isRecording = false;
        }
        mRecorder.start();
        isRecording = true;
        mStartTime = SystemClock.elapsedRealtime();
        mUpdateHandler.postDelayed(mRecordUpdateRunnable, 1000);
        if (mRecordListener != null) {
            mRecordListener.onRecordStart(mRecordPath);
        }
    }

    public void startPlay (String path) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(path);
            mPlayPath = path;
            mPlayer.setOnPreparedListener(mPlayerPreparedListener);
            mPlayer.setOnCompletionListener(mPlayerCompetionListener);
            int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                //TODO couldn't get audio focus
                Toast.makeText(getContext(), "startPlay result failed", Toast.LENGTH_SHORT).show();
                return;
            }
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPlayingPath () {
        return mPlayPath;
    }

    public void stopPlay () {
        mPlayer.stop();
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        isPlaying = false;
        mPlayPath = null;
        mUpdateHandler.removeCallbacks(mPlayUpdateRunnable);
        if (mPlayListener != null) {
            mPlayListener.onPlayStop();
        }
        /*final int length = mPlayListenerList.size();
        for (int i = 0; i < length; i++) {
            OnPlayListener listener = mPlayListenerList.get(i);
            if (listener != null) {
                listener.onPlayStop();
            }
        }*/
    }

    public boolean isPlaying () {
        return isPlaying;
    }

    public boolean isRecording () {
        return isRecording;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            if (isRecording()) {
                stopRecord();
            }
            if (isPlaying()) {
                stopPlay();
            }
        }
        Log.v(TAG, "focusChange=" + focusChange);
        /*switch (mMode) {
            case MODE_RECORD:
                doRecord();
                break;
            case MODE_PLAY_BACK:
                break;
        }*/
    }

    public void setOnRecordListener (OnRecordListener listener) {
        mRecordListener = listener;
    }

    public static interface OnRecordListener {
        public void onRecordStart (String targetPath);
        public void onRecording (String targetPath, MediaRecorder recorder, long currentDuration);
        public void onRecordStop (String targetPath, long durationInMills);
    }

    public void setOnPlayListener (OnPlayListener listener) {
        mPlayListener = listener;
    }

    /*public void registerOnPlayListener (OnPlayListener listener) {
        if (mPlayListenerList.contains(listener)) {
            return;
        }
        mPlayListenerList.add(listener);
    }

    public void unregisterOnPlayListener (OnPlayListener listener) {
        if (!mPlayListenerList.contains(listener)) {
            return;
        }
        mPlayListenerList.remove(listener);
    }*/

    public static interface OnPlayListener {
        public void onPlayStart (MediaPlayer player);
        public void onPlaying (String targetPath, MediaPlayer mp, long position);
        public void onPlayStop ();
    }
}

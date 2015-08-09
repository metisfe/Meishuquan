package com.metis.base.manager;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.widget.Toast;

import com.metis.base.utils.Log;

import java.io.IOException;

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
        }
    };

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
        if (mRecordListener != null) {
            mRecordListener.onRecordStart(mRecordPath);
        }
    }

    public void startPlay (String path) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(path);
            mPlayer.setOnPreparedListener(mPlayerPreparedListener);

            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isRecording () {
        return isRecording;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
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
        public void onRecording (String targetPath, MediaRecorder recorder);
        public void onRecordStop (String targetPath, long durationInMills);
    }
}

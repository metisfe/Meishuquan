package com.metis.base.manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;

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

    private MediaRecorder.OnErrorListener mRecordErrorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Log.v(TAG, "onError what=" + what + " extra=" + extra);
        }
    };

    private VoiceManager(Context context) {
        super(context);

        mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        mRecorder = null;
    }

    public void startRecord (String path) {

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setOutputFile(path);

        mRecorder.setOnErrorListener(mRecordErrorListener);

        int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //TODO couldn't get audio focus
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
    }

    private void doRecord () {
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder.start();
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
}

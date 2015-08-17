package com.metis.commentpart.fragment;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.base.fragment.BaseFragment;
import com.metis.base.manager.CacheManager;
import com.metis.base.manager.VoiceManager;
import com.metis.base.utils.TimeUtils;
import com.metis.commentpart.R;

import java.io.File;

/**
 * Created by Beak on 2015/8/7.
 */
public class VoiceFragment extends BaseFragment implements View.OnClickListener, VoiceManager.OnRecordListener, VoiceManager.OnPlayListener{

    private ImageView mVoiceBtn = null;
    private TextView mDurationTv, mGiveUpTv, mUseTv;
    private ViewGroup mBtnContainer = null;

    private VoiceManager mVoiceManager = null;

    private String mVoicePath = null;
    private int mDuration = 0;
    private VoiceDispatcher mDispatcher = null;

    private VoiceManager.OnRecordListener mRecordListener = null;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = null;
        if (enter) {
            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_bottom);
        } else {
            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_out_bottom);
        }
        return animation;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voice, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVoiceBtn = (ImageView)view.findViewById(R.id.voice_btn);
        mDurationTv = (TextView)view.findViewById(R.id.voice_duration);
        mGiveUpTv = (TextView)view.findViewById(R.id.voice_give_up);
        mUseTv = (TextView)view.findViewById(R.id.voice_send);
        mBtnContainer = (ViewGroup)view.findViewById(R.id.btn_container);

        mVoiceBtn.setOnClickListener(this);
        mGiveUpTv.setOnClickListener(this);
        mUseTv.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVoiceManager = VoiceManager.getInstance(getActivity());
        mVoiceManager.setOnRecordListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == mVoiceBtn.getId()) {
            if (TextUtils.isEmpty(mVoicePath)) {
                if (mVoiceManager.isRecording()) {
                    mVoiceManager.stopRecord();
                } else {
                    File file = new File (CacheManager.getInstance(getActivity()).getMyVoiceCacheDir().getAbsolutePath() + File.separator + "Audio" + File.separator + System.currentTimeMillis());
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    mVoiceManager.startRecord(file.getAbsolutePath());
                }
            } else {
                if (mVoiceManager.isPlaying()) {
                    mVoiceManager.stopPlay();
                    mVoiceManager.setOnPlayListener(null);
                } else {
                    mVoiceManager.setOnPlayListener(VoiceFragment.this);
                    mVoiceManager.startPlay(mVoicePath);
                }
            }

        } else if (id == mGiveUpTv.getId()) {
            giveUp();
        } else if (id == mUseTv.getId()) {
            use();
        }
    }

    private void giveUp () {
        if (mDispatcher != null) {
            mDispatcher.onGiveUp(mVoicePath);
        }
        new File(mVoicePath).delete();
        toRecordIdle();
    }

    private void use () {
        if (mDispatcher != null) {
            mDispatcher.onUse(mVoicePath, mDuration);
        }
        toRecordIdle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVoiceManager.setOnRecordListener(null);
        if (mVoicePath != null) {
            giveUp();
        }
    }

    @Override
    public void onRecordStart(String targetPath) {
        mVoiceBtn.setImageResource(R.drawable.ic_record_stop);
        mGiveUpTv.setEnabled(false);
        mUseTv.setEnabled(false);
        mVoiceBtn.setKeepScreenOn(true);
        if (mRecordListener != null) {
            mRecordListener.onRecordStart(targetPath);
        }
    }

    @Override
    public void onRecording(String targetPath, MediaRecorder recorder, long currentDuration) {
        if (mRecordListener != null) {
            mRecordListener.onRecording(targetPath, recorder, currentDuration);
        }
        mDurationTv.setText(TimeUtils.format(currentDuration));
        mDurationTv.setTextColor(currentDuration > 50 * 1000 ? getResources().getColor(android.R.color.holo_red_light) : getResources().getColor(R.color.color_c2));
    }

    @Override
    public void onRecordStop(String targetPath, long durationInMills) {
        mVoiceBtn.setKeepScreenOn(false);
        if (durationInMills < 1000) {
            toRecordIdle();
            Toast.makeText(getActivity(), R.string.voice_to_short, Toast.LENGTH_SHORT).show();
            return;
        }
        toPlayIdle(targetPath, durationInMills);
        if (mRecordListener != null) {
            mRecordListener.onRecordStop(targetPath, durationInMills);
        }
    }

    private void toRecordIdle () {
        mBtnContainer.setVisibility(View.INVISIBLE);
        mGiveUpTv.setEnabled(false);
        mUseTv.setEnabled(false);
        mVoiceBtn.setImageResource(R.drawable.ic_record_start);
        mVoicePath = null;
        mDuration = 0;
        mDurationTv.setText(R.string.voice_max_duration);
    }

    private void toPlayIdle(String targetPath, long durationInMills) {
        mVoiceBtn.setImageResource(R.drawable.ic_record_play);
        mGiveUpTv.setEnabled(true);
        mUseTv.setEnabled(true);
        mVoicePath = targetPath;
        mDuration = (int)durationInMills;
        mBtnContainer.setVisibility(View.VISIBLE);
        mDurationTv.setTextColor(getResources().getColor(R.color.color_c2));
    }

    public void setVoiceDispatcher (VoiceDispatcher dispatcher) {
        mDispatcher = dispatcher;
    }

    @Override
    public void onPlayStart(VoiceManager manager, MediaPlayer player) {
        mVoiceBtn.setImageResource(R.drawable.ic_record_stop);
        mDurationTv.setText("00:00");
        mGiveUpTv.setEnabled(false);
        mUseTv.setEnabled(false);
    }

    @Override
    public void onPlaying(VoiceManager manager,String path, MediaPlayer player, long position) {
        mDurationTv.setText(TimeUtils.format(position));
    }

    @Override
    public void onPlayStop(VoiceManager manager) {
        toPlayIdle(mVoicePath, mDuration);
    }

    public void setOnRecordListener (VoiceManager.OnRecordListener listener) {
        mRecordListener = listener;
    }

    public static interface VoiceDispatcher {
        public void onGiveUp (String path);
        public void onUse (String path, int duration);
    }

}

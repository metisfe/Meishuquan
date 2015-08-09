package com.metis.commentpart.fragment;

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
import com.metis.commentpart.R;

import java.io.File;

/**
 * Created by Beak on 2015/8/7.
 */
public class VoiceFragment extends BaseFragment implements View.OnClickListener, VoiceManager.OnRecordListener{

    private ImageView mVoiceBtn = null;
    private TextView mGiveUpTv, mUseTv;

    private VoiceManager mVoiceManager = null;

    private String mVoicePath = null;

    private VoiceDispatcher mDispatcher = null;

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
        mGiveUpTv = (TextView)view.findViewById(R.id.voice_give_up);
        mUseTv = (TextView)view.findViewById(R.id.voice_send);

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
                mVoiceManager.startPlay(mVoicePath);
            }

        } else if (id == mGiveUpTv.getId()) {
            if (mDispatcher != null) {
                mDispatcher.onGiveUp(mVoicePath);
            }
            new File(mVoicePath).delete();
            mVoicePath = null;
            //mVoiceBtn.setImageResource(R.drawable);
        } else if (id == mUseTv.getId()) {
            if (mDispatcher != null) {
                mDispatcher.onUse(mVoicePath);
            }
            mVoicePath = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVoiceManager.setOnRecordListener(null);
    }

    @Override
    public void onRecordStart(String targetPath) {
        mVoiceBtn.setImageResource(R.drawable.image_broken);
        Toast.makeText(getActivity(), "onRecordStart targetPath=" + targetPath, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecording(String targetPath, MediaRecorder recorder) {

    }

    @Override
    public void onRecordStop(String targetPath, long durationInMills) {
        mVoiceBtn.setImageResource(R.drawable.ic_camera);
        mVoicePath = targetPath;
        Toast.makeText(getActivity(), "onRecordStop targetPath=" + targetPath + " durationInMills=" + durationInMills, Toast.LENGTH_SHORT).show();
    }

    public void setVoiceDispatcher (VoiceDispatcher dispatcher) {
        mDispatcher = dispatcher;
    }

    public static interface VoiceDispatcher {
        public void onGiveUp (String path);
        public void onUse (String path);
    }
}

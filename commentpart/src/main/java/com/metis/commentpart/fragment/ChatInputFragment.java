package com.metis.commentpart.fragment;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.metis.base.fragment.BaseFragment;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.VoiceManager;
import com.metis.base.module.User;
import com.metis.base.utils.FragmentUtils;
import com.metis.base.utils.Log;
import com.metis.base.utils.SystemUtils;
import com.metis.commentpart.R;

/**
 * Created by Beak on 2015/8/7.
 */
public class ChatInputFragment extends BaseFragment implements View.OnClickListener, VoiceManager.OnRecordListener, VoiceFragment.VoiceDispatcher {

    private static final String TAG = ChatInputFragment.class.getSimpleName();

    private EditText mInputEt = null;
    private ImageView mVoiceIv, mSendIv;

    private Fragment mExtraFragment = null;

    private VoiceFragment mVoiceFragment = null;
    private VoiceFragment.VoiceDispatcher mDispatcher = null;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                mVoiceIv.setVisibility(View.GONE);
                mSendIv.setVisibility(View.VISIBLE);
            } else {
                mVoiceIv.setVisibility(View.VISIBLE);
                mSendIv.setVisibility(View.GONE);
            }
        }
    };

    private Controller mController = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_input, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInputEt = (EditText)view.findViewById(R.id.chat_text);
        mVoiceIv = (ImageView)view.findViewById(R.id.chat_voice);
        mSendIv = (ImageView)view.findViewById(R.id.chat_send);

        mInputEt.addTextChangedListener(mTextWatcher);

        mVoiceIv.setOnClickListener(this);
        mSendIv.setOnClickListener(this);

        User me = AccountManager.getInstance(getActivity()).getMe();
        if (me == null || (me.userRole != User.USER_ROLE_STUDIO && me.userRole != User.USER_ROLE_TEACHER)) {
            mVoiceIv.setVisibility(View.GONE);
            mSendIv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        final int id = v.getId();

        if (id == mVoiceIv.getId()) {
            if (mExtraFragment != null) {
                getActivity().onBackPressed();
                //FragmentUtils.hideFragment(getChildFragmentManager(), mExtraFragment);
            } else {
                if (mVoiceFragment == null) {
                    mVoiceFragment = new VoiceFragment();
                    mVoiceFragment.setVoiceDispatcher(this);
                    mVoiceFragment.setOnRecordListener(this);
                }
                FragmentUtils.showFragment(getFragmentManager(), mVoiceFragment, R.id.chat_input_extra_container);
                toVoice();
                mExtraFragment = mVoiceFragment;
            }
        } else if (id == mSendIv.getId()) {
            String content = mInputEt.getText().toString();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(getActivity(), R.string.status_detail_input_not_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            mInputEt.setText("");
            if (mController != null) {
                mController.onSend(content);
            }
        }
    }

    public boolean onBackPressed () {
        if (mExtraFragment != null && mExtraFragment == mVoiceFragment) {
            FragmentUtils.hideFragment(getChildFragmentManager(), mExtraFragment);
            toText();
            mExtraFragment = null;
            return true;
        }
        return false;
    }

    private void toVoice () {
        mVoiceIv.setImageResource(R.drawable.ic_keyboard_sel);
        SystemUtils.hideIME(getActivity(), mInputEt);
    }

    private void toText () {
        mVoiceIv.setEnabled(true);
        mInputEt.setEnabled(true);
        mVoiceIv.setImageResource(R.drawable.ic_microphone);
        mInputEt.requestFocus();
    }

    public void setVoiceDispatcher (VoiceFragment.VoiceDispatcher dispatcher) {
        mDispatcher = dispatcher;
    }

    public void setController (Controller controller) {
        mController = controller;
    }

    @Override
    public void onRecordStart(String targetPath) {
        mInputEt.setEnabled(false);
        mVoiceIv.setEnabled(false);
    }

    @Override
    public void onRecording(String targetPath, MediaRecorder recorder, long currentDuration) {

    }

    @Override
    public void onRecordStop(String targetPath, long durationInMills) {

    }

    @Override
    public void onGiveUp(String path) {
        mInputEt.setEnabled(true);
        mVoiceIv.setEnabled(true);
        if (mDispatcher != null) {
            mDispatcher.onGiveUp(path);
        }
    }

    @Override
    public void onUse(String path, int duration) {
        mVoiceIv.setEnabled(true);
        mVoiceIv.setEnabled(true);
        if (mDispatcher != null) {
            mDispatcher.onUse(path, duration);
        }
    }

    public void askToInput () {
        SystemUtils.showIME(getActivity(), mInputEt);
    }

    public interface Controller {
        public void onSend (String content);
    }
}

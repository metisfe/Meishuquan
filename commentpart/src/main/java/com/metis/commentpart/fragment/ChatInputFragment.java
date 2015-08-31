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
public class ChatInputFragment extends BaseFragment implements View.OnClickListener, VoiceManager.OnRecordListener,
        VoiceFragment.VoiceDispatcher, AccountManager.OnUserChangeListener {

    private static final String TAG = ChatInputFragment.class.getSimpleName();

    private EditText mInputEt = null;
    private ImageView mVoiceIv, mSendIv;
    private View mInputMaskView = null;
    private Fragment mExtraFragment = null;

    private VoiceFragment mVoiceFragment = null;
    private VoiceFragment.VoiceDispatcher mDispatcher = null;

    private boolean mNeedShowVoiceBtnWhenClear = false;

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
                mVoiceIv.setVisibility(mNeedShowVoiceBtnWhenClear ? View.VISIBLE : View.GONE);
                mSendIv.setVisibility(mNeedShowVoiceBtnWhenClear ? View.GONE : View.VISIBLE);
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
        mInputMaskView = view.findViewById(R.id.chat_text_mask);
        mVoiceIv = (ImageView)view.findViewById(R.id.chat_voice);
        mSendIv = (ImageView)view.findViewById(R.id.chat_send);

        mInputEt.addTextChangedListener(mTextWatcher);

        mInputMaskView.setOnClickListener(this);
        mVoiceIv.setOnClickListener(this);
        mSendIv.setOnClickListener(this);


    }

    @Override
    public void onResume() {
        super.onResume();
        checkUser();
        AccountManager.getInstance(getActivity()).registerOnUserChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        AccountManager.getInstance(getActivity()).unregisterOnUserChangeListener(this);
    }

    private void checkUser () {
        User me = AccountManager.getInstance(getActivity()).getMe();
        if (me == null || me.userRole == User.USER_ROLE_STUDENT || me.userRole == User.USER_ROLE_PARENTS) {
            mVoiceIv.setVisibility(View.GONE);
            mSendIv.setVisibility(View.VISIBLE);
        } else if (me != null) {
            if (me.userRole == User.USER_ROLE_TEACHER) {
                mVoiceIv.setVisibility(View.VISIBLE);
                mSendIv.setVisibility(View.GONE);
            } else if (me.userRole == User.USER_ROLE_STUDIO) {
                mVoiceIv.setVisibility(View.GONE);
                mSendIv.setVisibility(View.GONE);
            }
        }

        if (me == null) {
            showMask();
            setEnable(false);
            setInputHint(getString(R.string.hint_comment_need_login));
        } else {
            if (me.userRole == User.USER_ROLE_STUDIO) {
                setEnable(false);
                setInputHint(getString(R.string.hint_comment_not_for_studio));
            } else {
                hideMask();
                setEnable(true);
                setInputHint(getString(R.string.status_item_publish_comment));
            }
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
                askToRecord();
            }
        } else if (id == mSendIv.getId()) {
            String content = mInputEt.getText().toString();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(getActivity(), R.string.status_detail_input_not_empty, Toast.LENGTH_SHORT).show();
                return;
            }

            if (mController != null) {
                mNeedShowVoiceBtnWhenClear = mController.onSend(content);
            }
            mInputEt.setText("");
        } else if (id == mInputMaskView.getId()) {
            if (mController != null) {
                mController.onInputMaskClick(v);
            }
        }
    }

    public void hideMask () {
        mInputMaskView.setVisibility(View.GONE);
    }

    public void showMask () {
        mInputMaskView.setVisibility(View.VISIBLE);
    }

    public void setInputEnable (boolean enable) {
        mInputEt.setEnabled(enable);
    }

    public void setInputHint (CharSequence charSequence) {
        mInputEt.setHint(charSequence);
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
        checkUser();
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
        checkUser();
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
        checkUser();
    }

    @Override
    public void onUse(String path, int duration) {
        mVoiceIv.setEnabled(true);
        mVoiceIv.setEnabled(true);
        if (mDispatcher != null) {
            mDispatcher.onUse(path, duration);
        }
        checkUser();
    }

    public void askToInput () {
        mInputEt.requestFocus();
        //SystemUtils.toggleIME(getActivity());
        SystemUtils.showIME(getActivity(), mInputEt);
    }

    public void askToRecord () {
        if (mVoiceFragment == null) {
            mVoiceFragment = new VoiceFragment();
            mVoiceFragment.setVoiceDispatcher(this);
            mVoiceFragment.setOnRecordListener(this);
        }
        FragmentUtils.showFragment(getFragmentManager(), mVoiceFragment, R.id.chat_input_extra_container);
        toVoice();
        mExtraFragment = mVoiceFragment;
    }

    @Override
    public void onUserChanged(User user, boolean onLine) {
        checkUser();
    }

    public interface Controller {
        /**
         *
         * @param content
         * @return true if showVoiceBtn
         */
        public boolean onSend (String content);
        public void onInputMaskClick (View view);
    }

    public void setEnable (boolean enable) {
        mInputEt.setEnabled(enable);
        mVoiceIv.setEnabled(enable);
        mSendIv.setEnabled(enable);
    }
}

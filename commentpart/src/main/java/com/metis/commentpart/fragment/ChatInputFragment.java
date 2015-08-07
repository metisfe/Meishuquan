package com.metis.commentpart.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;

import com.metis.base.fragment.BaseFragment;
import com.metis.base.manager.VoiceManager;
import com.metis.base.utils.FragmentUtils;
import com.metis.commentpart.R;

import java.io.File;

/**
 * Created by Beak on 2015/8/7.
 */
public class ChatInputFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = ChatInputFragment.class.getSimpleName();

    private EditText mInputEt = null;
    private ImageView mVoiceIv, mSendIv;

    private Fragment mExtraFragment = null;

    private VoiceFragment mVoiceFragment = null;

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
    }

    @Override
    public void onClick(View v) {

        final int id = v.getId();

        if (id == mVoiceIv.getId()) {
            if (mExtraFragment != null) {
                FragmentUtils.hideFragment(getChildFragmentManager(), mExtraFragment);
                mExtraFragment = null;
            } else {
                if (mVoiceFragment == null) {
                    mVoiceFragment = new VoiceFragment();
                }
                FragmentUtils.showFragment(getChildFragmentManager(), mVoiceFragment, R.id.chat_input_extra_container);
                mExtraFragment = mVoiceFragment;
            }
        }
    }
}

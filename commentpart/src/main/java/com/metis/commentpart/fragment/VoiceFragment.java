package com.metis.commentpart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.metis.base.fragment.BaseFragment;
import com.metis.commentpart.R;

/**
 * Created by Beak on 2015/8/7.
 */
public class VoiceFragment extends BaseFragment {

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

}

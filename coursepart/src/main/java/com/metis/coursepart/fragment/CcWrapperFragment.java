package com.metis.coursepart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.metis.base.fragment.BaseFragment;
import com.metis.base.fragment.CcPlayFragment;
import com.metis.base.fragment.CcPlayFragment.PlayerCallback;
import com.metis.base.utils.Log;
import com.metis.base.widget.VideoMaskView;
import com.metis.coursepart.R;
import com.metis.coursepart.module.Course;

/**
 * Created by Beak on 2015/10/27.
 */
public class CcWrapperFragment extends BaseFragment implements VideoMaskView.OnButtonListener, CcPlayFragment.OnFullScreenCallback {

    private static final String TAG = CcWrapperFragment.class.getSimpleName();

    private CcPlayFragment mPlayFragment = null;

    private VideoMaskView mMaskView = null;

    private Course mCourse = null;

    private CcPlayFragment.OnFullScreenCallback mFullScreenCallback = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_cc_wrapper, null, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPlayFragment = (CcPlayFragment)getChildFragmentManager().findFragmentById(R.id.cc_wrapper_fragment);
        mMaskView = (VideoMaskView)view.findViewById(R.id.cc_wrapper_mask_view);
        mMaskView.setOnButtonListener(this);

        mPlayFragment.registerPlayerCallback(mMaskView);
        mPlayFragment.setOnFullScreenCallback(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMaskView.setOnButtonListener(null);
        mPlayFragment.unregisterPlayerCallback(mMaskView);
        mPlayFragment.setOnFullScreenCallback(null);
    }

    public boolean isFullScreen () {
        return mPlayFragment != null && mPlayFragment.isFullScreen();
    }

    public void setFullScreen (boolean fullScreen) {
        if (mPlayFragment == null) {
            return;
        }
        mPlayFragment.setFullScreen(fullScreen);
    }

    public void setCourse (Course course) {
        mCourse = course;
        if (course != null) {
            mMaskView.setCover(mCourse.videoPic);
        }
    }

    public void play () {
        playCourse(mCourse);
    }

    public void playCourse (Course course) {
        if (course == null) {
            return;
        }
        mMaskView.setTitle(course.subCourseName);
        mPlayFragment.startRemotePlayWithUrl(course.videoUrl);
    }

    public void resumePlay () {
        if (mPlayFragment != null) {
            mPlayFragment.resumePlay();
        }
    }

    @Override
    public void onBackBtnPressed(@VideoMaskView.State int state) {
        if (mPlayFragment.isFullScreen()) {
            mPlayFragment.setFullScreen(false);
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void onPlayOrPauseBtnPressed(@VideoMaskView.State int state) {
        Log.v(TAG, "onPlayOrPauseBtnPressed " + state);
        if (state == VideoMaskView.STATE_PAUSED) {
            mPlayFragment.resumePlay();
        } else if (state != VideoMaskView.STATE_STARTED) {
            play();
        } else {
            mPlayFragment.pausePlay();
        }
    }

    @Override
    public void onFullScreenPressed(@VideoMaskView.State int state) {
        mPlayFragment.setFullScreen(!mPlayFragment.isFullScreen());
    }

    @Override
    public void onSeekStart(SeekBar seekBar) {
        mPlayFragment.setSeeking(true);
    }

    @Override
    public void onSeekStop(SeekBar seekBar) {
        mPlayFragment.setSeeking(false);
        int progress = seekBar.getProgress();
        int duration = mPlayFragment.getDuration();

        mPlayFragment.seekTo(duration / 100 * progress);
    }

    public void setOnFullScreenCallback (CcPlayFragment.OnFullScreenCallback callback) {
        mFullScreenCallback = callback;
    }

    @Override
    public void onFullScreen(boolean fullScreen) {
        if (mFullScreenCallback != null) {
            mFullScreenCallback.onFullScreen(fullScreen);
        }
        mMaskView.setFullScreen(fullScreen);

        if (fullScreen) {
            mMaskView.delayHide();
        }
    }

    public void registerPlayerCallback (PlayerCallback callback) {
        mPlayFragment.registerPlayerCallback(callback);
    }

    public void unregisterPlayerCallback (PlayerCallback callback) {
        mPlayFragment.unregisterPlayerCallback(callback);
    }

}

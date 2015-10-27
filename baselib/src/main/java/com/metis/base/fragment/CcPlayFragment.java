package com.metis.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.R;
import com.metis.base.manager.PlayerManager;


/**
 * Created by Beak on 2015/9/24.
 */
public class CcPlayFragment extends Fragment implements
        SurfaceHolder.Callback{

    private static final String TAG = CcPlayFragment.class.getSimpleName();

    private static CcPlayFragment sFragment = null;

    public static CcPlayFragment getInstance () {
        if (sFragment == null) {
            sFragment = new CcPlayFragment();
        }
        return sFragment;
    }

    private SurfaceView mCcPlaySurfaceView = null;

    /*private DWMediaPlayer mPlayer = null;

    private DRMServer mDrmServer = null;*/

    private Context mAppContext = null;

    private boolean isSufaceViewCreated = false, isStarted = false;

    private PlayerProperty mProperty = null;

    private PlayerManager mManager = null;

    private OnFullScreenCallback mFullScreenCallback = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppContext = context.getApplicationContext();
        mProperty = PlayerProperty.getInstance(context);
        mManager = PlayerManager.getInstance(context);
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cc_play, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCcPlaySurfaceView = (SurfaceView)view.findViewById(R.id.cc_play_surface_view);
        mCcPlaySurfaceView.getHolder().addCallback(this);
        //mCcPlaySurfaceView.setSurfaceTextureListener(this);

        Log.v(TAG, TAG + " onViewCreated");
    }

    public void setFullScreen(boolean fullScreen) {
        View view = getActivity().getWindow().getDecorView();
        if (fullScreen) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LOW_PROFILE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            view.setSystemUiVisibility(0);
        }
        if (mFullScreenCallback != null) {
            mFullScreenCallback.onFullScreen(fullScreen);
        }
    }

    public boolean isFullScreen () {
        return getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    public PlayerManager getPlayerManager () {
        return mManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mManager.release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mManager.setSurface(holder.getSurface());
        isSufaceViewCreated = true;
        Log.v(TAG, TAG + " surfaceCreated ");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(TAG, TAG + " surfaceChanged ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isSufaceViewCreated = false;
        Log.v(TAG, TAG + " surfaceDestroyed " + isStarted);
        if (mManager.isStarted()) {
            mManager.stopPlay();
            mManager.release();
        }
        mCcPlaySurfaceView.getHolder().removeCallback(this);
    }

    public boolean isStarted () {
        return mManager.isStarted();
    }

    public void setOnFullScreenCallback (OnFullScreenCallback callback) {
        mFullScreenCallback = callback;
    }

    public interface OnFullScreenCallback {
        public void onFullScreen (boolean fullScreen);
    }
}

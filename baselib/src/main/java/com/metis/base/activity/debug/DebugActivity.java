package com.metis.base.activity.debug;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.metis.base.R;
import com.metis.base.fragment.CcPlayFragment;
import com.metis.base.manager.PlayerManager;
import com.metis.base.utils.Log;

public class DebugActivity extends AppCompatActivity implements View.OnClickListener, PlayerManager.PlayerCallback, CcPlayFragment.OnFullScreenCallback{

    private static final String TAG = DebugActivity.class.getSimpleName();

    private CcPlayFragment mCcPlayerFragment = null;

    private Button mBtn = null, mFullScreen = null;

    private FrameLayout mFragmentLayout = null;

    private static final String TEST_ID = "2C4349EB23345F859C33DC5901307461";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        mCcPlayerFragment = CcPlayFragment.getInstance();

        mFragmentLayout = (FrameLayout)findViewById(R.id.debug_player_container);

        mBtn = (Button)findViewById(R.id.debug_start);
        mBtn.setOnClickListener(this);

        mFullScreen = (Button)findViewById(R.id.debug_full_screen);
        mFullScreen.setOnClickListener(this);

        PlayerManager.getInstance(this).setPlayerCallback(this);
        mCcPlayerFragment.setOnFullScreenCallback(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.debug_player_container, mCcPlayerFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (mCcPlayerFragment.isFullScreen()) {
            mCcPlayerFragment.setFullScreen(false);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == mBtn.getId()) {
            if (mCcPlayerFragment.isStarted()) {
                PlayerManager.getInstance(this).stopPlay();
            } else {
                PlayerManager.getInstance(this).startRemotePlay(TEST_ID);
                //mCcPlayerFragment.startLocalPlay(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + "output.mp4");
            }
        } else if (id == mFullScreen.getId()) {
            if (mCcPlayerFragment.isFullScreen()) {
                mCcPlayerFragment.setFullScreen(false);
            } else {
                mCcPlayerFragment.setFullScreen(true);
            }

            /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(mCcPlayerFragment);
            ft.commit();
            PlayerManager.getInstance(this).pausePlay();
            Intent it = new Intent(this, FullScreenActivity.class);
            startActivity(it);*/
        }
    }

    @Override
    public void onPlayerStarted() {
        Toast.makeText(this, "start loading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlayerPrepared(int width, int height) {
        Log.v(TAG, "need size change onPlayerPrepared width=" + width + " height=" + height);
        if (mCcPlayerFragment != null && !mCcPlayerFragment.isFullScreen()) {
            final int winWid = getWindow().getWindowManager().getDefaultDisplay().getWidth();
            final int winHei = getWindow().getWindowManager().getDefaultDisplay().getHeight();

            final float scaleWid = (float)winWid / width;
            final float scaleHei = (float)winHei / height;

            final float scale = Math.min(scaleWid, scaleHei);

            final int targetWid = (int)(width * scale);
            final int targetHei = (int)(height * scale);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mFragmentLayout.getLayoutParams();
            if (params != null) {
                params.width = targetWid;
                params.height = targetHei;
            } else {
                params = new RelativeLayout.LayoutParams(targetWid, targetHei);
            }
            Log.v(TAG, "need size change onPlayerPrepared params.width=" + params.width + " params.height=" + params.height);
            mFragmentLayout.setLayoutParams(params);
        }
    }

    @Override
    public void onPlayerPaused() {

    }

    @Override
    public void onPlayerResumed() {

    }

    @Override
    public void onPlayerStopped() {

    }

    @Override
    public void onPlayerCompleted() {

    }

    @Override
    public void onFullScreen(boolean fullScreen) {
        Log.v(TAG, "need size change onFullScreen ");
        final int winWid = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        final int winHei = getWindow().getWindowManager().getDefaultDisplay().getHeight();

        int[] size = PlayerManager.getInstance(this).getSize();

        final int width = size != null ? size[0] : 0;
        final int height = size != null ? size[1] : 0;

        int targetWid = 0;
        int targetHei = 0;
        if (fullScreen) {
            targetWid = winWid;
            targetHei = winHei;
        } else {
            final float scaleWid = (float)winWid / width;
            final float scaleHei = (float)winHei / height;

            final float scale = Math.min(scaleWid, scaleHei);

            targetWid = (int)(width * scale);
            targetHei = (int)(height * scale);
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mFragmentLayout.getLayoutParams();
        if (params != null) {
            params.width = targetWid;
            params.height = targetHei;
        } else {
            params = new RelativeLayout.LayoutParams(targetWid, targetHei);
        }
        Log.v(TAG, "need size change onFullScreen params.width=" + params.width + " params.height=" + params.height);
        mFragmentLayout.setLayoutParams(params);
    }
}

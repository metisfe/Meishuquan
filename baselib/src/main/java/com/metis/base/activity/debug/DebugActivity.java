package com.metis.base.activity.debug;

import android.os.Environment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.cyberplayer.core.BVideoView;
import com.metis.base.R;
import com.metis.playerlib.VideoWrapperFragment;

import java.io.File;

public class DebugActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mStartBtn, mPauseBtn, mFullScreenBtn;
    private VideoWrapperFragment mVideoFragment = null;
    private TextView mLogTv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        mStartBtn = (Button)findViewById(R.id.start);
        mPauseBtn = (Button)findViewById(R.id.pause);
        mFullScreenBtn = (Button)findViewById(R.id.fullScreen);

        mVideoFragment = (VideoWrapperFragment)getSupportFragmentManager().findFragmentById(R.id.debug_video_fragment);

        mLogTv = (TextView)findViewById(R.id.debug_log);

        String source = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Movies" + File.separator + "Taylor_Swift.mp4";
        mVideoFragment.setSource(source);

        mStartBtn.setOnClickListener(this);
        mPauseBtn.setOnClickListener(this);
        mFullScreenBtn.setOnClickListener(this);
        mLogTv.setOnClickListener(this);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == mLogTv.getId()) {
        } else if (id == mStartBtn.getId()) {
        } else if (id == mPauseBtn.getId()) {
        } else if (id == mFullScreenBtn.getId()) {
        }
    }

}

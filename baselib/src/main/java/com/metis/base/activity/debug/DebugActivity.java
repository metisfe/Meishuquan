package com.metis.base.activity.debug;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.metis.base.R;
import com.metis.base.fragment.CcPlayFragment;

public class DebugActivity extends AppCompatActivity implements View.OnClickListener, CcPlayFragment.OnFullScreenCallback{

    private static final String TAG = DebugActivity.class.getSimpleName();

    private static final String TEST_ID = "2C4349EB23345F859C33DC5901307461";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onFullScreen(boolean fullScreen) {
    }
}

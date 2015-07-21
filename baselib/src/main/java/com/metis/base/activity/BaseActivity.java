package com.metis.base.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Beak on 2015/7/21.
 */
public class BaseActivity extends AppCompatActivity {

    private boolean isAlive = false;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        isAlive = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAlive = false;
    }

    public boolean isAlive () {
        return isAlive;
    }
}

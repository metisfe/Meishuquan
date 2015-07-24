package com.metis.base.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Beak on 2015/7/2.
 */
public class BaseFragment extends Fragment {

    private boolean isAlive = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAllowEnterTransitionOverlap(true);
        setAllowReturnTransitionOverlap(true);
        isAlive = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isAlive = false;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    public boolean isAlive () {
        return isAlive;
    }
}

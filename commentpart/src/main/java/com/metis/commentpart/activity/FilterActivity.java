package com.metis.commentpart.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.metis.base.activity.TitleBarActivity;
import com.metis.commentpart.R;

public class FilterActivity extends TitleBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fliter);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

}

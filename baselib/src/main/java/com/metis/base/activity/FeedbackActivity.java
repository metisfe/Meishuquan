package com.metis.base.activity;

import android.os.Bundle;

import com.metis.base.R;

public class FeedbackActivity extends TitleBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.text_my_feedback);
    }
}

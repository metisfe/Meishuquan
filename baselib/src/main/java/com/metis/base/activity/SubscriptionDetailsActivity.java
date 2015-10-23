package com.metis.base.activity;

import android.os.Bundle;

import com.metis.base.R;

public class SubscriptionDetailsActivity extends TitleBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_details);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }
}

package com.metis.base.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.metis.base.R;

public class SubscriptionActivity extends TitleBarActivity {

    private RecyclerView mSubscriptionRv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_stription);

        mSubscriptionRv = (RecyclerView)findViewById(R.id.subscription_recycler_view);
        mSubscriptionRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.title_activity_subscription);
    }
}

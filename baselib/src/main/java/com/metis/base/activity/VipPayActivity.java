package com.metis.base.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.metis.base.R;

public class VipPayActivity extends TitleBarActivity implements View.OnClickListener {

    private TextView mGoToPayBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_pay);

        mGoToPayBtn = (TextView)findViewById(R.id.vip_pay_go);
    }

    @Override
    public void onClick(View v) {

    }
}

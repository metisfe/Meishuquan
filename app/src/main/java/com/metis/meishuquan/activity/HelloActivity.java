package com.metis.meishuquan.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.metis.base.ActivityDispatcher;
import com.metis.base.activity.BaseActivity;
import com.metis.meishuquan.R;


public class HelloActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        this.getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityDispatcher.loginActivity(HelloActivity.this);
                //startActivity(new Intent(HelloActivity.this, LoginActivity.class/*RoleChooseActivity.class*/));
                finish();
            }
        }, 3000);
    }

}

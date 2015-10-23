package com.metis.base.activity;

import android.os.Bundle;

import com.metis.base.ActivityDispatcher;
import com.metis.base.R;
import com.metis.base.fragment.HomePageFragment;
import com.metis.base.manager.RequestCallback;
import com.metis.base.manager.UserManager;
import com.metis.base.module.User;
import com.metis.msnetworklib.contract.ReturnInfo;

public class UserActivity extends TitleBarActivity {

    private long mUserId = 0;

    private HomePageFragment mHomeFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mHomeFragment = (HomePageFragment) getSupportFragmentManager().findFragmentById(R.id.user_fragment);

        User user = (User)getIntent().getSerializableExtra(ActivityDispatcher.KEY_USER);
        if (user == null) {
            mUserId = getIntent().getLongExtra(ActivityDispatcher.KEY_USER_ID, 0);
            UserManager.getInstance(this).getUserInfo(mUserId, new RequestCallback<User>() {
                @Override
                public void callback(ReturnInfo<User> returnInfo, String callbackId) {
                    if (returnInfo.isSuccess()) {
                        mHomeFragment.setUser(returnInfo.getData());
                    }
                }
            });
        } else {
            mHomeFragment.setUser(user);
        }

    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }
}

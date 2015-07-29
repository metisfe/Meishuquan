package com.metis.commentpart.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.metis.base.activity.TitleBarActivity;
import com.metis.commentpart.R;

public class InviteActivity extends TitleBarActivity {

    private RecyclerView mSelectedRv, mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        mSelectedRv = (RecyclerView)findViewById(R.id.invite_selected_recycler_view);
        mRv = (RecyclerView)findViewById(R.id.invite_recycler_view);

        mSelectedRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRv.setLayoutManager(new LinearLayoutManager(this));

        getTitleBar().setDrawableResourceRight(R.drawable.ic_check);
        getTitleBar().setOnRightBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.title_activity_invite);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }
}

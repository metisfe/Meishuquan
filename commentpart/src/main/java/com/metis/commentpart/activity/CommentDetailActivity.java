package com.metis.commentpart.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.metis.base.activity.TitleBarActivity;
import com.metis.commentpart.R;


public class CommentDetailActivity extends TitleBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);

        getTitleBar().setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.title_activity_comment_detail);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }
}

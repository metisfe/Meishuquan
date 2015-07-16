package com.metis.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.metis.base.widget.TitleBar;

/**
 * Created by Beak on 2015/7/8.
 */
public abstract class TitleBarActivity extends AppCompatActivity {

    private TitleBar mTitleBar = null;
    private FrameLayout mContentViewContainer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_title_bar);

        mTitleBar = (TitleBar)findViewById(R.id.title_bar);
        mContentViewContainer = (FrameLayout)findViewById(R.id.content_view_container);

        mTitleBar.setTitleLeft(getTitleLeft());
        mTitleBar.setTitleCenter(getTitleCenter());
        if (showAsUpEnable()) {
            mTitleBar.setDrawableResourceLeft(R.drawable.ic_title_back);
            mTitleBar.setOnLeftBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        if (!isTitleBarOverlay()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mTitleBar.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.title_bar);
        }
    }

    public CharSequence getTitleCenter () {
        return null;
    }

    public TitleBar getTitleBar () {
        return mTitleBar;
    }

    public CharSequence getTitleLeft () {
        return null;
    }

    public boolean isTitleBarOverlay () {
        return false;
    }

    public boolean showAsUpEnable () {
        return false;
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null,true);
        setContentView(view);
        //super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        //super.setContentView(view);
        mContentViewContainer.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        //super.setContentView(view, params);
        mContentViewContainer.addView(view, params);
    }
}

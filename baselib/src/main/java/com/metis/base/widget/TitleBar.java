package com.metis.base.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.base.R;

/**
 * Created by Beak on 2015/7/6.
 */
public class TitleBar extends RelativeLayout{

    private FrameLayout mLeftFrameLayout = null, mRightFrameLayout = null;
    private FrameLayout mCenterFrameLayout = null;
    private TextView mCenterTitleTv = null, mLeftTitleTv;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initThis(context);
    }

    private void initThis (Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this, true);
        mLeftFrameLayout = (FrameLayout)findViewById(R.id.title_bar_left_btn);
        mRightFrameLayout = (FrameLayout)findViewById(R.id.title_bar_right_btn);
        mCenterFrameLayout = (FrameLayout)findViewById(R.id.title_bar_center);
        mCenterTitleTv = (TextView)findViewById(R.id.title_bar_center_text);
        mLeftTitleTv = (TextView)findViewById(R.id.title_bar_left_text);
    }

    public void setCenterView (View view) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mCenterFrameLayout.removeAllViews();
        mCenterFrameLayout.addView(view, params);
    }

    public void setTitleLeft (CharSequence charSequence) {
        mLeftTitleTv.setText(charSequence);
    }

    public void setTitleLeft (@StringRes int stringRes) {
        mLeftTitleTv.setText(stringRes);
    }

    public void setOnLeftBtnClickListener (OnClickListener listener) {
        mLeftFrameLayout.setOnClickListener(listener);
    }
}

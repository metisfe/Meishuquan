package com.metis.base.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.base.R;

/**
 * Created by Beak on 2015/7/6.
 */
public class TitleBar extends RelativeLayout{

    private FrameLayout mLeftFrameLayout = null, mRightFrameLayout = null;
    private FrameLayout mCenterFrameLayout = null;
    private TextView mCenterTitleTv = null, mLeftTitleTv, mRightTitleTv;
    private ImageView mLeftIv, mRightIv;

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
        mRightTitleTv = (TextView)findViewById(R.id.title_bar_right_text);

        mLeftIv = (ImageView)findViewById(R.id.title_bar_left_img);
        mRightIv = (ImageView)findViewById(R.id.title_bar_right_img);
    }

    public void setCenterView (View view) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mCenterFrameLayout.removeAllViews();
        mCenterFrameLayout.addView(view, params);
    }

    public void setTitleCenter (CharSequence charSequence) {
        mCenterTitleTv.setText(charSequence);
    }

    public void setTitleCenter (@StringRes int stringRes) {
        mCenterTitleTv.setText(stringRes);
    }

    public void setTitleLeft (CharSequence charSequence) {
        mLeftTitleTv.setText(charSequence);
    }

    public void setTitleLeft (@StringRes int stringRes) {
        mLeftTitleTv.setText(stringRes);
    }

    public void setDrawableLeft (Drawable drawableLeft) {
        mLeftIv.setImageDrawable(drawableLeft);
    }

    public void setDrawableResourceLeft (@DrawableRes int drawableResourceLeft) {
        mLeftIv.setImageResource(drawableResourceLeft);
    }

    public void setBitmapLeft (Bitmap bmp) {
        mLeftIv.setImageBitmap(bmp);
    }

    public void setOnLeftBtnClickListener (OnClickListener listener) {
        mLeftFrameLayout.setOnClickListener(listener);
    }


    public void setTitleRight (CharSequence charSequence) {
        mRightTitleTv.setText(charSequence);
    }

    public void setTitleRight (@StringRes int stringRes) {
        mRightTitleTv.setText(stringRes);
    }

    public void setDrawableRight (Drawable drawableLeft) {
        mRightIv.setImageDrawable(drawableLeft);
    }

    public void setDrawableResourceRight (@DrawableRes int drawableResourceLeft) {
        mRightIv.setImageResource(drawableResourceLeft);
    }

    public void setBitmapRight (Bitmap bmp) {
        mRightIv.setImageBitmap(bmp);
    }

    public void setOnRightBtnClickListener (OnClickListener listener) {
        mRightFrameLayout.setOnClickListener(listener);
    }
}

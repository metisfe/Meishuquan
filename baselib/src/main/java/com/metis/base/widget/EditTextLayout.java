package com.metis.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.metis.base.R;

/**
 * Created by Beak on 2015/10/10.
 */
public class EditTextLayout extends RelativeLayout{

    private EditText mEt = null;
    private ImageView mOkIv = null;

    public EditTextLayout(Context context) {
        this(context, null);
    }

    public EditTextLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initThis(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditTextLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initThis(context, attrs);
    }

    private void initThis (Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_std_edit_text, this);
        mEt = (EditText)findViewById(R.id.std_edit_text);
        mOkIv = (ImageView)findViewById(R.id.std_edit_text_ok);

        mEt.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    mOkIv.performClick();
                }
                return false;
            }
        });
    }

    public void setText (CharSequence text) {
        mEt.setText(text);
    }

    public CharSequence getText () {
        return mEt.getText();
    }

    public void setSingleLine () {
        mEt.setSingleLine();
    }

    public void setContentGravity (int gravity) {
        mEt.setGravity(gravity);
    }

    public void setOnOkListener (OnClickListener listener) {
        mOkIv.setOnClickListener(listener);
    }
}

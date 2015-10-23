package com.metis.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.base.R;

import java.util.Map;

/**
 * Created by Beak on 2015/10/8.
 */
public class KeyValueLayout extends RelativeLayout {

    private TextView mKeyTv = null;
    private TextView mValueTv = null;
    private ImageView mEditableFlagIv = null;
    private FrameLayout mValueContainer = null;

    public KeyValueLayout(Context context) {
        this(context, null);
    }

    public KeyValueLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyValueLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initThis(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KeyValueLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initThis(context, attrs);
    }

    private void initThis (Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_key_value, this);
        mKeyTv = (TextView)findViewById(R.id.key_value_key_tv);
        mValueTv = (TextView)findViewById(R.id.key_value_value_tv);
        mEditableFlagIv = (ImageView)findViewById(R.id.key_value_editable_flag_iv);
        mValueContainer = (FrameLayout)findViewById(R.id.key_value_value_container);

        if (attrs != null) {
            TypedArray array = null;
            try {
                array = context.obtainStyledAttributes(attrs, R.styleable.KeyValueLayout);
                CharSequence key = array.getText(R.styleable.KeyValueLayout_key);
                CharSequence value = array.getText(R.styleable.KeyValueLayout_value);
                boolean editable = array.getBoolean(R.styleable.KeyValueLayout_editable, false);

                setKeyValue(key, value);
                setEditable(editable);
            } finally {
                if (array != null) {
                    array.recycle();
                }
            }
        }
    }

    public void setKey (CharSequence key) {
        mKeyTv.setText(key);
    }

    public void setKey (@StringRes int keyId) {
        mKeyTv.setText(keyId);
    }

    public CharSequence getKey () {
        return mKeyTv.getText();
    }

    public void setValue (CharSequence value) {
        mValueTv.setText(value);
    }

    public void setValue (@StringRes int valueId) {
        mValueTv.setText(valueId);
    }

    public CharSequence getValue () {
        return mValueTv.getText();
    }

    public void setEditable (boolean editable) {
        mEditableFlagIv.setVisibility(editable ? VISIBLE : GONE);
        mEditableFlagIv.setEnabled(editable);
        mEditableFlagIv.setClickable(editable);
    }

    public boolean isEditable () {
        return mEditableFlagIv.getVisibility() == VISIBLE;
    }

    public void addValueView (View view) {
        mValueContainer.addView(view);
    }

    public void removeValueView (View view) {
        mValueContainer.removeView(view);
    }

    public boolean hasValueView (View view) {
        return mValueContainer.indexOfChild(view) >= 0;
    }

    public void setKeyValue (CharSequence key, CharSequence value) {
        setKey(key);
        setValue(value);
    }

    public void setKeyValue (@StringRes int keyId, @StringRes int valueId) {
        setKey(keyId);
        setValue(valueId);
    }
}

package com.metis.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.base.R;

/**
 * Created by Beak on 2015/10/14.
 */
public class IconTextView extends RelativeLayout {

    private ImageView mIconIv = null, mEditFlagIv = null;
    private TextView mTextTv = null;

    public IconTextView(Context context) {
        this(context, null);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initContext(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initContext(context, attrs);
    }

    private void initContext (Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.layout_icon_text, this);

        mIconIv = (ImageView)findViewById(R.id.icon_text_icon);
        mTextTv = (TextView)findViewById(R.id.icon_text_text);
        mEditFlagIv = (ImageView)findViewById(R.id.icon_text_editable_flag);

        TypedArray array = null;
        try {
            array = context.obtainStyledAttributes(attrs, R.styleable.IconTextLayout);
            int iconId = array.getResourceId(R.styleable.IconTextLayout_iconRes, 0);
            if (iconId != 0) {
                mIconIv.setImageResource(iconId);
            }
            mTextTv.setText(array.getText(R.styleable.IconTextLayout_text));
            boolean isVisible = array.getBoolean(R.styleable.IconTextLayout_canEdit, true);
            mEditFlagIv.setVisibility(isVisible ? VISIBLE : GONE);
        } finally {
            if (array != null) {
                array.recycle();
            }
        }

    }
}

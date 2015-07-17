package com.metis.base.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.base.R;
import com.metis.base.manager.DisplayManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by Beak on 2015/7/16.
 */
public class ProfileNameView extends RelativeLayout {

    private ImageView mProfileIv = null;
    private TextView mNameTv = null;

    public ProfileNameView(Context context) {
        this(context, null);
    }

    public ProfileNameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileNameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init (Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_profile_name, this);
        mProfileIv = (ImageView)findViewById(R.id.profile);
        mNameTv = (TextView)findViewById(R.id.name);
    }

    public void setName (CharSequence charSequence) {
        mNameTv.setText(charSequence);
    }

    public void setName (@StringRes int stringRes) {
        mNameTv.setText(stringRes);
    }

    public void setProfile (String uri) {
        if (uri == null) {
            mProfileIv.setImageDrawable(null);
        }
        DisplayManager.getInstance(getContext())
                .display(uri, mProfileIv);
    }

    public void setProfile (String uri, DisplayImageOptions options) {
        if (uri == null) {
            mProfileIv.setImageDrawable(null);
        }
        DisplayManager.getInstance(getContext())
                .display(uri, mProfileIv, options);
    }
}

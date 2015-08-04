package com.metis.base.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metis.base.R;
/**
 * Created by Beak on 2015/8/4.
 */
public class DoubleTab extends LinearLayout {

    public static final int INDEX_FIRST = 0, INDEX_SECOND = 1;

    private TextView mFirstTab, mSecondTab;

    private OnTabSwitchListener mTabListener = null;

    private View mCurrentView = null;

    public DoubleTab(Context context) {
        this(context, null);
    }

    public DoubleTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initThis(context);
    }

    private void initThis (Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_double_tab, this);
        mFirstTab = (TextView)findViewById(R.id.tab_first);
        mSecondTab = (TextView)findViewById(R.id.tab_second);

        mFirstTab.setOnClickListener(mTabClickListener);
        mSecondTab.setOnClickListener(mTabClickListener);
    }

    public void setFirstTabText (CharSequence cs) {
        mFirstTab.setText(cs);
    }

    public void setFirstTabText (@StringRes int stringId) {
        mFirstTab.setText(stringId);
    }

    public void setSecondTabText (CharSequence cs) {
        mSecondTab.setText(cs);
    }

    public void setSecondTabText (@StringRes int stringId) {
        mSecondTab.setText(stringId);
    }

    public void select (int index) {
        if (index == INDEX_FIRST) {
            mFirstTab.performClick();
        } else if (index == INDEX_SECOND) {
            mSecondTab.performClick();
        }
    }

    private OnClickListener mTabClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mCurrentView) {
                return;
            }
            if (mCurrentView != null) {
                mCurrentView.setSelected(false);
            }
            v.setSelected(true);
            mCurrentView = v;
            if (mTabListener != null) {
                mTabListener.onSwitch(v.getId() == mFirstTab.getId() ? INDEX_FIRST : INDEX_SECOND);
            }
        }
    };

    public void setOnTabSwitchListener (OnTabSwitchListener listener) {
        mTabListener = listener;
    }

    public static interface OnTabSwitchListener {
        public void onSwitch (int index);
    }
}

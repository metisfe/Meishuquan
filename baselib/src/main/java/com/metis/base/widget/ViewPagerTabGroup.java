package com.metis.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metis.base.R;
import com.metis.base.utils.Log;

/**
 * Created by Beak on 2015/10/17.
 */
public class ViewPagerTabGroup extends LinearLayout implements ViewPager.OnPageChangeListener{

    private static final String TAG = ViewPagerTabGroup.class.getSimpleName();

    public ViewPagerTabGroup(Context context) {
        this(context, null);
    }

    public ViewPagerTabGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerTabGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initThis(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewPagerTabGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initThis(context, attrs);
    }

    private void initThis (Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_view_pager_tab_group, this);
        Resources res = getContext().getResources();
        this.setDividerDrawable(res.getDrawable(R.drawable.divider_vertical));
        this.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        this.setDividerPadding(res.getDimensionPixelSize(R.dimen.padding_middle));
    }

    public void setViewPager (final ViewPager viewPager) {
        PagerAdapter adapter = viewPager.getAdapter();
        removeAllViews();
        for (int i = 0; i < adapter.getCount(); i++) {
            final int index = i;
            TextView tv = makeTabView();
            tv.setText(adapter.getPageTitle(i));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tv.getLayoutParams();
            if (params == null) {
                params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            }
            params.weight = 1;
            tv.setLayoutParams(params);
            this.addView(tv);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(index, true);
                }
            });
        }
        viewPager.addOnPageChangeListener(this);
        updateSelected(viewPager.getCurrentItem());
    }

    private TextView makeTabView () {
        TextView tv = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.layout_pager_tab_view, null);

        /*TextView tv = new TextView(getContext());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        tv.setTextColor(getResources().getColorStateList(R.color.video_tab_text_color));
        tv.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        params.weight = 1;
        tv.setLayoutParams(params);*/
        return tv;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        updateSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void updateSelected (int position) {
        for (int i = 0; i < getChildCount(); i++) {
            this.getChildAt(i).setSelected(i == position);
        }
    }
}

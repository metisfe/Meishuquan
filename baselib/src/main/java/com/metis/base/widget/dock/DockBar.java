package com.metis.base.widget.dock;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.metis.base.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WJ on 2015/6/23.
 */
public class DockBar extends LinearLayout {

    //private List<Dock> mDockList = new ArrayList<Dock>();
    private Map<Dock, DockItemView> mDockMap = new HashMap<Dock, DockItemView>();

    private OnDockItemClickListener mOnDockItemClickListener = null;

    private View mCurrentView = null;

    public DockBar(Context context) {
        this(context, null);
    }

    public DockBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DockBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDockBar(context);
    }

    private void initDockBar (Context context) {
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER);
    }

    public int size () {
        return mDockMap.size();
    }

    public void addDock (final Dock dock) {
        if (!mDockMap.containsKey(dock)) {
            final DockItemView itemView = new DockItemView(this.getContext());
            mDockMap.put(dock, itemView);

            itemView.setDock(dock);
            LinearLayout.LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            params.weight = 1;
            itemView.setTextColor(getResources().getColorStateList(R.color.dock_item_text_color));
            this.addView(itemView, params);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectDock(dock);
                }
            });
        }
    }

    public void selectDock (Dock dock) {
        if (!mDockMap.containsKey(dock)) {
            return;
        }
        View view = mDockMap.get(dock);
        if (mCurrentView != null) {
            mCurrentView.setSelected(false);
        }
        view.setSelected(true);
        mCurrentView = view;

        if (view != null && mOnDockItemClickListener != null) {
            mOnDockItemClickListener.onDockClick(view, dock);
        }
    }

    public void setOnDockItemClickListener (OnDockItemClickListener listener) {
        mOnDockItemClickListener = listener;
    }

    public static class Dock {

        public int id;
        public Drawable icon;
        public String title;
        public Fragment target;

        public Dock (int id, Drawable icon, String title, Fragment target) {
            this.id = id;
            this.icon = icon;
            this.title = title;
            this.target = target;
        }

        public Dock (Context context, int id, @DrawableRes int drawableId, @StringRes int stringId, Fragment target) {
            this.id = id;
            this.icon = context.getResources().getDrawable(drawableId);
            this.title = context.getString(stringId);
            this.target = target;
        }

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof Dock) {
                return ((Dock) o).id == id;
            }
            return false;
        }
    }

    public static interface OnDockItemClickListener {
        public void onDockClick (View view, Dock dock);
    }
}

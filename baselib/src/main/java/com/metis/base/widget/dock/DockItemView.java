package com.metis.base.widget.dock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.base.R;

/**
 * Created by WJ on 2015/6/23.
 */
public class DockItemView extends RelativeLayout {

    private ImageView mDockItemIv = null;
    private TextView mDockItemTv = null;

    private DockBar.Dock mDock = null;

    public DockItemView(Context context) {
        this(context, null);
    }

    public DockItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DockItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDockItemView(context);
    }

    private void initDockItemView (Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_dock_item_view, this);
        mDockItemIv = (ImageView)findViewById(R.id.dock_item_image);
        mDockItemTv = (TextView)findViewById(R.id.dock_item_title);
    }

    public void setDock (DockBar.Dock dock) {
        mDock = dock;
        mDockItemIv.setImageDrawable(dock.icon);
        mDockItemTv.setText(dock.title);
    }
}

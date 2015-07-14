package com.metis.coursepart.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.coursepart.adapter.delegate.CourseDelegateType;
import com.metis.coursepart.adapter.holder.CourseTypeFilterHolder;
import com.metis.coursepart.adapter.holder.FilterHolder;
import com.metis.coursepart.adapter.holder.StudioFilterHolder;

/**
 * Created by Beak on 2015/7/14.
 */
public class FilterAdapter extends DelegateAdapter {

    private int mSelectedPosition = -1;

    private OnFilterSelectedListener mOnFilterSelectedListener = null;

    public FilterAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case CourseDelegateType.ID.ID_FILTER:
                return new FilterHolder(view);
            case CourseDelegateType.ID.ID_FILTER_COURSE_TYPE:
                return new CourseTypeFilterHolder(view);
            case CourseDelegateType.ID.ID_FILTER_STUDIO:
                return new StudioFilterHolder(view);
        }
        throw new Resources.NotFoundException("no AbsViewHolder returned for viewType-" + viewType + " in FilterAdapter");
    }

    @Override
    public void onBindViewHolder(AbsViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedPosition < 0) {
                    mSelectedPosition = findCurrentSelectedOne();
                }
                if (mSelectedPosition >= 0) {
                    AbsDelegate item = getDataItem(mSelectedPosition);
                    if (item instanceof FilterSelectable) {
                        FilterSelectable selectable = (FilterSelectable)item;
                        selectable.setSelected(false);
                    }

                }
                mSelectedPosition = position;
                if (mSelectedPosition >= 0) {
                    AbsDelegate item = getDataItem(mSelectedPosition);
                    if (item instanceof FilterSelectable) {
                        FilterSelectable selectable = (FilterSelectable)item;
                        selectable.setSelected(true);
                        if (mOnFilterSelectedListener != null) {
                            mOnFilterSelectedListener.onSelected(position, selectable.getFilterId());
                        }
                    }

                }
                notifyDataSetChanged();
            }
        });
    }

    private int findCurrentSelectedOne () {
        final int length = getItemCount();
        for (int i = 0; i < length; i++) {
            AbsDelegate item = getDataItem(i);
            if (item instanceof FilterSelectable) {
                if (((FilterSelectable) item).isSelected()) {
                    return i;
                }
            }
        }
        return -1;
    }

    public long getSelectedFilterId () {
        final int length = getItemCount();
        for (int i = 0; i < length; i++) {
            AbsDelegate item = getDataItem(i);
            if (item instanceof FilterSelectable) {
                if (((FilterSelectable) item).isSelected()) {
                    return ((FilterSelectable) item).getFilterId();
                }
            }
        }
        return 0;
    }

    public void setOnFilterSelectedListener (OnFilterSelectedListener listener) {
        mOnFilterSelectedListener = listener;
    }

    public static interface FilterSelectable {
        public boolean isSelected();
        public void setSelected(boolean isSelected);
        public long getFilterId ();
    }

    public static interface OnFilterSelectedListener {
        public void onSelected (int position, long id);
    }
}

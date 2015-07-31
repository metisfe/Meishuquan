package com.metis.base.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.R;
import com.metis.base.widget.adapter.holder.FilterHolder;
import com.metis.base.widget.filter.Filter;
import com.metis.base.widget.filter.FilterProvider;

import java.util.List;

/**
 * Created by Beak on 2015/7/31.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterHolder> {

    private Context mContext = null;
    private List<FilterProvider> mFilterList = null;

    private FilterProvider mCurrentProvider = null;

    private OnFilterChangedListener mChangedListener = null;

    public FilterAdapter (Context context, List<FilterProvider> filterProviders) {
        mContext = context;
        mFilterList = filterProviders;
        if (!filterProviders.isEmpty()) {
            mCurrentProvider = mFilterList.get(0);
        }
    }

    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_filter_item, null);
        return new FilterHolder(view);
    }

    @Override
    public void onBindViewHolder(FilterHolder holder, int position) {
        final FilterProvider provider = mFilterList.get(position);
        Filter filter = provider.getFilter();
        holder.titleTv.setText(filter.getTitle());
        holder.itemView.setSelected(provider.isChecked());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (provider != mCurrentProvider) {
                    if (mCurrentProvider != null) {
                        mCurrentProvider.setChecked(false);
                    }
                    provider.setChecked(true);
                    mCurrentProvider = provider;
                    notifyDataSetChanged();
                    if (mChangedListener != null) {
                        mChangedListener.onFilterChanged(provider);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    public void setOnFilterChangedListener (OnFilterChangedListener listener) {
        mChangedListener = listener;
    }

    public static interface OnFilterChangedListener {
        public void onFilterChanged (FilterProvider provider);
    }
}

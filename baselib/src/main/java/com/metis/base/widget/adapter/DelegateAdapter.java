package com.metis.base.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.base.widget.adapter.delegate.TypeLayoutProvider;
import com.metis.base.widget.adapter.holder.AbsViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by gaoyunfei on 15/5/16.
 */
public abstract class DelegateAdapter extends RecyclerView.Adapter<AbsViewHolder> {

    private Context mContext = null;

    private List<BaseDelegate> mDataList = null;

    public DelegateAdapter(Context context/*, List<? extends AbsDelegate> dataList*/) {
        mContext = context;
        mDataList = new ArrayList<BaseDelegate>();
    }


    @Override
    public AbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //DelegateType type = DelegateType.getDelegateTypeById(viewType);
        final int layoutId = TypeLayoutProvider.getLayoutResource(viewType);
        final View view = LayoutInflater.from(mContext).inflate(layoutId, null);
        return onCreateAbsViewHolder(parent, viewType, view);
    }

    public abstract AbsViewHolder onCreateAbsViewHolder (ViewGroup parent, int viewType, View view);

    @Override
    public void onBindViewHolder(AbsViewHolder holder, int position) {
        holder.bindData(mContext, getDataItem(position), this, position);
    }

    public List<BaseDelegate> getDataList () {
        return mDataList;
    }

    public void addDataItem (BaseDelegate delegate) {
        mDataList.add(delegate);
    }

    public void addDataItem (int position, BaseDelegate delegate) {
        mDataList.add(position, delegate);
    }

    public void addDataList (Collection<? extends BaseDelegate> list) {
        mDataList.addAll(list);
    }

    public void addDataList (int position, Collection<? extends BaseDelegate> list) {
        mDataList.addAll(position, list);
    }

    public void setDataItem (int position, BaseDelegate delegate) {
        mDataList.set(position, delegate);
    }

    public AbsDelegate removeDataItem (int position) {
        return mDataList.remove(position);
    }

    public void removeDataItem (Object object) {
        mDataList.remove(object);
    }

    public void clearDataList () {
        mDataList.clear();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public AbsDelegate getDataItem (int position) {
        return mDataList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return getDataItem(position).getDelegateType();
    }

}

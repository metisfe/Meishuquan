package com.metis.meishuquan.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.meishuquan.widget.delegate.AbsDelegate;
import com.metis.meishuquan.widget.delegate.BaseDelegate;
import com.metis.meishuquan.widget.delegate.DelegateType;
import com.metis.meishuquan.widget.holder.AbsViewHolder;

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
        DelegateType type = DelegateType.getDelegateTypeById(viewType);
        final int layoutId = type.getLayoutResource();
        final View view = LayoutInflater.from(mContext).inflate(layoutId, null);
        return onCreateAbsViewHolder(parent, viewType, type, view);
//        AbsViewHolder holder = null;
//        switch (type) {
//            case STATUS_TEXT:
//                holder = new StatusHolder(view);
//                break;
//            case STATUS_SINGLE_IMAGE:
//                holder = new StatusSingleImageHolder(view);
//                break;
//            case STATUS_MULTI_IMAGE:
//                holder = new StatusMultiImageHolder(view);
//                break;
//            case STATUS_TEXT_REPOST:
//                holder = new StatusRepostHolder(view);
//                break;
//            case STATUS_SINGLE_IMAGE_REPOST:
//                holder = new StatusSingleImageRepostHolder(view);
//                break;
//            case STATUS_MULTI_IMAGE_REPOST:
//                holder = new StatusMultiImageRepostHolder(view);
//                break;
//            case COMMENT_:
//                holder = new CommentHolder(view);
//                break;
//            case SYSTEM_LIST_FOOTER:
//                holder = new ListFooterHolder(view);
//                break;
//        }
//        return holder;
    }

    public abstract AbsViewHolder onCreateAbsViewHolder (ViewGroup parent, int viewType, DelegateType delegateType, View view);

    @Override
    public void onBindViewHolder(AbsViewHolder holder, int position) {
        holder.bindData(mContext, getDataItem(position));
//        int id = getItemViewType(position);
//        AbsDelegate.DelegateType type = AbsDelegate.DelegateType.getDelegateTypeById(id);
//        switch (type) {
//            case STATUS_TEXT:
//            case STATUS_SINGLE_IMAGE:
//            case STATUS_MULTI_IMAGE:
//            case STATUS_TEXT_REPOST:
//            case STATUS_SINGLE_IMAGE_REPOST:
//            case STATUS_MULTI_IMAGE_REPOST:
//                StatusDelegate delegate = (StatusDelegate)getDataItem(position);
//                StatusHolder statusHolder = (StatusHolder)holder;
//                statusHolder.bindData(mContext, delegate);
//                break;
//            case COMMENT_:
//                CommentHolder commentHolder = (CommentHolder)holder;
//                CommentDelegate commentDelegate = (CommentDelegate)getDataItem(position);
//                commentHolder.bindData(mContext, commentDelegate);
//                break;
//            default:
//                holder.bindData(mContext, getDataItem(position));
//                break;
//        }
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
        return getDataItem(position).getDelegateType().getId();
    }

}

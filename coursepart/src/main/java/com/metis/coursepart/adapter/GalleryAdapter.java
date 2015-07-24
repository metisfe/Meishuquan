package com.metis.coursepart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.base.widget.adapter.delegate.DelegateType;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.base.widget.adapter.holder.FooterHolder;
import com.metis.coursepart.adapter.delegate.CourseDelegateType;
import com.metis.coursepart.adapter.delegate.GalleryItemDelegate;
import com.metis.coursepart.adapter.holder.GalleryItemHolder;
import com.metis.coursepart.module.GalleryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/8.
 */
public class GalleryAdapter extends DelegateAdapter {

    public GalleryAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case CourseDelegateType.ID.ID_GALLERY_ITEM:
                return new GalleryItemHolder(view);
            case DelegateType.ID.ID_FOOTER:
                return new FooterHolder(view);
        }
        throw new NullPointerException("null AbsViewHolder returnen in " + this.getClass().getSimpleName());
    }

    public GalleryItem[] getGalleryItemArray () {
        List<BaseDelegate> dataList = getDataList();
        final int length = dataList.size();
        List<GalleryItem> itemList = new ArrayList<GalleryItem>();
        for (int i = 0; i < length; i++) {
            BaseDelegate delegate = dataList.get(i);
            if (delegate instanceof GalleryItemDelegate) {
                itemList.add(((GalleryItemDelegate)delegate).getSource());
            }
        }
        GalleryItem[] itemArray = new GalleryItem[itemList.size()];
        itemList.toArray(itemArray);
        return itemArray;
    }
}

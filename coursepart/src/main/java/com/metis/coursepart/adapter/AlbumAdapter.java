package com.metis.coursepart.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.coursepart.adapter.delegate.CourseDelegateType;
import com.metis.coursepart.adapter.holder.AlbumItemSmallHolder;
import com.metis.coursepart.adapter.holder.ItemTitleHolder;

/**
 * Created by Beak on 2015/7/10.
 */
public class AlbumAdapter extends DelegateAdapter {

    public AlbumAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case CourseDelegateType.ID.ID_ALBUM_ITEM_SMALL:
                return new AlbumItemSmallHolder(view);
            case CourseDelegateType.ID.ID_ITEM_TITLE:
                return new ItemTitleHolder(view);
        }
        throw new Resources.NotFoundException("no AbsViewHolder for viewType=" + viewType);
    }
}

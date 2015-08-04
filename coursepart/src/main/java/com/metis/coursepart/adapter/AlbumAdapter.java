package com.metis.coursepart.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.DelegateType;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.base.widget.adapter.holder.FooterHolder;
import com.metis.coursepart.adapter.delegate.CourseDelegateType;
import com.metis.coursepart.adapter.holder.AlbumItemSmallHolder;
import com.metis.coursepart.adapter.holder.ItemTitleHolder;
import com.metis.coursepart.adapter.holder.UserInDetailHolder;

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
            case DelegateType.ID.ID_ITEM_TITLE:
                return new ItemTitleHolder(view);
            case CourseDelegateType.ID.ID_USER_IN_DETAIL:
                return new UserInDetailHolder(view);
            case DelegateType.ID.ID_FOOTER:
                return new FooterHolder(view);
        }
        throw new Resources.NotFoundException("no AbsViewHolder for viewType=" + viewType);
    }
}

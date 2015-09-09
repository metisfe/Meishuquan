package com.metis.newslib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.delegate.AbsDelegate;
import com.metis.base.widget.adapter.delegate.DelegateType;
import com.metis.base.widget.adapter.delegate.FooterDelegate;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.base.widget.adapter.holder.FooterHolder;
import com.metis.newslib.adapter.delegate.NewsBigDelegate;
import com.metis.newslib.adapter.delegate.NewsSmallDelegate;
import com.metis.newslib.adapter.holder.NewsBigHolder;
import com.metis.newslib.adapter.holder.NewsSmallHolder;

/**
 * Created by Beak on 2015/9/2.
 */
public class NewsAdapter extends DelegateAdapter {

    public NewsAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View view) {
        switch (viewType) {
            case NewsDelegateType.ID.ID_NEWS_BIG:
                return new NewsBigHolder(view);
            case NewsDelegateType.ID.ID_NEWS_SMALL:
                return new NewsSmallHolder(view);
            case DelegateType.ID.ID_FOOTER:
                return new FooterHolder(view);
        }
        throw new UnknownError("no viewholder found for viewType=" + viewType + " in " + getClass().getName());
    }

    public long getLastId () {
        for (int i = getItemCount() - 1; i >= 0; i--) {
            AbsDelegate delegate = getDataItem(i);
            if (delegate instanceof NewsSmallDelegate) {
                return ((NewsSmallDelegate) delegate).getSource().newsId;
            } else if (delegate instanceof NewsBigDelegate) {
                return ((NewsBigDelegate) delegate).getSource().newsId;
            } else {
                continue;
            }
        }
        return 0;
    }

    public boolean hasFooter () {
        for (int i = getItemCount() - 1; i >= 0; i--) {
            AbsDelegate delegate = getDataItem(i);
            if (delegate instanceof FooterDelegate) {
                return true;
            }
        }
        return false;
    }
}

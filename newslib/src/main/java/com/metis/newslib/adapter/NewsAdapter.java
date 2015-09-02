package com.metis.newslib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.metis.base.widget.adapter.DelegateAdapter;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
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
        }
        throw new UnknownError("no viewholder found for viewType=" + viewType + " in " + getClass().getName());
    }
}

package com.metis.base.widget.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.metis.base.module.Title;
import com.metis.base.widget.adapter.delegate.TitleDelegate;
import com.metis.base.R;
/**
 * Created by Beak on 2015/8/4.
 */
public class TitleHolder extends AbsViewHolder<TitleDelegate> {

    public TextView titleTv, subTitleTv;

    public TitleHolder(View itemView) {
        super(itemView);
        titleTv = (TextView)itemView.findViewById(R.id.title_text);
        subTitleTv = (TextView)itemView.findViewById(R.id.title_sub_text);
    }

    @Override
    public void bindData(Context context, TitleDelegate titleDelegate, RecyclerView.Adapter adapter, int position) {
        Title title = titleDelegate.getSource();
        if (title != null) {
            titleTv.setText(title.getTitle());
            subTitleTv.setText(title.getSubTitle());
            subTitleTv.setOnClickListener(title.getListener());
        }
    }
}

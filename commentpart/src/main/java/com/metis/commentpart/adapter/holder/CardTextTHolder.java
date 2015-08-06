package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.CardTextTDelegate;
import com.metis.commentpart.module.Comment;

/**
 * Created by Beak on 2015/8/5.
 */
public class CardTextTHolder extends AbsViewHolder<CardTextTDelegate> {

    public TextView textTv = null;

    public CardTextTHolder(View itemView) {
        super(itemView);
        textTv = (TextView)itemView.findViewById(R.id.t_text);
    }

    @Override
    public void bindData(Context context, CardTextTDelegate cardTextTDelegate, RecyclerView.Adapter adapter, int position) {
        Comment comment = cardTextTDelegate.getSource();
        textTv.setText(comment.content);
    }
}

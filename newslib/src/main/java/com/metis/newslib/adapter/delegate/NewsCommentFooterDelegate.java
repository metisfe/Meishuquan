package com.metis.newslib.adapter.delegate;

import android.view.View;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.newslib.adapter.NewsDelegateType;

/**
 * Created by Beak on 2015/9/11.
 */
public class NewsCommentFooterDelegate extends BaseDelegate<String> {

    private View.OnClickListener mClickListener = null;

    public NewsCommentFooterDelegate(String s) {
        super(s);
    }

    @Override
    public int getDelegateType() {
        return NewsDelegateType.TYPE_NEWS_COMMENT_FOOTER.getTypeId();
    }

    public void setOnClickListener (View.OnClickListener listener) {
        mClickListener = listener;
    }

    public View.OnClickListener getOnClickListener () {
        return mClickListener;
    }
}

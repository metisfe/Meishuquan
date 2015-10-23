package com.metis.newslib.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.manager.DisplayManager;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.newslib.R;
import com.metis.newslib.adapter.delegate.NewsDetailsImgDelegate;
import com.metis.newslib.adapter.delegate.NewsDetailsTxtDelegate;
import com.metis.newslib.module.NewsDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Beak on 2015/9/4.
 */
public class NewsDetailsTxtHolder extends AbsViewHolder<NewsDetailsTxtDelegate> {

    private static final String TAG = NewsDetailsTxtHolder.class.getSimpleName();

    private static Pattern sPattern = Pattern.compile("\\$link#\\d+\\$");

    public TextView textView;
    public NewsDetailsTxtHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.news_detail_txt);
    }

    @Override
    public void bindData(Context context, NewsDetailsTxtDelegate newsDetailsTxtDelegate, RecyclerView.Adapter adapter, int position) {
        NewsDetails.Item item = newsDetailsTxtDelegate.getSource();
        if (TextUtils.isEmpty(item.data.Content)) {
            textView.setText("");
        } else {
            String content = item.data.Content.trim();
            //SpannableString ss = new SpannableString(content);
            Matcher matcher = sPattern.matcher(content);

            int count = 0;

            while (matcher.find()) {
                count++;
            }
            Log.v(TAG, "bindData count=" + count);
            List<SpanObj> spanObjs = new ArrayList<SpanObj>();
            for (int i = 0; i < count; i++) {
                matcher = sPattern.matcher(content);
                StringBuilder builder = new StringBuilder(content);
                if (matcher.find()) {
                    String group = matcher.group();

                    NewsDetails.Url url = findUrlByFlag(newsDetailsTxtDelegate.getDetails().urls, group.replaceAll("\\$", ""));
                    Log.v(TAG, "bindData url=" + url);
                    if (url != null) {
                        int start = content.indexOf(group);
                        int end = start + group.length();
                        builder.replace(start, end, url.description);

                        SpanObj obj = new SpanObj();
                        obj.start = start;
                        obj.end = start + url.description.length();
                        obj.url = url;

                        spanObjs.add(obj);
                        content = builder.toString();
                    }
                }
            }
            Log.v(TAG, "bindData content=" + content);
            SpannableString ss = new SpannableString(content);
            final int size = spanObjs.size();
            for (int i = 0; i < size; i++) {
                SpanObj obj = spanObjs.get(i);
                ss.setSpan(new URLSpan(obj.url.dir), obj.start, obj.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            textView.setText(ss);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private NewsDetails.Url findUrlByFlag (NewsDetails.Url[] urls, String flag) {
        Log.v(TAG, "findUrlByFlag urls=" + urls);
        if (urls == null) {
            return null;
        }

        final int length = urls.length;
        for (int i = 0; i < length; i++) {
            NewsDetails.Url url = urls[i];
            Log.v(TAG, "findUrlByFlag urls=" + url);
            if (url.newShowContent != null && url.newShowContent.equals(flag)) {
                return url;
            }
        }
        return null;
    }

    private class SpanObj {
        public int start, end;
        public NewsDetails.Url url;
    }
}

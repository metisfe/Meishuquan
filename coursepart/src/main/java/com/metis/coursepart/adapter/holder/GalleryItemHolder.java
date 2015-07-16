package com.metis.coursepart.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.activity.GalleryItemDetailActivity;
import com.metis.coursepart.module.GalleryItem;
import com.metis.coursepart.adapter.delegate.GalleryItemDelegate;
import com.metis.coursepart.module.KeyWord;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Beak on 2015/7/8.
 */
public class GalleryItemHolder extends AbsViewHolder<GalleryItemDelegate> {

    public ImageView thumbIv;
    public TextView tagTv, sourceTv, countTv;

    public GalleryItemHolder(View itemView) {
        super(itemView);
        thumbIv = (ImageView)itemView.findViewById(R.id.gallery_item_thumb);
        tagTv = (TextView)itemView.findViewById(R.id.gallery_item_tag);
        sourceTv = (TextView)itemView.findViewById(R.id.gallery_item_source);
        countTv = (TextView)itemView.findViewById(R.id.gallery_item_read_count);
    }

    @Override
    public void bindData(final Context context, final GalleryItemDelegate galleryItemDelegate, RecyclerView.Adapter adapter, int position) {
        final GalleryItem item = galleryItemDelegate.getSource();
        List<KeyWord> keyWordList = item.keyWordList;
        if (keyWordList != null && !keyWordList.isEmpty()) {
            final int length = keyWordList.size();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(keyWordList.get(i).keyWordName);
            }
            tagTv.setText(builder);
        } else {
            tagTv.setText("");
        }
        User user = item.studio;
        if (user != null) {
            sourceTv.setText(user.name);
        }
        countTv.setText(context.getString(R.string.gallery_read_count, item.viewCount));
        if (galleryItemDelegate.delegateWidth >= 0 && galleryItemDelegate.delegateHeight >= 0) {
            manageLayoutParams(context, galleryItemDelegate);
        } else {
            if (item.width > 0 && item.height > 0) {
                galleryItemDelegate.autoComputeByScreen(context, item.width, item.height);
                manageLayoutParams(context, galleryItemDelegate);
            } else {
                DisplayManager.getInstance(context).display(
                        item.thumbnailUrl,
                        thumbIv,
                        DisplayManager.getInstance(context).getDefaultOptions(),
                        new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                galleryItemDelegate.autoComputeByScreen(context, bitmap.getWidth(), bitmap.getHeight());
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
            }

        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.imageDetailActivity(context, item.picId);
            }
        });

    }

    private void manageLayoutParams (Context context, GalleryItemDelegate galleryItemDelegate) {
        GalleryItem item = galleryItemDelegate.getSource();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)thumbIv.getLayoutParams();
        if (params == null) {
            params = new RelativeLayout.LayoutParams(galleryItemDelegate.delegateWidth, galleryItemDelegate.delegateHeight);
        } else {
            params.width = galleryItemDelegate.delegateWidth;
            params.height = galleryItemDelegate.delegateHeight;
        }
        thumbIv.setLayoutParams(params);
        DisplayManager.getInstance(context).display(item.thumbnailUrl, thumbIv);
    }
}

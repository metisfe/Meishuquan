package com.metis.coursepart.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.base.widget.holder.AbsViewHolder;
import com.metis.coursepart.R;
import com.metis.coursepart.activity.GalleryItemDetailActivity;
import com.metis.coursepart.module.GalleryItem;
import com.metis.coursepart.adapter.delegate.GalleryItemDelegate;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
    public void bindData(final Context context, final GalleryItemDelegate galleryItemDelegate) {
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        }
        final GalleryItem item = galleryItemDelegate.getSource();
        if (item.tags != null) {
            final int length = item.tags.size();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(item.tags.get(i));
                if (i < length - 1) {
                    sb.append(" ");
                }
            }

            tagTv.setText(context.getString(R.string.gallery_tags, sb.toString()));
        }

        sourceTv.setText(item.source);
        countTv.setText(context.getString(R.string.gallery_read_count, item.count));
        if (galleryItemDelegate.delegateWidth >= 0 && galleryItemDelegate.delegateHeight >= 0) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)thumbIv.getLayoutParams();
            if (params == null) {
                params = new RelativeLayout.LayoutParams(galleryItemDelegate.delegateWidth, galleryItemDelegate.delegateHeight);
            } else {
                params.width = galleryItemDelegate.delegateWidth;
                params.height = galleryItemDelegate.delegateHeight;
            }
            thumbIv.setLayoutParams(params);
        }

        ImageLoader.getInstance().displayImage(
                item.url,
                thumbIv,
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
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, GalleryItemDetailActivity.class));
            }
        });
    }
}

package com.metis.coursepart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.delegate.AlbumSmallDelegate;
import com.metis.coursepart.module.CourseAlbum;

/**
 * Created by Beak on 2015/7/10.
 */
public class AlbumItemSmallHolder extends AbsViewHolder<AlbumSmallDelegate> {

    public ImageView itemThumbIv, itemAuthorProfileIv, itemMoreBtn;
    public TextView itemTitleTv, itemAuthorNameTv, itemPlayCountTv;

    public AlbumItemSmallHolder(View itemView) {
        super(itemView);
        itemThumbIv = (ImageView)itemView.findViewById(R.id.item_thumb);
        itemMoreBtn = (ImageView)itemView.findViewById(R.id.item_btn_more);
        itemTitleTv = (TextView)itemView.findViewById(R.id.item_title);

        itemAuthorNameTv = (TextView)itemView.findViewById(R.id.item_author_name);
        itemAuthorProfileIv = (ImageView)itemView.findViewById(R.id.item_author_profile);
        itemPlayCountTv = (TextView)itemView.findViewById(R.id.item_play_count);
    }

    @Override
    public void bindData(final Context context, AlbumSmallDelegate videoAlbumDelegate, RecyclerView.Adapter adapter, int position) {
        final CourseAlbum album = videoAlbumDelegate.getSource();
        final User studioInfo = album.studio;
        DisplayManager.getInstance(context).display(album.coursePic, itemThumbIv);
        itemTitleTv.setText(album.title);
        if (studioInfo != null) {
            DisplayManager.getInstance(context).display(studioInfo.avatar, itemAuthorProfileIv);
            itemAuthorNameTv.setText(studioInfo.name);
            /*View.OnClickListener userListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    com.metis.base.ActivityDispatcher.userActivity(context, studioInfo.userId);
                }
            };
            itemAuthorProfileIv.setOnClickListener(userListener);
            itemAuthorNameTv.setOnClickListener(userListener);*/
        } else {
            /*itemAuthorProfileIv.setOnClickListener(null);
            itemAuthorNameTv.setOnClickListener(null);*/
        }

        itemPlayCountTv.setText(context.getString(R.string.course_play_count, album.viewCount));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.videoDetailActivity(context, album);
            }
        });
        if (position == 0) {
            itemView.setBackgroundResource(R.drawable.video_item_bg_small_sel_top);
        } else if (position == adapter.getItemCount() - 1) {
            itemView.setBackgroundResource(R.drawable.video_item_bg_small_sel_bottom);
        } else {
            itemView.setBackgroundResource(R.drawable.video_item_bg_small_sel);
        }
    }
}

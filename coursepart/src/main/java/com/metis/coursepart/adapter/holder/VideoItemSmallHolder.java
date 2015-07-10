package com.metis.coursepart.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.base.manager.DisplayManager;
import com.metis.base.widget.holder.AbsViewHolder;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.delegate.VideoAlbumDelegate;
import com.metis.coursepart.module.CourseAlbum;
import com.metis.coursepart.module.StudioInfo;

/**
 * Created by Beak on 2015/7/10.
 */
public class VideoItemSmallHolder extends AbsViewHolder<VideoAlbumDelegate> {

    public ImageView itemThumbIv, itemAuthorProfileIv, itemMoreBtn;
    public TextView itemTitleTv, itemAuthorNameTv, itemTimeTv;

    public VideoItemSmallHolder(View itemView) {
        super(itemView);
        itemThumbIv = (ImageView)itemView.findViewById(R.id.item_thumb);
        itemMoreBtn = (ImageView)itemView.findViewById(R.id.item_btn_more);
        itemTitleTv = (TextView)itemView.findViewById(R.id.item_title);

        itemAuthorNameTv = (TextView)itemView.findViewById(R.id.item_author_name);
        itemAuthorProfileIv = (ImageView)itemView.findViewById(R.id.item_author_profile);
    }

    @Override
    public void bindData(final Context context, VideoAlbumDelegate videoAlbumDelegate) {
        final CourseAlbum album = videoAlbumDelegate.getSource();
        StudioInfo studioInfo = album.studio;
        DisplayManager.getInstance(context).display(album.coursePic, itemThumbIv);
        itemTitleTv.setText(album.title);
        if (studioInfo != null) {
            DisplayManager.getInstance(context).display(studioInfo.avatar, itemAuthorProfileIv);
            itemAuthorNameTv.setText(studioInfo.name);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.videoDetailActivity(context, album.courseId);
            }
        });
    }
}

package com.metis.coursepart.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.base.manager.DisplayManager;
import com.metis.base.widget.holder.AbsViewHolder;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.activity.CourseVideoDetailActivity;
import com.metis.coursepart.adapter.VideoSubAdapter;
import com.metis.coursepart.adapter.decoration.VideoItemSmallDecoration;
import com.metis.coursepart.adapter.delegate.VideoAlbumDelegate;
import com.metis.coursepart.adapter.delegate.VideoItemDelegate;
import com.metis.coursepart.adapter.delegate.VideoItemTitleDelegate;
import com.metis.coursepart.module.Author;
import com.metis.coursepart.module.CourseAlbum;
import com.metis.coursepart.module.StudioInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/10.
 */
public class VideoItemHolder extends AbsViewHolder<VideoItemDelegate> {

    private static final String TAG = VideoItemHolder.class.getSimpleName();

    private static final int MARGIN_BOTTOM_IN_DP = 8;

    public View itemBigView;
    public ImageView itemBigThumbIv, itemBigAuthorProfileIv, itemBigMoreBtn;
    public TextView itemBigTitleTv, itemBigAuthorNameTv;
    public RecyclerView itemRv;

    public VideoItemHolder(View itemView) {
        super(itemView);
        itemBigView = itemView.findViewById(R.id.item_container_big);
        itemBigThumbIv = (ImageView)itemView.findViewById(R.id.item_big_thumb);
        itemBigAuthorProfileIv = (ImageView)itemView.findViewById(R.id.item_big_author_profile);
        itemBigMoreBtn = (ImageView)itemView.findViewById(R.id.item_big_btn_more);
        itemBigTitleTv = (TextView)itemView.findViewById(R.id.item_big_title);
        itemBigAuthorNameTv = (TextView)itemView.findViewById(R.id.item_big_author_name);

        itemRv = (RecyclerView)itemView.findViewById(R.id.item_container_recycler_view);
        itemRv.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        itemRv.addItemDecoration(new VideoItemSmallDecoration());
    }

    @Override
    public void bindData(final Context context, VideoItemDelegate videoItemDelegate) {
        List<CourseAlbum> albumList = videoItemDelegate.getSource();
        if (!albumList.isEmpty()) {
            final CourseAlbum firstOne = albumList.get(0);
            StudioInfo studioInfo = firstOne.studio;
            DisplayManager.getInstance(context).display(firstOne.coursePic, itemBigThumbIv);
            DisplayManager.getInstance(context).display(studioInfo.avatar, itemBigAuthorProfileIv);
            itemBigTitleTv.setText(firstOne.title);
            itemBigAuthorNameTv.setText(studioInfo.name);
            itemBigView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDispatcher.videoDetailActivity(context, firstOne.courseId);
                }
            });

            List<CourseAlbum> subAlbumList = albumList.subList(1, albumList.size());
            if (subAlbumList != null && !subAlbumList.isEmpty()) {
                VideoSubAdapter subAdapter = videoItemDelegate.getSubAdapter();
                if (subAdapter == null) {
                    subAdapter = new VideoSubAdapter(context);
                    videoItemDelegate.setSubAdapter(subAdapter);
                }
                itemRv.setAdapter(subAdapter);
                List<VideoAlbumDelegate> albumDelegates = new ArrayList<VideoAlbumDelegate>();
                final int length = subAlbumList.size();
                for (int i = 0; i < length; i++) {
                    albumDelegates.add(new VideoAlbumDelegate(subAlbumList.get(i)));
                }
                int rvHeight = videoItemDelegate.getSubRecyclerViewHeight();
                if (rvHeight <= 0) {
                    Resources resources = context.getResources();
                    final int itemHeight = resources.getDimensionPixelSize(R.dimen.video_item_small_height);
                    final int titleHeight = resources.getDimensionPixelSize(R.dimen.video_item_title_height);
                    final int bottom_margin = (int)(resources.getDisplayMetrics().density * MARGIN_BOTTOM_IN_DP);
                    final int maxHeight = 3 * itemHeight + titleHeight + bottom_margin;
                    final int measureHeight = length * itemHeight + titleHeight + bottom_margin;
                    rvHeight = Math.min(maxHeight, measureHeight);
                    videoItemDelegate.setSubRecyclerViewHeight(rvHeight);
                }

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)itemRv.getLayoutParams();
                if (params == null) {
                    params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, rvHeight);
                } else {
                    params.height = rvHeight;
                }
                itemRv.setLayoutParams(params);
                subAdapter.addDataItem(new VideoItemTitleDelegate(firstOne.getChannel()));
                subAdapter.addDataList(albumDelegates);
                subAdapter.notifyDataSetChanged();
            }
        }
    }
}

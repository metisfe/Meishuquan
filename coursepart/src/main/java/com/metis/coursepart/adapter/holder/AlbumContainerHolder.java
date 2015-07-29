package com.metis.coursepart.adapter.holder;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.AlbumAdapter;
import com.metis.coursepart.adapter.decoration.VideoItemSmallDecoration;
import com.metis.coursepart.adapter.delegate.AlbumSmallDelegate;
import com.metis.coursepart.adapter.delegate.AlbumContainerDelegate;
import com.metis.coursepart.adapter.delegate.ItemTitleDelegate;
import com.metis.coursepart.module.CourseAlbum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/10.
 */
public class AlbumContainerHolder extends AbsViewHolder<AlbumContainerDelegate> {

    private static final String TAG = AlbumContainerHolder.class.getSimpleName();

    private static final int MARGIN_BOTTOM_IN_DP = 8;
    private static final int MAX_FOLLOW_ITEM_COUNT = 3;
    //private static final boolean SHOW_ALL = false;

    public View itemBigView;
    public ImageView itemBigThumbIv, itemBigAuthorProfileIv, itemBigMoreBtn;
    public TextView itemBigTitleTv, itemBigAuthorNameTv, itemBigCountTv;
    public RecyclerView itemRv;

    public AlbumContainerHolder(View itemView) {
        super(itemView);
        itemBigView = itemView.findViewById(R.id.item_container_big);
        itemBigThumbIv = (ImageView)itemView.findViewById(R.id.item_big_thumb);
        itemBigAuthorProfileIv = (ImageView)itemView.findViewById(R.id.item_big_author_profile);
        itemBigMoreBtn = (ImageView)itemView.findViewById(R.id.item_big_btn_more);
        itemBigTitleTv = (TextView)itemView.findViewById(R.id.item_big_title);
        itemBigAuthorNameTv = (TextView)itemView.findViewById(R.id.item_big_author_name);
        itemBigCountTv = (TextView)itemView.findViewById(R.id.item_big_play_count);

        itemRv = (RecyclerView)itemView.findViewById(R.id.item_container_recycler_view);
        itemRv.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        itemRv.addItemDecoration(new VideoItemSmallDecoration());
    }

    @Override
    public void bindData(final Context context, AlbumContainerDelegate videoItemDelegate, RecyclerView.Adapter adapter, int position) {
        List<CourseAlbum> albumList = videoItemDelegate.getSource();
        if (!albumList.isEmpty()) {
            final CourseAlbum firstOne = albumList.get(0);
            final User studioInfo = firstOne.studio;
            DisplayManager.getInstance(context).display(firstOne.coursePic, itemBigThumbIv);
            itemBigTitleTv.setText(firstOne.title);
            itemBigCountTv.setText(context.getString(R.string.course_play_count, firstOne.viewCount));
            if (studioInfo != null) {
                DisplayManager.getInstance(context).display(studioInfo.avatar, itemBigAuthorProfileIv);
                itemBigAuthorNameTv.setText(studioInfo.name);
                /*View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        com.metis.base.ActivityDispatcher.userActivity(context, studioInfo.userId);
                    }
                };
                itemBigAuthorProfileIv.setOnClickListener(listener);
                itemBigAuthorNameTv.setOnClickListener(listener);*/
            } else {
                itemBigAuthorNameTv.setText("");
                itemBigAuthorProfileIv.setOnClickListener(null);
                itemBigAuthorNameTv.setOnClickListener(null);
            }

            itemBigView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDispatcher.videoDetailActivity(context, firstOne);
                }
            });

            List<CourseAlbum> subAlbumList = albumList.subList(1, albumList.size());
            if (subAlbumList != null && !subAlbumList.isEmpty()) {
                AlbumAdapter subAdapter = videoItemDelegate.getSubAdapter();
                if (subAdapter == null) {
                    subAdapter = new AlbumAdapter(context);
                    videoItemDelegate.setSubAdapter(subAdapter);
                }
                itemRv.setAdapter(subAdapter);
                List<AlbumSmallDelegate> albumDelegates = new ArrayList<AlbumSmallDelegate>();
                final int length = subAlbumList.size();
                for (int i = 0; i < length; i++) {
                    albumDelegates.add(new AlbumSmallDelegate(subAlbumList.get(i)));
                }
                int rvHeight = videoItemDelegate.getSubRecyclerViewHeight();
                if (rvHeight <= 0) {
                    Resources resources = context.getResources();
                    final int itemHeight = resources.getDimensionPixelSize(R.dimen.video_item_small_height);
                    final int titleHeight = resources.getDimensionPixelSize(R.dimen.video_item_title_height);
                    final int bottom_margin = (int)(resources.getDisplayMetrics().density * MARGIN_BOTTOM_IN_DP);
                    final int maxHeight = MAX_FOLLOW_ITEM_COUNT * itemHeight + titleHeight + bottom_margin;
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
                ItemTitleDelegate titleDelegate = new ItemTitleDelegate(firstOne.getChannel());
                titleDelegate.setClickable(true);
                titleDelegate.setFilterId(videoItemDelegate.getFilterId());
                subAdapter.addDataItem(titleDelegate);
                subAdapter.addDataList(albumDelegates);
                subAdapter.notifyDataSetChanged();
            }
        }
    }

}

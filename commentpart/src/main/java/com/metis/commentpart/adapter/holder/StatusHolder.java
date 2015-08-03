package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.module.UserMark;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.ActivityDispatcher;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.FlipperAdapter;
import com.metis.commentpart.adapter.delegate.StatusDelegate;
import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.Status;
import com.metis.commentpart.widget.ViewFlippable;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Beak on 2015/7/28.
 */
public class StatusHolder extends AbsViewHolder<StatusDelegate> implements ViewFlippable{

    public ImageView statusProfileIv, statusThumbIv;
    public TextView nameTv, locationTv, categoryTv,
            stateTv, contentTv, quickCommentBtn;
    public TextView thumbUpCountTv, commentCountTv;
    public AdapterViewFlipper adapterViewFlipper = null;

    public View thumbUpBtn;

    public StatusHolder(View itemView) {
        super(itemView);
        statusProfileIv = (ImageView)itemView.findViewById(R.id.status_profile);
        nameTv = (TextView)itemView.findViewById(R.id.status_name);
        locationTv = (TextView)itemView.findViewById(R.id.status_location);
        categoryTv = (TextView)itemView.findViewById(R.id.status_category);
        stateTv = (TextView)itemView.findViewById(R.id.status_state);
        contentTv = (TextView)itemView.findViewById(R.id.status_text);
        statusThumbIv = (ImageView)itemView.findViewById(R.id.status_thumb);
        thumbUpBtn = itemView.findViewById(R.id.status_thumb_up_btn);
        thumbUpCountTv = (TextView)itemView.findViewById(R.id.status_thumb_up_count);
        commentCountTv = (TextView)itemView.findViewById(R.id.status_comment_count);
        quickCommentBtn = (TextView)itemView.findViewById(R.id.status_quick_comment);
        adapterViewFlipper = (AdapterViewFlipper)itemView.findViewById(R.id.status_flipper);
    }

    @Override
    public void bindData(final Context context, StatusDelegate statusDelegate, RecyclerView.Adapter adapter, int position) {
        final Status status = statusDelegate.getSource();
        final User user = status.user;
        if (user != null) {
            DisplayManager.getInstance(context).display(user.avatar, statusProfileIv);
            nameTv.setText(user.name);
            statusProfileIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    com.metis.base.ActivityDispatcher.userActivity(context, user.userId);
                }
            });
        }

        contentTv.setText(status.desc);
        thumbUpCountTv.setText("" + status.supportCount);
        commentCountTv.setText("" + status.commentCount);
        /*switch (status.)
        stateTv.setText();*/
        if (status.img != null) {
            statusThumbIv.setVisibility(View.VISIBLE);

            if (statusDelegate.getItemHeight() <= 0) {
                int margin = context.getResources().getDimensionPixelSize(R.dimen.status_item_decoration_offset);
                int itemWidth = context.getResources().getDisplayMetrics().widthPixels - 2 * margin;
                statusDelegate.setItemHeight(itemWidth);
            }
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)statusThumbIv.getLayoutParams();
            if (params == null) {
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, statusDelegate.getItemHeight());
                statusThumbIv.setLayoutParams(params);
            } else {
                if (params.height != statusDelegate.getItemHeight()) {
                    params.width = RecyclerView.LayoutParams.FILL_PARENT;
                    params.height = statusDelegate.getItemHeight();
                    statusThumbIv.setLayoutParams(params);
                }
            }

            DisplayManager.getInstance(context).display(status.img.imgThumbnailUrl, statusThumbIv);
            statusThumbIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    com.metis.base.ActivityDispatcher.imagePreviewActivity(context, status.img);
                }
            });
        } else {
            statusThumbIv.setVisibility(View.GONE);
            statusThumbIv.setOnClickListener(null);
        }

        UserMark mark = status.userMark;
        if (mark != null) {
            thumbUpBtn.setSelected(mark.isSupport);
        } else {
            thumbUpBtn.setSelected(false);
        }

        List<Comment> commentList = statusDelegate.getSource().teacherCommentList;
        if (commentList != null && !commentList.isEmpty()) {
            adapterViewFlipper.setVisibility(View.VISIBLE);
            adapterViewFlipper.setAdapter(new FlipperAdapter(context, commentList));
            adapterViewFlipper.startFlipping();
        } else {
            adapterViewFlipper.setVisibility(View.GONE);
            adapterViewFlipper.setAdapter(null);
            adapterViewFlipper.stopFlipping();
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.statusDetail(context);
            }
        });
    }

    @Override
    public AdapterViewFlipper getViewFlipper() {
        return adapterViewFlipper;
    }
}

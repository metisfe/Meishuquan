package com.metis.commentpart.adapter.holder;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.metis.base.manager.CacheManager;
import com.metis.base.manager.VoiceManager;
import com.metis.base.utils.Log;
import com.metis.base.utils.TimeUtils;
import com.metis.base.widget.adapter.holder.AbsViewHolder;
import com.metis.commentpart.R;
import com.metis.commentpart.adapter.delegate.CardVoiceTDelegate;
import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.CommentAttachment;

import java.io.File;

/**
 * Created by Beak on 2015/8/10.
 */
public class CardVoiceTHolder extends AbsViewHolder<CardVoiceTDelegate> implements VoiceManager.OnPlayListener {

    public ImageView voiceIv = null;
    public ViewGroup voiceContainer = null;
    public TextView voiceDurationTv = null;
    private VoiceManager mVoiceManager = null;
    private AnimationDrawable mAnimDrawable = null;

    public CardVoiceTHolder(View itemView) {
        super(itemView);
        voiceIv = (ImageView)itemView.findViewById(R.id.voice_iv);
        voiceContainer = (ViewGroup)itemView.findViewById(R.id.voice_container);
        voiceDurationTv = (TextView)itemView.findViewById(R.id.voice_duration);

        mAnimDrawable = (AnimationDrawable)voiceIv.getDrawable();
    }

    @Override
    public void bindData(final Context context, CardVoiceTDelegate cardVoiceTDelegate, RecyclerView.Adapter adapter, int position) {
        final Comment comment = cardVoiceTDelegate.getSource();
        CommentAttachment attachment = cardVoiceTDelegate.getSource().imgOrVoiceUrl;
        Resources resources = context.getResources();
        final int min = resources.getDimensionPixelSize(R.dimen.voice_min_width);
        final int max = resources.getDimensionPixelSize(R.dimen.voice_max_width);
        int width = min;
        if (attachment != null) {
            int duration = attachment.voiceLength;
            width = min + duration / 100;
            width = Math.min(width, max);
            voiceDurationTv.setText(TimeUtils.format(duration));
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)voiceContainer.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = width;
        } else {
            layoutParams = new RelativeLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT);
        }
        voiceContainer.setLayoutParams(layoutParams);
        voiceContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVoiceManager = VoiceManager.getInstance(context);
                HttpUtils utils = new HttpUtils(10 * 1000);
                utils.download(comment.imgOrVoiceUrl.voiceUrl, CacheManager.getInstance(context).getMyVoiceCacheDir().getAbsolutePath() + File.separator + "a.mp3", true, true, new RequestCallBack<File>() {
                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        if (mVoiceManager.isPlaying()) {
                            mVoiceManager.stopPlay();
                        }
                        if (!mVoiceManager.isPlaying()) {
                            mVoiceManager.setOnPlayListener(CardVoiceTHolder.this);
                            mVoiceManager.startPlay(responseInfo.result.getAbsolutePath());
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });
                /*AnimationDrawable drawable = (AnimationDrawable)voiceIv.getDrawable();
                drawable.start();*/
            }
        });
    }

    @Override
    public void onPlayStart(MediaPlayer player) {
        mAnimDrawable.start();
    }

    @Override
    public void onPlaying(String path, MediaPlayer player, long position) {

    }

    @Override
    public void onPlayStop() {
        mAnimDrawable.stop();
        if (mVoiceManager != null) {
            mVoiceManager.setOnPlayListener(null);
        }
    }
}

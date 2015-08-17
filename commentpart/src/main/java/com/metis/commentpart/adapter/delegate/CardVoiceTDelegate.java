package com.metis.commentpart.adapter.delegate;

import android.media.MediaPlayer;

import com.metis.base.manager.VoiceManager;
import com.metis.base.utils.Log;
import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.commentpart.adapter.holder.CardVoiceTHolder;
import com.metis.commentpart.module.Comment;

/**
 * Created by Beak on 2015/8/10.
 */
public class CardVoiceTDelegate extends BaseDelegate<Comment> implements VoiceManager.OnPlayListener{

    private static final String TAG = CardVoiceTHolder.class.getSimpleName();

    private boolean isPlaying = false;

    private OnNeedUpdateCallback mNeedUpdateCallback = null;

    public CardVoiceTDelegate(Comment comment) {
        super(comment);
    }

    @Override
    public int getDelegateType() {
        return CommentDelegateType.TYPE_COMMENT_CARD_VOICE_T.getType();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    @Override
    public void onPlayStart(VoiceManager voiceManager, MediaPlayer player) {
        isPlaying = true;
        if (mNeedUpdateCallback != null) {
            mNeedUpdateCallback.onNeed(true);
        }
    }

    @Override
    public void onPlaying(VoiceManager voiceManager, String targetPath, MediaPlayer mp, long position) {

    }

    @Override
    public void onPlayStop(VoiceManager voiceManager) {
        isPlaying = false;
        Log.v(TAG, TAG + " onPlayStop");
        voiceManager.setOnPlayListener(null);
        if (mNeedUpdateCallback != null) {
            mNeedUpdateCallback.onNeed(false);
        }
    }

    public void setOnNeedUpdateCallback (OnNeedUpdateCallback callback) {
        mNeedUpdateCallback = callback;
    }

    public static interface OnNeedUpdateCallback {
        public void onNeed (boolean started);
    }
}

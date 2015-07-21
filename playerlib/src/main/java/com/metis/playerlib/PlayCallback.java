package com.metis.playerlib;

import com.baidu.cyberplayer.core.BVideoView;

public interface PlayCallback {
        public void onStarted (BVideoView bVideoView);
        public void onPaused (BVideoView bVideoView);
        public void onResumed (BVideoView bVideoView);
        public void onCompleted (BVideoView bVideoView);
        public boolean onError (BVideoView bVideoView, int what, int extra);
}
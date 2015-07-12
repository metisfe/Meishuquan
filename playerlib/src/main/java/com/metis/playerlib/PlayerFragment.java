package com.metis.playerlib;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.utils.VersionManager;

import java.io.File;

/**
 * Created by gaoyunfei on 15/7/11.
 */
public class PlayerFragment extends Fragment
        implements BVideoView.OnPreparedListener,BVideoView.OnCompletionListener,
        BVideoView.OnErrorListener, BVideoView.OnInfoListener, BVideoView.OnPlayingBufferCacheListener {

    private static final String TAG = PlayerFragment.class.getSimpleName();

    private BVideoView mBvv = null;

    static {
        //BVideoView.setNativeLibsDirectory("/data/data/com.metis.meishuquan/files");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String cpuVersion = VersionManager.getInstance().getCurrentVersion();
        Log.v(TAG, "cpuVersion=" + cpuVersion);
        return inflater.inflate(R.layout.fragment_player, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBvv = (BVideoView)view.findViewById(R.id.player_view);

        mBvv.setAKSK("Rv72t6Icg15WKGSO", "suDyWZaHXltatWvG");
        //mBvv.setAKSK("lxKtIjyHewUpBcUa", "9fUZnbBO9mijcnri");

        mBvv.setOnPreparedListener(this);
        mBvv.setOnCompletionListener(this);
        mBvv.setOnErrorListener(this);
        mBvv.setOnInfoListener(this);
        mBvv.setOnPlayingBufferCacheListener(this);

        mBvv.setDecodeMode(BVideoView.DECODE_SW);
        mBvv.setCacheBufferSize(1 * 1024 * 1024);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Movies" + File.separator + "TR.mp4";
        File file = new File (path);
        Log.v(TAG, "file is exist = " + file.exists());
        mBvv.setVideoPath(Uri.parse(path).toString());
        mBvv.showCacheInfo(true);

        mBvv.start();
    }

    @Override
    public void onCompletion() {

    }

    @Override
    public boolean onError(int i, int i1) {
        return false;
    }

    @Override
    public boolean onInfo(int i, int i1) {
        return false;
    }

    @Override
    public void onPlayingBufferCache(int i) {

    }

    @Override
    public void onPrepared() {

    }
}

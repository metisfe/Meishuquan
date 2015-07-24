package com.metis.commentpart.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.metis.base.fragment.DockFragment;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.User;
import com.metis.base.widget.dock.DockBar;
import com.metis.commentpart.R;
import com.metis.commentpart.activity.CommentDetailActivity;
import com.metis.msnetworklib.contract.ReturnInfo;

/**
 * Created by Beak on 2015/7/24.
 */
public class CommentTabFragment extends DockFragment {

    private DockBar.Dock mDock = null;

    private Button mBtn = null;

    @Override
    public DockBar.Dock getDock(Context context) {
        if (mDock == null) {
            mDock = new DockBar.Dock(context, 1, android.R.drawable.btn_star, android.R.string.copyUrl, this);
        }
        return mDock;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment_tab, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtn = (Button)view.findViewById(R.id.comment_tv);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CommentDetailActivity.class));
                /*AccountManager.getInstance(getActivity()).login("15701218034", "12345678", new RequestCallback<User>() {
                    @Override
                    public void callback(ReturnInfo<User> returnInfo, String callbackId) {

                    }
                });*/
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}

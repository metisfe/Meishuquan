package com.metis.base.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.base.ActivityDispatcher;
import com.metis.base.R;
import com.metis.base.activity.CacheActivity;
import com.metis.base.activity.FeedbackActivity;
import com.metis.base.activity.SettingActivity;
import com.metis.base.activity.SubscriptionActivity;
import com.metis.base.activity.VipActivity;
import com.metis.base.manager.AccountManager;
import com.metis.base.manager.DisplayManager;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.base.widget.IconTextView;
import com.metis.base.widget.TitleBar;
import com.metis.base.widget.adapter.MeAdapter;
import com.metis.base.widget.adapter.delegate.MeHeaderDelegate;
import com.metis.base.widget.dock.DockBar;
import com.pgyersdk.feedback.PgyFeedback;

/**
 * Created by Beak on 2015/8/24.
 */
public class MeTabFragment extends DockFragment implements AccountManager.OnUserChangeListener, View.OnClickListener{

    private static final String TAG = MeTabFragment.class.getSimpleName();

    private DockBar.Dock mDock = null;

    private TitleBar mTitleBar = null;

    private RelativeLayout mProfileLayout;
    private ImageView mProfileIv = null;
    private TextView mNameTv = null;
    private TextView mFollowsTv = null, mFansTv = null;
    private IconTextView mCollectionItv, mCacheItv, mSubscriptionItv, mVipItv, mFeedbackItv, mSettingItv;
    private View mErrorTv = null;

    private HomePageFragment mHomePageFragment = null;

    private User mMe = null;

    @Override
    public DockBar.Dock getDock(Context context) {
        if (mDock == null) {
            mDock = new DockBar.Dock(context, R.id.dock_item_id_me, R.drawable.ic_me_sel, R.string.dock_item_me_title, this);
        }
        return mDock;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleBar = (TitleBar)view.findViewById(R.id.me_title_bar);
        mTitleBar.setTitleCenter(R.string.text_title_me);

        mErrorTv = view.findViewById(R.id.me_home_page_error_tip);

        mProfileLayout = (RelativeLayout)view.findViewById(R.id.me_profile_container);
        mProfileIv = (ImageView)view.findViewById(R.id.me_profile);
        mNameTv = (TextView)view.findViewById(R.id.me_name);
        mFollowsTv = (TextView)view.findViewById(R.id.me_extras_follows);
        mFansTv = (TextView)view.findViewById(R.id.me_extras_fans);

        mCollectionItv = (IconTextView)view.findViewById(R.id.me_collection);
        mCacheItv = (IconTextView)view.findViewById(R.id.me_cache);
        mSubscriptionItv = (IconTextView)view.findViewById(R.id.me_subscription);
        mVipItv = (IconTextView)view.findViewById(R.id.me_vip);
        mFeedbackItv = (IconTextView)view.findViewById(R.id.me_feedback);
        mSettingItv = (IconTextView)view.findViewById(R.id.me_setting);

        mSettingItv.setOnClickListener(this);

        mHomePageFragment = (HomePageFragment)getChildFragmentManager().findFragmentById(R.id.me_home_page_fragment);
        mHomePageFragment.setUser(AccountManager.getInstance(getActivity()).getMe());
        AccountManager.getInstance(getActivity()).registerOnUserChangeListener(this);

        final User me = AccountManager.getInstance(getContext()).getMe();
        if (me == null) {
            ActivityDispatcher.loginActivityWhenAlreadyIn(getContext());
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        final User me = AccountManager.getInstance(getContext()).getMe();
        if (me == null) {
            mErrorTv.setVisibility(View.VISIBLE);
            mErrorTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDispatcher.loginActivityWhenAlreadyIn(getContext());
                }
            });
            return;
        }
        mErrorTv.setVisibility(View.GONE);
        mErrorTv.setOnClickListener(null);
        setMe(me);
    }

    private void setMe (final User me) {
        if (me != null) {
            mMe = me;
            DisplayManager.getInstance(getContext()).display(me.getAvailableAvatar(), mProfileIv,
                    DisplayManager.getInstance(getContext()).makeRoundDisplayImageOptions(
                            getContext().getResources().getDimensionPixelSize(R.dimen.profile_size_big)
                    ));
            mProfileLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDispatcher.userActivity(getContext(), me);
                }
            });
            mNameTv.setText(me.name);
            mFollowsTv.setText(getString(R.string.text_follows_count, me.focusNum));
            mFansTv.setText(getString(R.string.text_fans_count, me.fansNum));

            mFollowsTv.setOnClickListener(this);
            mFansTv.setOnClickListener(this);

            mCollectionItv.setOnClickListener(this);
            mCacheItv.setOnClickListener(this);
            mSubscriptionItv.setOnClickListener(this);
            mVipItv.setOnClickListener(this);
            mFeedbackItv.setOnClickListener(this);
            mSettingItv.setOnClickListener(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AccountManager.getInstance(getActivity()).unregisterOnUserChangeListener(this);
    }

    @Override
    public void onUserChanged(User user, boolean onLine) {
        //Toast.makeText(getActivity(), "onUserChanged", Toast.LENGTH_SHORT).show();
        mHomePageFragment.setUser(AccountManager.getInstance(getActivity()).getMe());
    }

    @Override
    public void onUserInfoChanged(User user) {
        Log.v(TAG, "onUserInfoChanged " + user.userId);
        setMe(user);
    }

    @Override
    public void onUserLogout() {

    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == mFollowsTv.getId()) {
            ActivityDispatcher.followsActivity(getContext(), mMe.userId);
        } else if (id == mFansTv.getId()) {
            ActivityDispatcher.fansActivity(getContext(), mMe.userId);
        } else if (id == mCollectionItv.getId()) {

        } else if (id == mCacheItv.getId()) {
            startActivity(new Intent(getContext(), CacheActivity.class));
        } else if (id == mSubscriptionItv.getId()) {
            startActivity(new Intent(getContext(), SubscriptionActivity.class));
        } else if (id == mVipItv.getId()) {
            startActivity(new Intent(getContext(), VipActivity.class));
        } else if (id == mSettingItv.getId()) {
            startActivity(new Intent(getContext(), SettingActivity.class));
        } else if (id == mFeedbackItv.getId()) {
            //startActivity(new Intent(getContext(), FeedbackActivity.class));
            // 以对话框的形式弹出
            PgyFeedback.getInstance().showDialog(getContext());

            // 以Activity的形式打开，这种情况下必须在AndroidManifest.xml配置FeedbackActivity
            // 打开沉浸式,默认为false
            // FeedbackActivity.setBarImmersive(true);
            //PgyFeedback.getInstance().showActiivty(MainActivity.this);
        }/* else if (id == mSetttingItv.getId()) {

        }*/
    }
}

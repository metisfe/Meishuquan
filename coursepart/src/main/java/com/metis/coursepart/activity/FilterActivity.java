package com.metis.coursepart.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.metis.base.activity.TitleBarActivity;
import com.metis.base.manager.RequestCallback;
import com.metis.base.utils.FragmentUtils;
import com.metis.base.utils.Log;
import com.metis.coursepart.ActivityDispatcher;
import com.metis.coursepart.R;
import com.metis.coursepart.fragment.FilterPanelFragment;
import com.metis.coursepart.fragment.GalleryFilterFragment;
import com.metis.coursepart.fragment.VideoFilterFragment;
import com.metis.coursepart.manager.CourseManager;
import com.metis.coursepart.module.CourseChannelList;
import com.metis.coursepart.module.CourseType;
import com.metis.coursepart.module.StudioInfo;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.List;

public class FilterActivity extends TitleBarActivity implements RadioGroup.OnCheckedChangeListener{

    private static final String TAG = FilterActivity.class.getSimpleName();

    private RadioButton mVideoBtn, mGalleryBtn;

    private VideoFilterFragment mVideoFilterFragment = new VideoFilterFragment();
    private GalleryFilterFragment mGalleryFilterFragment = new GalleryFilterFragment();

    private long mState = -1;

    private String mAction = null;

    private boolean isDestroyed = false;

    private Fragment mCurrentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mAction = getIntent().getAction();
        RadioGroup switchView = (RadioGroup) LayoutInflater.from(this).inflate(R.layout.layout_tab_switch, null);
        getTitleBar().setCenterView(switchView);

        mVideoBtn = (RadioButton)switchView.findViewById(R.id.tab_video);
        mGalleryBtn = (RadioButton)switchView.findViewById(R.id.tab_gallery);

        switchView.setOnCheckedChangeListener(this);

        if (ActivityDispatcher.ACTION_VIDEO_FILTER.equals(mAction)) {
            mVideoBtn.setChecked(true);
            /*FragmentUtils.showFragment(getSupportFragmentManager(), mVideoFilterFragment, R.id.filter_fragment_container);*/
        } else if (ActivityDispatcher.ACTION_GALLERY_FILTER.equals(mAction)) {
            mGalleryBtn.setChecked(true);
            /*FragmentUtils.showFragment(getSupportFragmentManager(), mGalleryFilterFragment, R.id.filter_fragment_container);*/
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mState = getIntent().getLongExtra(ActivityDispatcher.KEY_STATE_FILTER_ID, 1);
        mVideoFilterFragment.setCurrentState(mState);
        CourseManager.getInstance(this).getCourseChannelList(new RequestCallback<CourseChannelList>() {
            @Override
            public void callback(ReturnInfo<CourseChannelList> returnInfo, String callbackId) {
                if (isDestroyed) {
                    return;
                }
                if (returnInfo.isSuccess()) {
                    CourseChannelList channelList = returnInfo.getData();
                    List<StudioInfo> studioList = channelList.coursestuido;
                    List<CourseType> courseTypeList =  channelList.courseType;

                    mVideoFilterFragment.setCourseTypeList(courseTypeList);
                    mVideoFilterFragment.setStudioList(studioList);

                    mGalleryFilterFragment.setCourseTypeList(courseTypeList);
                    mGalleryFilterFragment.setStudioList(studioList);

                    if (ActivityDispatcher.ACTION_VIDEO_FILTER.equals(mAction)) {
                        FilterPanelFragment panelFragment = mVideoFilterFragment.getFilterPanelFragment();
                        mVideoFilterFragment.loadData(
                                panelFragment.getCurrentState(),
                                panelFragment.getCurrentCategory(),
                                panelFragment.getCurrentStudio(),
                                panelFragment.getCurrentCharge());
                    } else if (ActivityDispatcher.ACTION_GALLERY_FILTER.equals(mAction)) {
                        //mGalleryFilterFragment.setCurrentState(mState);
                    }
                }
            }
        });

    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.title_filter);
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (mCurrentFragment != null) {
            FragmentUtils.hideFragment(getSupportFragmentManager(), mCurrentFragment);
        }
        if (checkedId == mVideoBtn.getId()) {
            mCurrentFragment = mVideoFilterFragment;
        } else if (checkedId == mGalleryBtn.getId()) {
            mCurrentFragment = mGalleryFilterFragment;
        }
        FragmentUtils.showFragment(getSupportFragmentManager(), mCurrentFragment, R.id.filter_fragment_container);
    }
}

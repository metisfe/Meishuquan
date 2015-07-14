package com.metis.coursepart.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.metis.base.TitleBarActivity;
import com.metis.base.manager.RequestCallback;
import com.metis.base.utils.FragmentUtils;
import com.metis.coursepart.R;
import com.metis.coursepart.adapter.AlbumAdapter;
import com.metis.coursepart.fragment.VideoFilterFragment;
import com.metis.coursepart.manager.CourseManager;
import com.metis.coursepart.module.CourseChannelList;
import com.metis.coursepart.module.CourseType;
import com.metis.coursepart.module.StudioInfo;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.List;

public class FilterActivity extends TitleBarActivity {

    private VideoFilterFragment mVideoFilterFragment = new VideoFilterFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        /*RadioGroup switchView = (RadioGroup) LayoutInflater.from(this).inflate(R.layout.layout_tab_switch, null);
        getTitleBar().setCenterView(switchView);*/

        FragmentUtils.showFragment(getSupportFragmentManager(), mVideoFilterFragment, R.id.filter_fragment_container);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        CourseManager.getInstance(this).getCourseChannelList(new RequestCallback<CourseChannelList>() {
            @Override
            public void callback(ReturnInfo<CourseChannelList> returnInfo, String callbackId) {
                if (returnInfo.isSuccess()) {
                    CourseChannelList channelList = returnInfo.getData();
                    List<StudioInfo> studioList = channelList.coursestuido;
                    List<CourseType> courseTypeList =  channelList.courseType;
                    mVideoFilterFragment.getFilterPanelFragment().setCourseTypeList(courseTypeList);
                    mVideoFilterFragment.getFilterPanelFragment().setStudioList(studioList);
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

}

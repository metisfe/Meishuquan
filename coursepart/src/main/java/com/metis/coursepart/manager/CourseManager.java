package com.metis.coursepart.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.base.framework.NetProxy;
import com.metis.base.manager.AbsManager;
import com.metis.base.manager.RequestCallback;
import com.metis.coursepart.module.CourseChannelList;
import com.metis.coursepart.module.MainCourseList;
import com.metis.msnetworklib.contract.ReturnInfo;

/**
 * Created by Beak on 2015/7/3.
 */
public class CourseManager extends AbsManager {

    private static final String REQUEST_ROOT = "v1.1/Channel/";

    private static final String
    COURSE_CHANNEL_LIST = "CourseChannelList",
    MAIN_COURSE_LIST = "MainCourseList";

    public CourseManager(Context context) {
        super(context);
    }

    public void getCourseChannelList (final RequestCallback<CourseChannelList> callback) {
        NetProxy.getInstance(getContext()).doGetRequest(REQUEST_ROOT + COURSE_CHANNEL_LIST, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result) {
                Gson gson = new Gson();
                ReturnInfo<CourseChannelList> returnInfo = gson.fromJson(
                        result, new TypeToken<ReturnInfo<CourseChannelList>>() {
                }.getType());
                if (callback != null) {
                    callback.callback(returnInfo);
                }
            }
        });
    }

    public void getMainCourseList (final RequestCallback<MainCourseList> callback) {
        NetProxy.getInstance(getContext()).doGetRequest(REQUEST_ROOT + MAIN_COURSE_LIST, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result) {
                Gson gson = new Gson();
                ReturnInfo<MainCourseList> returnInfo = gson.fromJson(
                        result,
                        new TypeToken<ReturnInfo<MainCourseList>>(){}.getType()
                );
                if (callback != null) {
                    callback.callback(returnInfo);
                }
            }
        });
    }
}

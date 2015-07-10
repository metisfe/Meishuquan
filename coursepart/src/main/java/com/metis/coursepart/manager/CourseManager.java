package com.metis.coursepart.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.metis.base.framework.NetProxy;
import com.metis.base.manager.AbsManager;
import com.metis.base.manager.RequestCallback;
import com.metis.coursepart.module.CourseChannelList;
import com.metis.coursepart.module.CourseSubList;
import com.metis.coursepart.module.MainCourseList;
import com.metis.msnetworklib.contract.ReturnInfo;

/**
 * Created by Beak on 2015/7/3.
 */
public class CourseManager extends AbsManager {

    private static final String REQUEST_ROOT = "v1.1/";

    private static CourseManager sManager = null;
    public synchronized static CourseManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new CourseManager(context.getApplicationContext());
        }
        return sManager;
    }

    private static final String
    COURSE_CHANNEL_LIST = REQUEST_ROOT + "Channel/CourseChannelList",
    MAIN_COURSE_LIST = REQUEST_ROOT + "Course/MainCourseList",
    COURSE_SUB_LIST = REQUEST_ROOT + "Course/CourseSublist?id={id}";
    private Gson mGson = null;
    private CourseManager(Context context) {
        super(context);
        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

    public void getCourseChannelList (final RequestCallback<CourseChannelList> callback) {
        NetProxy.getInstance(getContext()).doGetRequest(COURSE_CHANNEL_LIST, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result) {
                ReturnInfo<CourseChannelList> returnInfo = mGson.fromJson(
                        result, new TypeToken<ReturnInfo<CourseChannelList>>() {
                }.getType());
                if (callback != null) {
                    callback.callback(returnInfo);
                }
            }
        });
    }

    public void getMainCourseList (final RequestCallback<MainCourseList> callback) {
        NetProxy.getInstance(getContext()).doGetRequest(MAIN_COURSE_LIST, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result) {
                ReturnInfo<MainCourseList> returnInfo = mGson.fromJson(
                        result,
                        new TypeToken<ReturnInfo<MainCourseList>>(){}.getType()
                );
                if (callback != null) {
                    callback.callback(returnInfo);
                }
            }
        });
    }

    public void getCourseSubList (long courseId, final RequestCallback<CourseSubList> callback) {
        String request = COURSE_SUB_LIST.replace("{id}", courseId + "");
        NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result) {
                ReturnInfo<CourseSubList> returnInfo = mGson.fromJson(
                        result,
                        new TypeToken<ReturnInfo<CourseSubList>>(){}.getType()
                );
                if (callback != null) {
                    callback.callback(returnInfo);
                }
            }
        });
    }

    //private void onCallback
}

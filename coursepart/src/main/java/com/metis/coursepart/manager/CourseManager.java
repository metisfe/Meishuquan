package com.metis.coursepart.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.metis.base.framework.NetProxy;
import com.metis.base.manager.AbsManager;
import com.metis.base.manager.RequestCallback;
import com.metis.coursepart.module.CourseAlbum;
import com.metis.coursepart.module.CourseChannelList;
import com.metis.coursepart.module.CourseSubList;
import com.metis.coursepart.module.MainCourseList;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.List;

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
    COURSE_SUB_LIST = REQUEST_ROOT + "Course/CourseSublist?id={id}",
    COURSE_LIST = REQUEST_ROOT + "Course/CourseList?tags={tags}&orderType={orderType}&querycontent={querycontent}&index={index}&studioid={studioid}&chargetype={chargetype}";
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
            public void onResponse(String result, String requestId) {
                ReturnInfo<CourseChannelList> returnInfo = mGson.fromJson(
                        result, new TypeToken<ReturnInfo<CourseChannelList>>() {
                }.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    public void getMainCourseList (final RequestCallback<MainCourseList> callback) {
        NetProxy.getInstance(getContext()).doGetRequest(MAIN_COURSE_LIST, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<MainCourseList> returnInfo = mGson.fromJson(
                        result,
                        new TypeToken<ReturnInfo<MainCourseList>>(){}.getType()
                );
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    public void getCourseSubList (long courseId, final RequestCallback<CourseSubList> callback) {
        String request = COURSE_SUB_LIST.replace("{id}", courseId + "");
        NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<CourseSubList> returnInfo = mGson.fromJson(
                        result,
                        new TypeToken<ReturnInfo<CourseSubList>>(){}.getType()
                );
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }
//    Course/CourseList?tags={tags}&orderType={orderType}&querycontent={querycontent}&index={index}&studioid={studioid}&chargetype={chargetype}
    public String getCourseList (long tagId, long orderType, String queryContent, int index, long studioId, long chargeType, final RequestCallback<List<CourseAlbum>> callback) {
        String request = COURSE_LIST
                .replace("{tags}", tagId + "")
                .replace("{orderType}", orderType + "")
                .replace("{querycontent}", queryContent)
                .replace("{index}", index + "")
                .replace("{studioid}", studioId + "")
                .replace("{chargetype}", chargeType + "");
        return NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<List<CourseAlbum>> returnInfo = mGson.fromJson(
                        result,
                        new TypeToken<ReturnInfo<List<CourseAlbum>>>(){}.getType()
                );
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    //private void onCallback
}

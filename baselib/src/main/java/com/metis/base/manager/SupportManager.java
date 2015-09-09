package com.metis.base.manager;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.metis.base.framework.NetProxy;
import com.metis.msnetworklib.contract.ReturnInfo;

/**
 * Created by Beak on 2015/9/9.
 */
public class SupportManager extends AbsManager {

    private static SupportManager sManager = null;

    private static final String REQUEST_SUPPORT = "v1.1/Comment/Support?userid={userid}&id={id}&type={type}&result={result}&session={session}";

    public static synchronized SupportManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new SupportManager(context.getApplicationContext());
        }
        return sManager;
    }

    private SupportManager(Context context) {
        super(context);
    }

    public String supportStatusComment (long userId, long commentId, String session) {
        return action(userId, commentId, TYPE_ASSESS_COMMENT, 1, session, null);
        /*String request = REQUEST_SUPPORT
                .replace("{userid}", userId + "")
                .replace("{id}", commentId + "")
                .replace("{type}", 2 + "")
                .replace("{result}", 1 + "")
                .replace("{session}", session);
        return NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {

            }
        });*/
    }

    /*public String unSupportStatusComment (long userId, long commentId, String session) {
        return action(userId, commentId, TYPE_ASSESS_COMMENT, 2, session, null);
    }*/

    public String supportStatus (long userId, long assessId, String session) {
        return action(userId, assessId, TYPE_ASSESS, 1, session, null);
        /*String request = REQUEST_SUPPORT
                .replace("{userid}", userId + "")
                .replace("{id}", assessId + "")
                .replace("{type}", 1 + "")
                .replace("{result}", 1 + "")
                .replace("{session}", session);
        return NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {

            }
        });*/
    }

    public String supportNewsComment (long userId, long commentId, String session) {
        return action(userId, commentId, TYPE_NEWS_COMMENT, 1, session, null);
    }

    /*public String unSupportStatus (long userId, long assessId, String session) {
        return action(userId, assessId, TYPE_ASSESS, 2, session, null);
    }*/
    /*REQUEST_SUPPORT = "v1.1/Comment/Support?userid={userid}&id={id}&type={type}&result={result}&session={session}";*/
    /// <summary>
    /// 赞/踩
    /// </summary>
    /// <param name="userid">用户id</param>
    /// <param name="id">内容id</param>
    /// 赞/转发 类型Assess = 1,AssessComment = 2,News = 3,NewsComment = 4,Course = 5,CourseComment = 6,Circle=7,CircleComment =8,ActivityStudio = 9,ActivityStudent = 10,Activity = 11
    /// <param name="type"></param>
    /// <param name="result">1 赞 2 踩</param>
    /// <returns></returns>
    public String action (long userId, long id, int type, int result, String session, final RequestCallback callback) {
        String request = REQUEST_SUPPORT
                .replace("{userid}", userId + "")
                .replace("{id}", id + "")
                .replace("{type}", type + "")
                .replace("{result}", result + "")
                .replace("{session}", session);
        return NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo>(){}.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    public static final int
            TYPE_ASSESS = 1,
            TYPE_ASSESS_COMMENT = 2,
            TYPE_NEWS = 3,
            TYPE_NEWS_COMMENT = 4,
            TYPE_COURSE = 5,
            TYPE_COURSE_COMMENT = 6,
            TYPE_CIRCLE = 7,
            TYPE_CIRCLE_COMMENT = 8,
            TYPE_ACTIVITY_STUDIO = 9,
            TYPE_ACTIVITY_STUDENT = 10,
            TYPE_ACTIVITY = 11;
}

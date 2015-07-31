package com.metis.commentpart.manager;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.metis.base.framework.NetProxy;
import com.metis.base.manager.AbsManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.Thumbnail;
import com.metis.base.module.User;
import com.metis.commentpart.module.Teacher;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Beak on 2015/7/30.
 */
public class StatusManager extends AbsManager {

    private static final int TYPE_COMMENT_IMAGE = 0;

    private static StatusManager sManager = null;

    public synchronized static StatusManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new StatusManager(context.getApplicationContext());
        }
        return sManager;
    }

    private static final String
    REQUEST_PUBLISH_ASSESS = "v1.1/Assess/PublishAssess",
    REQUEST_GET_ASSESS_TEACHER = "v1.1/Assess/GetAssessTeacher?softType={softType}&querystring={querystring}&session={session}",
    REQUEST_GET_ASSESS_LIST = "v1.1/Assess/GetAssessList?assessState={assessState}&channelId={channelId}&region={region}&index={index}",
    REQUEST_PUSH_COMMENT = "v1.1/AssessComment/PushComment",
    REQUEST_GET_COMMENT_LIST = "v1.1/AssessComment/GetCommentList?assessId={assessId}";

    public static final int TEACHER_TYPE_MOST_COMMENTS = 0, TEACHER_TYPE_BEST = 1, TEACHER_TYPE_RECENT = 2;

    private StatusManager(Context context) {
        super(context);
    }

    public String publishAssess (String content, int channelId,
                               List<Long> teacherIds, Thumbnail thumbnail, final RequestCallback callback) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("assessContent", content);
        map.put("assessChanell", channelId + "");
        StringBuilder builder = new StringBuilder("[");
        final int length = teacherIds.size();
        for (int i = 0; i < length; i++) {
            builder.append(teacherIds.get(i).longValue());
            if (i < length - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        map.put("teacherIds", builder.toString());
        if (thumbnail != null) {
            map.put("assessImg", getGson().toJson(thumbnail));
        }
        return NetProxy.getInstance(getContext()).doPostRequest(
                REQUEST_PUBLISH_ASSESS,
                map, new NetProxy.OnResponseListener() {
                    @Override
                    public void onResponse(String result, String requestId) {
                        if (callback != null) {
                            //callback.callback();
                        }
                    }
                });
    }

    /*排序规则 0 评论最多，1 口碑最好，2 最近点评*/
    public String getAssessTeacher (int softType, String queryString, String session, final RequestCallback<List<Teacher>> callback) {
        String request = REQUEST_GET_ASSESS_TEACHER
                .replace("{softType}", softType + "")
                .replace("{querystring}", queryString)
                .replace("{session}", session);
        return NetProxy.getInstance(getContext()).doGetRequest(
                request, new NetProxy.OnResponseListener() {
                    @Override
                    public void onResponse(String result, String requestId) {
                        ReturnInfo<List<Teacher>> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<List<Teacher>>>() {
                        }.getType());
                        if (callback != null) {
                            callback.callback(returnInfo, requestId);
                        }
                    }
                }
        );
    }
    /*点评状态 0 全部 1 未点评 1 点评 2 热门点评*/
    public String getAssessList (int assessState, long channelId, int region, int index, final RequestCallback callback) {
        String request = REQUEST_GET_ASSESS_LIST
                .replace("{assessState}", assessState + "")
                .replace("{channelId}", channelId + "")
                .replace("{region}", region + "")
                .replace("{index}", index + "");
        return NetProxy.getInstance(getContext()).doGetRequest(
                request, new NetProxy.OnResponseListener() {
                    @Override
                    public void onResponse(String result, String requestId) {

                    }
                }
        );
    }

    public String pushComment (long assessId, long userId, long replyUserId,
                               String content, long replyCid, List<Thumbnail> thumbnails,
                               String voiceUrl, String voiceLength, int commentType) {
        Map<String, String> map = new Hashtable<String, String>();
        map.put("assessId", assessId + "");
        map.put("userId", userId + "");
        map.put("replyUserId", replyUserId + "");
        map.put("content", content);
        map.put("replyCid", replyCid + "");

        //TODO thumbnails

        map.put("voice", voiceUrl);
        map.put("voiceLength", voiceLength);
        map.put("commentType", commentType + "");

        return NetProxy.getInstance(getContext()).doPostRequest(
                REQUEST_PUSH_COMMENT, map, new NetProxy.OnResponseListener() {
                    @Override
                    public void onResponse(String result, String requestId) {

                    }
                }
        );
    }

    public String getCommentList (long assessId, final RequestCallback callback) {
        String request = REQUEST_GET_COMMENT_LIST.replace("{assessId}", assessId + "");
        return NetProxy.getInstance(getContext()).doGetRequest(
                request, new NetProxy.OnResponseListener() {
                    @Override
                    public void onResponse(String result, String requestId) {
                        if (callback != null) {
                            //callback
                        }
                    }
                }
        );
    }

}

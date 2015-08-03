package com.metis.commentpart.manager;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.metis.base.framework.NetProxy;
import com.metis.base.manager.AbsManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.ImageInfo;
import com.metis.base.module.Thumbnail;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.commentpart.module.AssessChannel;
import com.metis.commentpart.module.Status;
import com.metis.commentpart.module.StatusList;
import com.metis.commentpart.module.Teacher;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Beak on 2015/7/30.
 */
public class StatusManager extends AbsManager {

    private static final String TAG = StatusManager.class.getSimpleName();

    private static final int TYPE_COMMENT_IMAGE = 0;

    private static StatusManager sManager = null;

    public synchronized static StatusManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new StatusManager(context.getApplicationContext());
        }
        return sManager;
    }

    private static final String
    REQUEST_PUBLISH_ASSESS = "v1.1/Assess/PublishAssess?session={session}",
    REQUEST_GET_ASSESS_TEACHER = "v1.1/Assess/GetAssessTeacher?softType={softType}&querystring={querystring}&session={session}",
    REQUEST_GET_ASSESS_LIST = "v1.1/Assess/GetAssessList?assessState={assessState}&channelId={channelId}&region={region}&index={index}&session={session}",
    REQUEST_PUSH_COMMENT = "v1.1/AssessComment/PushComment",
    REQUEST_GET_COMMENT_LIST = "v1.1/AssessComment/GetCommentList?assessId={assessId}",
    REQUEST_CHANNEL_LIST = "v1.1/Channel/AssessChanellist";

    public static final int TEACHER_TYPE_MOST_COMMENTS = 0, TEACHER_TYPE_BEST = 1, TEACHER_TYPE_RECENT = 2;

    private AssessChannel mAssessChannel = null;

    private StatusManager(Context context) {
        super(context);
        getChannelList(null);
    }

    public String publishAssess (String content, long channelId,
                               List<Long> teacherIds, ImageInfo thumbnail, String session, final RequestCallback callback) {
        /*Map<String, String> map = new HashMap<String, String>();
        map.put("assessContent", content);
        map.put("assessChanell", channelId + "");
        if (teacherIds != null) {
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
        }

        if (thumbnail != null) {
            String thumbnailString = getGson().toJson(thumbnail);
            map.put("assessImg", thumbnailString);
        }*/
        Access access = new Access();
        access.assessContent = content;
        access.assessChannell = channelId;
        if (teacherIds != null && !teacherIds.isEmpty()) {
            final int length = teacherIds.size();
            access.teacherIds = new long[length];
            for (int i = 0; i < length; i++) {
                access.teacherIds[i] = teacherIds.get(i).longValue();
            }
        }
        if (thumbnail != null) {
            access.assessImg = thumbnail;
            Log.v(TAG, "publish access with pic=" + access.assessImg.getImgUrl());
        }

        return NetProxy.getInstance(getContext()).doPostRequest(
                REQUEST_PUBLISH_ASSESS.replace("{session}", session),
                        access, new NetProxy.OnResponseListener() {

                            @Override
                            public void onResponse(String result, String requestId) {

                            }
                        });
        /*return NetProxy.getInstance(getContext()).doPostRequest(
                REQUEST_PUBLISH_ASSESS.replace("{session}", session),
                map, new NetProxy.OnResponseListener() {
                    @Override
                    public void onResponse(String result, String requestId) {
                        if (callback != null) {
                            //callback.callback();
                        }
                    }
                });*/
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
    public String getAssessList (int assessState, long channelId, int region, int index, String session, final RequestCallback<StatusList> callback) {
        final String request = REQUEST_GET_ASSESS_LIST
                .replace("{assessState}", assessState + "")
                .replace("{channelId}", channelId + "")
                .replace("{region}", region + "")
                .replace("{index}", index + "")
                .replace("{session}", session);
        return NetProxy.getInstance(getContext()).doGetRequest(
                request, new NetProxy.OnResponseListener() {
                    @Override
                    public void onResponse(String result, String requestId) {
                        ReturnInfo<StatusList> returnInfo = getGson().fromJson(
                                result,
                                new TypeToken<ReturnInfo<StatusList>>(){}.getType());
                        if (callback != null) {
                            callback.callback(returnInfo, requestId);
                        }
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

    public AssessChannel getAssessChannel () {
        return mAssessChannel;
    }

    public String getChannelList (final RequestCallback<AssessChannel> listener) {
        return NetProxy.getInstance(getContext()).doGetRequest(REQUEST_CHANNEL_LIST, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<AssessChannel> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<AssessChannel>>(){}.getType());
                if (returnInfo.isSuccess()) {
                    mAssessChannel = returnInfo.getData();
                }
                if (listener != null) {
                    listener.callback(returnInfo, requestId);
                }
            }
        });
    }

    private class Access implements Serializable {
        public String assessContent;
        public long assessChannell;
        public long[] teacherIds;
        public ImageInfo assessImg;

        public String getAssessContent() {
            return assessContent;
        }

        public void setAssessContent(String assessContent) {
            this.assessContent = assessContent;
        }

        public long getAssessChannell() {
            return assessChannell;
        }

        public void setAssessChannell(long assessChannell) {
            this.assessChannell = assessChannell;
        }

        public long[] getTeacherIds() {
            return teacherIds;
        }

        public void setTeacherIds(long[] teacherIds) {
            this.teacherIds = teacherIds;
        }

        public ImageInfo getAssessImg() {
            return assessImg;
        }

        public void setAssessImg(ImageInfo assessImg) {
            this.assessImg = assessImg;
        }
    }

}

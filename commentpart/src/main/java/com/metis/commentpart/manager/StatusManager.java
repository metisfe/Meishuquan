package com.metis.commentpart.manager;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.metis.base.framework.NetProxy;
import com.metis.base.manager.AbsManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.ImageInfo;
import com.metis.base.module.Thumbnail;
import com.metis.base.module.User;
import com.metis.base.utils.Log;
import com.metis.commentpart.module.AssessChannel;
import com.metis.commentpart.module.Comment;
import com.metis.commentpart.module.CommentCollection;
import com.metis.commentpart.module.PushCommentParams;
import com.metis.commentpart.module.Status;
import com.metis.commentpart.module.StatusList;
import com.metis.commentpart.module.Teacher;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.io.Serializable;
import java.net.URLEncoder;
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
    REQUEST_GET_ASSESS_TEACHER = "v1.1/Assess/GetAssessTeacher?softType={softType}&querystring={querystring}&session={session}&index={index}",
    REQUEST_GET_ASSESS_LIST = "v1.1/Assess/GetAssessList?assessState={assessState}&channelId={channelId}&region={region}&index={index}&session={session}",
    REQUEST_PUSH_COMMENT = "v1.1/AssessComment/PushComment?session={session}",
    REQUEST_GET_COMMENT_LIST = "v1.1/AssessComment/GetCommentList?assessId={assessId}&session={session}",
    REQUEST_CHANNEL_LIST = "v1.1/Channel/AssessChanellist",
    REQUEST_SUPPORT = "v1.1/Comment/Support?userid={userid}&id={id}&type={type}&result={result}&session={session}";

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
        access.setAssessChanell((int)channelId);
        Log.v(TAG, "pushAssess=" + channelId);
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
                        ReturnInfo returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo>(){}.getType());
                        if (callback != null) {
                            callback.callback(returnInfo, requestId);
                        }
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
    public String getAssessTeacher (int softType, String queryString, String session, int index, final RequestCallback<List<Teacher>> callback) {
        String request = REQUEST_GET_ASSESS_TEACHER
                .replace("{softType}", softType + "")
                .replace("{querystring}", URLEncoder.encode(queryString))
                .replace("{session}", session)
                .replace("{index}", index + "");
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
    public String getAssessList (long assessState, long channelId, long region, int index, String session, final RequestCallback<StatusList> callback) {
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
                                new TypeToken<ReturnInfo<StatusList>>() {
                                }.getType());
                        if (callback != null) {
                            callback.callback(returnInfo, requestId);
                        }
                    }
                }
        );
    }

    /**
     *
     * @param assessId
     * @param userId
     * @param replyUserId
     * @param content
     * @param replyCid
     * @param thumbnails
     * @param voiceUrl
     * @param voiceLength
     * @param commentType 1图片 2语音 3文字
     * @param session
     * @return
     */
    public String pushComment (long assessId, long userId, long replyUserId,
                               String content, long replyCid, List<Thumbnail> thumbnails,
                               String voiceUrl, int voiceLength, int commentType, int commentSource, String session, final RequestCallback<Comment> callback) {
        PushCommentParams params = new PushCommentParams();
        params.setAssessId(assessId);
        params.setUserId(userId);
        params.setReplyUserId(replyUserId);
        params.setReplyCid(replyCid);
        params.setContent(content);
        params.setCommentType(commentType);
        params.setCommentSource(commentSource);
        if (!TextUtils.isEmpty(voiceUrl)) {
            params.setVoice(voiceUrl);
        }
        if (voiceLength > 0) {
            params.setVoiceLength(voiceLength);
        }

        return NetProxy.getInstance(getContext())
                .doPostRequest(
                        REQUEST_PUSH_COMMENT.replace("{session}", session),
                        params, new NetProxy.OnResponseListener() {
                            @Override
                            public void onResponse(String result, String requestId) {
                                ReturnInfo<Comment> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<Comment>>(){}.getType());
                                if (callback != null) {
                                    callback.callback(returnInfo, requestId);
                                }
                            }
                        }
                );
        /*Map<String, String> map = new Hashtable<String, String>();
        map.put("assessId", assessId + "");
        map.put("userId", userId + "");
        map.put("replyUserId", replyUserId + "");
        map.put("content", content);
        map.put("replyCid", replyCid + "");

        if (voiceUrl != null) {
            map.put("voice", voiceUrl);
        }
        if (voiceLength != null) {
            map.put("voiceLength", voiceLength);
        }

        map.put("commentType", commentType + "");

        return NetProxy.getInstance(getContext()).doPostRequest(
                REQUEST_PUSH_COMMENT.replace("{session}", session), map, new NetProxy.OnResponseListener() {
                    @Override
                    public void onResponse(String result, String requestId) {

                    }
                }
        );*/
    }

    public String getCommentList (long assessId, String session, final RequestCallback<CommentCollection> callback) {
        final String request = REQUEST_GET_COMMENT_LIST
                .replace("{assessId}", assessId + "")
                .replace("{session}", session);
        return NetProxy.getInstance(getContext()).doGetRequest(
                request, new NetProxy.OnResponseListener() {
                    @Override
                    public void onResponse(String result, String requestId) {
                        ReturnInfo<CommentCollection> returnInfo = getGson().fromJson(
                                result, new TypeToken<ReturnInfo<CommentCollection>>() {
                        }.getType()
                        );
                        if (callback != null) {
                            callback.callback(returnInfo, requestId);
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

    public String supportComment (long userId, long commentId, String session) {
        String request = REQUEST_SUPPORT
                .replace("{userid}", userId + "")
                .replace("{id}", commentId + "")
                .replace("{type}", 2 + "")
                .replace("{result}", 1 + "")
                .replace("{session}", session);
        return NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {

            }
        });
    }

    public String supportStatus (long userId, long assessId, String session) {
        String request = REQUEST_SUPPORT
                .replace("{userid}", userId + "")
                .replace("{id}", assessId + "")
                .replace("{type}", 1 + "")
                .replace("{result}", 1 + "")
                .replace("{session}", session);
        return NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {

            }
        });
    }

    private class Access implements Serializable {
        public String assessContent;
        public int assessChanell;
        public long[] teacherIds;
        public ImageInfo assessImg;

        public String getAssessContent() {
            return assessContent;
        }

        public void setAssessContent(String assessContent) {
            this.assessContent = assessContent;
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

        public int getAssessChanell() {
            return assessChanell;
        }

        public void setAssessChanell(int assessChanell) {
            this.assessChanell = assessChanell;
        }
    }

}

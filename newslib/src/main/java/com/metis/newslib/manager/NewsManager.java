package com.metis.newslib.manager;

import android.content.Context;
import android.os.Environment;

import com.google.gson.reflect.TypeToken;
import com.metis.base.framework.NetProxy;
import com.metis.base.manager.AbsManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.enums.BlockTypeEnum;
import com.metis.base.module.enums.SupportTypeEnum;
import com.metis.base.utils.Log;
import com.metis.msnetworklib.contract.ReturnInfo;
import com.metis.newslib.module.ChannelList;
import com.metis.newslib.module.NewsCommentList;
import com.metis.newslib.module.NewsDetails;
import com.metis.newslib.module.NewsItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Beak on 2015/9/1.
 */
public class NewsManager extends AbsManager {

    private static final String TAG = NewsManager.class.getSimpleName();

    private final String CHANNELLIST_URL = "v1.1/Channel/ChannelList?userId={userId}&type={type}";//根据用户Id和模块类型获得频道集合;
    private final String CHANNEL_NEW_LIST_URL = "v1.1/News/NewsList?ChanelId={ChanelId}&lastNewsId={lastNewsId}";//根据频道获取news列表
    private final String NEWS_INFO_URL = "v1.1/News/NewsDetail?newsId={newsId}&session={session}";//根据newsId获得详情
    private final String COMMENT_LIST_NEWSID = "v1.1/Comment/CommentList?type={type}&newsId={newsId}&lastCommentId={lastCommentId}&session={session}";//根据newsId获得评论列表
    private final String PUBLISHCOMMENT =
            "v1.1/Comment/PublishComment?userid={userid}&newsid={newsid}&content={content}&replyCid={replyCid}&blockType={blockType}&session={session}";//发表评论
    private final String PRIVATE = "v1.1/Comment/Favorite?userid={userid}&id={id}&type={type}&result={result}&session={session}";

    private static NewsManager sManager = null;

    public static synchronized NewsManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new NewsManager(context.getApplicationContext());
        }
        return sManager;
    }

    public NewsManager(Context context) {
        super(context);
    }

    public void getChannelList (String userId, int type, final RequestCallback<ChannelList> callback) {
        String request = CHANNELLIST_URL
                .replace("{userId}", userId)
                .replace("{type}", type + "");
        NetProxy.getInstance(getContext())
                .doGetRequest(request, new NetProxy.OnResponseListener() {
                    @Override
                    public void onResponse(String result, String requestId) {
                        ReturnInfo<ChannelList> returnInfo = getGson().fromJson(
                                result, new TypeToken<ReturnInfo<ChannelList>>() {
                                }.getType()
                        );
                        if (callback != null) {
                            callback.callback(returnInfo, requestId);
                        }
                    }
                });
    }

    public void getNewsList (long channelId, long lastNewsId, final RequestCallback<List<NewsItem>> callback) {
        final String request = CHANNEL_NEW_LIST_URL
                .replace("{ChanelId}", channelId + "")
                .replace("{lastNewsId}", lastNewsId + "");
        NetProxy.getInstance(getContext())
                .doGetRequest(request, new NetProxy.OnResponseListener() {
                    @Override
                    public void onResponse(String result, String requestId) {
                        ReturnInfo<List<NewsItem>> returnInfo = getGson().fromJson(
                                result, new TypeToken<ReturnInfo<List<NewsItem>>>(){}.getType()
                        );
                        if (callback != null) {
                            callback.callback(returnInfo, requestId);
                        }
                    }
                });
    }

    public void getNewsInfoById (long userId, String session, final RequestCallback<NewsDetails> callback) {
        final String request = NEWS_INFO_URL
                .replace("{newsId}", userId + "")
                .replace("{session}", session);
        NetProxy.getInstance(getContext())
                .doGetRequest(request, new NetProxy.OnResponseListener() {
                    @Override
                    public void onResponse(String result, String requestId) {
                        File file = new File (Environment.getExternalStorageDirectory(), "log" + File.separator + "log.txt");
                        Log.e(TAG, "log path=" + file.getAbsolutePath());
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        try {
                            FileWriter writer = new FileWriter(file);
                            writer.write(result);
                            writer.flush();
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (result.length() > 3100) {
                            Log.v(TAG, "string=" + result.substring(3000, 3100));
                        }
                        ReturnInfo<NewsDetails> returnInfo = getGson().fromJson(
                                result, new TypeToken<ReturnInfo<NewsDetails>>(){}.getType());

                        if (callback != null) {
                            callback.callback(returnInfo, requestId);
                        }
                    }
                });
    }

    public void getCommentListByNewsId (long newsId, long lastCommentId, String session, final RequestCallback<NewsCommentList> callback) {
        String request = COMMENT_LIST_NEWSID
                .replace("{type}", 0 + "")
                .replace("{newsId}", newsId + "")
                .replace("{lastCommentId}", lastCommentId + "")
                .replace("{session}", session);
        NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<NewsCommentList> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<NewsCommentList>>() {
                }.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    //"v1.1/Comment/PublishComment?userid={userid}&newsid={newsid}&content={content}&replyCid={replyCid}&blockType={blockType}&session={session}";//发表评论
    public void pushComment (long userId, long newsId, String content, long replyCid, String session, final RequestCallback<Integer> callback) {
        String request = PUBLISHCOMMENT
                .replace("{userid}", userId + "")
                .replace("{newsid}", newsId + "")
                .replace("{content}", URLEncoder.encode(content))
                .replace("{replyCid}", replyCid + "")
                .replace("{blockType}", BlockTypeEnum.TOPLINE.getVal() + "")
                .replace("{session}", session);
        NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<Integer> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<Integer>>(){}.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    //PRIVATE = "v1.1/Comment/Favorite?userid={userid}&id={id}&type={type}&result={result}&session={session}";

    /**
     *
     * @param userid
     * @param id
     * @param type
     * @param result 1 收藏, 2取消收藏
     * @param callback
     */
    public void favorite (long userid, long id, SupportTypeEnum type, int result, String session, final RequestCallback callback) {
        String request = PRIVATE
                .replace("{userid}", userid + "")
                .replace("{id}", id + "")
                .replace("{type}", type.getVal() + "")
                .replace("{result}", result + "")
                .replace("{session}", session);
        NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo>(){}.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

}
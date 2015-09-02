package com.metis.newslib.manager;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.metis.base.framework.NetProxy;
import com.metis.base.manager.AbsManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.utils.Log;
import com.metis.msnetworklib.contract.ReturnInfo;
import com.metis.newslib.module.ChannelList;
import com.metis.newslib.module.NewsDetails;
import com.metis.newslib.module.NewsItem;

import java.util.List;

/**
 * Created by Beak on 2015/9/1.
 */
public class NewsManager extends AbsManager {

    private static final String TAG = NewsManager.class.getSimpleName();

    private final String CHANNELLIST_URL = "v1.1/Channel/ChannelList?userId={userId}&type={type}";//根据用户Id和模块类型获得频道集合;
    private final String CHANNEL_NEW_LIST_URL = "v1.1/News/NewsList?ChanelId={ChanelId}&lastNewsId={lastNewsId}";//根据频道获取news列表
    private final String NEWS_INFO_URL = "v1.1/News/NewsDetail?newsId={newsId}";//根据newsId获得详情

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

    public void getNewsInfoById (long userId, final RequestCallback<NewsDetails> callback) {
        final String request = NEWS_INFO_URL
                .replace("{newsId}", userId + "");
        NetProxy.getInstance(getContext())
                .doGetRequest(request, new NetProxy.OnResponseListener() {
                    @Override
                    public void onResponse(String result, String requestId) {
                        Log.v(TAG, "string=" + result.substring(120, 140));
                        ReturnInfo<NewsDetails> returnInfo = getGson().fromJson(
                                result, new TypeToken<ReturnInfo<NewsDetails>>(){}.getType());

                        if (callback != null) {
                            callback.callback(returnInfo, requestId);
                        }
                    }
                });
    }

}

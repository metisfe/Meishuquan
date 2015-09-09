package com.metis.meishuquan.manager;

import android.content.Context;
import android.os.Environment;

import com.google.gson.reflect.TypeToken;
import com.metis.base.framework.NetProxy;
import com.metis.base.manager.AbsManager;
import com.metis.base.manager.RequestCallback;
import com.metis.msnetworklib.contract.ReturnInfo;
import com.metis.newslib.module.CircleImage;
import com.metis.newslib.module.CirclePushBlogParams;

import java.io.File;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.wechat.utils.WXImageObject;

/**
 * Created by Beak on 2015/8/26.
 */
public class ShareManager extends AbsManager {

    private static final String TAG = ShareManager.class.getSimpleName();

    private static final String URL_PUSHBLOG = "v1.1/Circle/PushBlog?session={session}";

    private static ShareManager sManager = null;

    public static synchronized ShareManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new ShareManager(context.getApplicationContext());
        }
        return sManager;
    }

    private ShareManager(Context context) {
        super(context);
        ShareSDK.initSDK(context);
    }

    public void loginAccess (Platform platform, PlatformActionListener listener) {
        if (platform == null) {
            return;
        }
        if (platform.isValid()) {
            //TODO
            listener.onComplete(platform, 0, null);
            PlatformDb db = platform.getDb();
            String userId = db.getUserId();
            String profile = db.getUserIcon();
            if (userId != null) {
                //UIHandler.sendEmptyMessage(Platform.)
                //TODO
                return;
            }
        }
        platform.setPlatformActionListener(listener);
        platform.SSOSetting(false);
        platform.showUser(null);
        //platform.authorize();
    }

    public void weChatMomentsShare (String title, String text, String imageUrl, String url, PlatformActionListener listener) {
        /*Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url); // 标题的超链接
        sp.setText(text);
        sp.setImagePath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File.separator + "a.jpg");
        sp.setSite("发布分享的网站名称");
        sp.setSiteUrl("发布分享网站的地址");
        sp.setShareType(Platform.SHARE_WEBPAGE);

        Platform weChat = ShareSDK.getPlatform (getContext(), WechatMoments.NAME);
        weChat.setPlatformActionListener(listener);
        weChat.share(sp);*/
        Platform.ShareParams wechat = new Platform.ShareParams();
        wechat.setTitle(title);
        wechat.setText(text);
        wechat.setImageUrl(imageUrl);
        wechat.setUrl(url);
        wechat.setShareType(Platform.SHARE_WEBPAGE);

        Platform weixin = ShareSDK.getPlatform(getContext(), WechatMoments.NAME);
        weixin.setPlatformActionListener(listener);
        weixin.share(wechat);
    }

    public void weChatShare (String title, String text, String imageUrl, String url, PlatformActionListener listener) {

        Platform.ShareParams wechat = new Platform.ShareParams();
        wechat.setTitle(title);
        wechat.setText(text);
        wechat.setImageUrl(imageUrl);
        wechat.setUrl(url);
        wechat.setShareType(Platform.SHARE_WEBPAGE);

        Platform weixin = ShareSDK.getPlatform(getContext(), Wechat.NAME);
        weixin.setPlatformActionListener(listener);
        weixin.share(wechat);

        /*Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url); // 标题的超链接
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        sp.setSite("发布分享的网站名称");
        sp.setSiteUrl("发布分享网站的地址");
        sp.setShareType(Platform.SHARE_WEBPAGE);

        Platform weChat = ShareSDK.getPlatform (getContext(), Wechat.NAME);
        weChat.setPlatformActionListener(listener);
        weChat.share(sp);*/
    }

    public void qZoneShare (String title, String text, String imageUrl, String url, PlatformActionListener listener) {

        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url); // 标题的超链接
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        /*sp.setSite("发布分享的网站名称");
        sp.setSiteUrl("发布分享网站的地址");*/

        Platform qzone = ShareSDK.getPlatform (getContext(), QZone.NAME);
        qzone.setPlatformActionListener(listener);
        qzone.share(sp);
    }

    public void qqShare (String title, String text, String imageUrl, String url, PlatformActionListener listener) {
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url); // 标题的超链接
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        /*sp.setSite("发布分享的网站名称");
        sp.setSiteUrl("发布分享网站的地址");*/
        sp.setShareType(Platform.SHARE_WEBPAGE);

        Platform qq = ShareSDK.getPlatform (getContext(), QQ.NAME);
        qq.setPlatformActionListener(listener);
        qq.share(sp);
    }

    public void sinaWeiboShare (String title, String text, String imageUrl, String url, PlatformActionListener listener) {
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url); // 标题的超链接
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        /*sp.setSite("发布分享的网站名称");
        sp.setSiteUrl("发布分享网站的地址");*/
        sp.setShareType(Platform.SHARE_WEBPAGE);

        Platform sina = ShareSDK.getPlatform (getContext(), SinaWeibo.NAME);
        sina.setPlatformActionListener(listener);
        sina.share(sp);
    }

    public void circleShare (String title, String text, String imageUrl, String url, String session, final RequestCallback callback) {
        /*CirclePushBlogParams params = new CirclePushBlogParams();
        params.setType(0);
        params.setContent(text);
        params.
        CircleImage image = new CircleImage();
        image.
        NetProxy.getInstance(getContext()).doPostRequest(URL_PUSHBLOG.replace("{session}", session), params, new NetProxy.OnResponseListener() {

            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo>(){}.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });*/
    }

    /*public void loginAccessWeChat (Context context, PlatformActionListener listener) {

        Platform wechat= ShareSDK.getPlatform(context, Wechat.NAME);
        boolean valid = wechat.isValid();
        Log.v(TAG, "loginAccessWeChat " + valid + " " + wechat.getId());
        wechat.setPlatformActionListener(listener);
        wechat.authorize();
    }*/

    /*@Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        StringBuilder stringBuilder = new StringBuilder("onComplete:");
        if (hashMap != null) {
            Set<String> set = hashMap.keySet();
            for (String key : set) {
                stringBuilder.append(key + " " + hashMap.get(key) + "\n");
            }
        }

        Log.v(TAG, stringBuilder.toString());
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.e(TAG, "onError " + platform.getName() + " i=" + i);
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {

    }*/
}

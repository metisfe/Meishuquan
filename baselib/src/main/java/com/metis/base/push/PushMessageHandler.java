package com.metis.base.push;

import android.app.Notification;
import android.content.Context;

import com.metis.base.utils.Log;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by Beak on 2015/10/23.
 */
public class PushMessageHandler extends UmengMessageHandler {

    private static final String TAG = PushMessageHandler.class.getSimpleName();

    private static PushMessageHandler sHandler = new PushMessageHandler();

    public static PushMessageHandler getInstance () {
        return sHandler;
    }

    private PushMessageHandler () {

    }

    @Override
    public Notification getNotification(Context context, UMessage uMessage) {
        Log.v(TAG, "getNotification " + uMessage.getRaw().toString());
        return super.getNotification(context, uMessage);
    }

    @Override
    public void dealWithCustomMessage(Context context, UMessage uMessage) {
        super.dealWithCustomMessage(context, uMessage);
        Log.v(TAG, "dealWithCustomMessage " + uMessage.getRaw().toString());
    }

    @Override
    public void dealWithNotificationMessage(Context context, UMessage uMessage) {
        super.dealWithNotificationMessage(context, uMessage);
        Log.v(TAG, "dealWithNotificationMessage " + uMessage.getRaw().toString());
    }
}

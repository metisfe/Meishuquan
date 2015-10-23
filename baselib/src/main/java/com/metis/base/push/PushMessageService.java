package com.metis.base.push;

import android.content.Context;
import android.content.Intent;

import com.metis.base.utils.Log;
import com.umeng.message.UTrack;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.client.BaseConstants;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Beak on 2015/10/23.
 */
public class PushMessageService extends UmengBaseIntentService {

    private static final String TAG = PushMessageService.class.getSimpleName();

    @Override
    protected void onRegistered(Context context, String s) {
        super.onRegistered(context, s);
        Log.v(TAG, "onRegistered msg=" + s);
    }

    @Override
    protected void onUnregistered(Context context, String s) {
        super.onUnregistered(context, s);
        Log.v(TAG, "onRegistered msg=" + s);
    }

    @Override
    protected void onError(Context context, String s) {
        super.onError(context, s);
        Log.v(TAG, "onRegistered msg=" + s);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        super.onMessage(context, intent);

        //可以通过MESSAGE_BODY取得消息体
        String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
        UMessage msg = null;
        try {
            msg = new UMessage(new JSONObject(message));
            Log.v(TAG, "onMessage msg=" + message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UTrack.getInstance(context).trackMsgClick(msg);
    }
}

package com.metis.base.framework;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.base.Debug;
import com.metis.msnetworklib.contract.ReturnInfo;
import com.metis.msnetworklib.framework.network.HttpMethod;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Beak on 2015/7/3.
 */
public class NetProxy {

    private static final String TAG = NetProxy.class.getSimpleName();

    private static NetProxy sProxy = null;
    public synchronized static NetProxy getInstance (Context context) {
        if (sProxy == null) {
            sProxy = new NetProxy();
            if (sProxy.mClient == null) {
                sProxy.mClient = sProxy.buildClient(context.getApplicationContext());
            }
        }
        return sProxy;
    }

    private Context mContext = null;
    private MobileServiceClient mClient = null;

    private MobileServiceClient buildClient (Context context) {
        MobileServiceClient client = null;
        try {
            client = new MobileServiceClient(NetProperty.FORMAL, NetProperty.FORMAL_KEY, context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return client;
    }

    public <T> void doGetRequest(String request, final OnResponseListener listener) {
        if (mClient == null) {
            return;
        }
        final String requestUUID = UUID.randomUUID().toString();
        if (Debug.DEBUG && listener != null) {
            Log.v(TAG, "request_get(" + requestUUID + ")=" + request);
        }
        mClient.invokeApi(request, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> returnInfo, Exception e1, ServiceFilterResponse serviceFilterResponse) {
                if (serviceFilterResponse == null) {
                    return;
                }
                final String responseString = serviceFilterResponse.getContent();
                if (Debug.DEBUG) {
                    Log.v(TAG, "response(" + requestUUID + ")=" + responseString);
                }
                if (listener != null) {
                    listener.onResponse(responseString);
                }
            }
        });
    }

    public void postRequest (String request, Map<String, String> map, final OnResponseListener listener){
        if (mClient == null) {
            return;
        }
        List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
        if (map != null && !map.isEmpty()) {
            Set<String> set = map.keySet();
            for (String key : set) {
                Pair<String, String> pair = new Pair<String, String>(key, map.get(key));
                params.add(pair);
            }
        }
        final String requestUUID = UUID.randomUUID().toString();
        if (Debug.DEBUG && listener != null) {
            Log.v(TAG, "request_get(" + requestUUID + ")=" + request);
        }
        mClient.invokeApi(request, HttpPost.METHOD_NAME, params, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> returnInfo, Exception e1, ServiceFilterResponse serviceFilterResponse) {
                if (serviceFilterResponse == null) {
                    return;
                }
                final String responseString = serviceFilterResponse.getContent();
                if (Debug.DEBUG) {
                    Log.v(TAG, "response(" + requestUUID + ")=" + responseString);
                }
                if (listener != null) {
                    listener.onResponse(responseString);
                }
            }
        });
        //mClient.invokeApi();
    }

    public static interface OnResponseListener {
        public void onResponse (String result);
    }
}

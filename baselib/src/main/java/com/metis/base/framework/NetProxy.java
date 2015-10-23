package com.metis.base.framework;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.metis.base.Debug;
import com.metis.base.utils.Log;
import com.metis.msnetworklib.contract.OptionSettings;
import com.metis.msnetworklib.contract.ReturnInfo;
import com.microsoft.windowsazure.mobileservices.AndroidHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;

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

    private static final String UPLOAD_FILE = "v1.1/File/Upload?session={session}";/*?type={type}&define={define}&session={session}*/

    public static final int TYPE_IMAGE = 1, TYPE_VOICE = 2, TYPE_AMR = 3;

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
            client = new MobileServiceClient(NetProperty.USE, NetProperty.USE_KEY, context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return client;
    }

    public <T> String doGetRequest(String request, final OnResponseListener listener) {
        if (mClient == null) {
            return "mClient is null";
        }
        final String requestUUID = UUID.randomUUID().toString();
        Log.v(TAG, "request_get(" + requestUUID + ")=" + request);
        mClient.invokeApi(request, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> returnInfo, Exception e1, ServiceFilterResponse serviceFilterResponse) {
                if (serviceFilterResponse == null) {
                    Log.e(TAG, "response_get(" + requestUUID + ")=serviceFilterResponse is null");
                    return;
                }
                String responseString = serviceFilterResponse.getContent();
                Log.v(TAG, "response_get(" + requestUUID + ")=" + responseString);
                if (TextUtils.isEmpty(responseString)) {
                    OptionSettings optionSettings = new OptionSettings();
                    optionSettings.errorCode = "-10086";
                    optionSettings.message = "no data received from server";
                    optionSettings.status = "-10086";
                    ReturnInfo info = new ReturnInfo();
                    info.setOption(optionSettings);
                    responseString = new Gson().toJson(info);
                    Log.v(TAG, "response_get(" + requestUUID + ") after handling empty responseString=" + responseString);
                }
                if (listener != null) {
                    listener.onResponse(responseString, requestUUID);
                }
            }
        });
        return requestUUID;
    }

    public String doPostRequest (String request, Map<String, String> map, final OnResponseListener listener){
        if (mClient == null) {
            return "mClient is null";
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
        Log.v(TAG, "request_post(" + requestUUID + ")=" + request);
        StringBuilder builder = new StringBuilder();
        for (Pair<String, String> pair : params) {
            builder.append("&" + pair.first + "=" + pair.second);
        }
        Log.v(TAG, "request_post(" + requestUUID + ") params=" + builder.toString());
        mClient.invokeApi(request, HttpPost.METHOD_NAME, params, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> returnInfo, Exception e1, ServiceFilterResponse serviceFilterResponse) {
                if (serviceFilterResponse == null) {
                    Log.e(TAG, "response_post(" + requestUUID + ")=serviceFilterResponse is null");
                    return;
                }
                String responseString = serviceFilterResponse.getContent();
                Log.v(TAG, "response_post(" + requestUUID + ")=" + responseString);
                if (TextUtils.isEmpty(responseString)) {
                    OptionSettings optionSettings = new OptionSettings();
                    optionSettings.errorCode = "-10086";
                    optionSettings.message = "no data received from server";
                    optionSettings.status = "-10086";
                    ReturnInfo info = new ReturnInfo();
                    info.setOption(optionSettings);
                    responseString = new Gson().toJson(info);

                    Log.v(TAG, "response_post(" + requestUUID + ") after handling empty responseString=" + responseString);
                }
                if (listener != null) {
                    listener.onResponse(responseString, requestUUID);
                }
            }
        });
        return requestUUID;
        //mClient.invokeApi();
    }

    public String doPostRequest (String request, Object object, final OnResponseListener listener) {
        final String requestUUID = UUID.randomUUID().toString();
        Log.v(TAG, "request_post(" + requestUUID + ")=" + request);
        mClient.invokeApi(request, object, HttpPost.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> stringReturnInfo, Exception e1, ServiceFilterResponse serviceFilterResponse) {
                if (serviceFilterResponse == null) {
                    Log.e(TAG, "response_post(" + requestUUID + ")=serviceFilterResponse is null");
                    return;
                }
                String responseString = serviceFilterResponse.getContent();
                Log.v(TAG, "response_post(" + requestUUID + ")=" + responseString);
                if (TextUtils.isEmpty(responseString)) {
                    OptionSettings optionSettings = new OptionSettings();
                    optionSettings.errorCode = "-10086";
                    optionSettings.message = "no data received from server";
                    optionSettings.status = "-10086";
                    ReturnInfo info = new ReturnInfo();
                    info.setOption(optionSettings);
                    responseString = new Gson().toJson(info);

                    Log.v(TAG, "response_post(" + requestUUID + ") after handling empty responseString=" + responseString);
                }
                if (listener != null) {
                    listener.onResponse(responseString, requestUUID);
                }
            }
        });
        return requestUUID;
    }

    public void upload (int type, byte[] data, String session, NetProxy.OnResponseListener listener) {
        upload(type, new byte[][]{data}, session, listener);
    }

    public String upload (int type, byte[][] dataArray, String session, final NetProxy.OnResponseListener listener) {

        int totalLength = 0;
        final int count = dataArray.length;
        StringBuilder subLengthSb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            byte[] data = dataArray[i];
            totalLength += data.length;
            subLengthSb.append("," + data.length);
        }
        byte[] source = new byte[totalLength];
        int current = 0;
        for (int i = 0; i < count; i++) {
            byte[] data = dataArray[i];
            for (int k = 0; k < data.length; k++) {
                source[current] = data[k];
                current++;
            }
        }
        String request = UPLOAD_FILE.replace("{session}", session);
        String define = totalLength + "," + count + subLengthSb;
        List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
        Pair<String, String> typePair = new Pair<String, String>("type", type + "");
        Pair<String, String> definePair = new Pair<String, String>("define", define);
        list.add(typePair);
        list.add(definePair);
        final String requestUUID = UUID.randomUUID().toString();
        Log.v(TAG, "request_upload(" + requestUUID + ")=" + request);
        NetProxy.getInstance(mContext).getClient().invokeApi(
                request, source, HttpPost.METHOD_NAME, null, list, new ServiceFilterResponseCallback() {
                    @Override
                    public void onResponse(ServiceFilterResponse serviceFilterResponse, Exception e) {
                        if (listener != null && serviceFilterResponse != null) {
                            String responseString = serviceFilterResponse.getContent();
                            Log.v(TAG, "response_upload(" + requestUUID + ")=" + responseString);
                            listener.onResponse(responseString, requestUUID);
                            return;
                        }
                        if (listener != null && e != null) {
                            String responseString = e.getLocalizedMessage();
                            Log.v(TAG, "response_upload(" + requestUUID + ")=" + responseString);
                            listener.onResponse(responseString, requestUUID);
                            return;
                        }
                    }
                });
        return requestUUID;
    }

    public MobileServiceClient getClient() {
        return mClient;
    }

    public static interface OnResponseListener {
        public void onResponse (String result, String requestId);
    }
}

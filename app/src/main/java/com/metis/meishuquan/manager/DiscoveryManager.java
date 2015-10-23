package com.metis.meishuquan.manager;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.metis.base.framework.NetProxy;
import com.metis.base.manager.AbsManager;
import com.metis.base.manager.RequestCallback;
import com.metis.base.module.SimpleProvince;
import com.metis.base.utils.Log;
import com.metis.meishuquan.module.Area;
import com.metis.meishuquan.module.College;
import com.metis.meishuquan.module.DiscoveryItem;
import com.metis.msnetworklib.contract.ReturnInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Beak on 2015/10/16.
 */
public class DiscoveryManager extends AbsManager {

    private static final String URL_AREA_LIST = "v1.1/Institutions/GetAllInstitutionsCategoryList?typeid={typeid}",
    URL_COLLEGE_LIST = "v1.1/Institutions/GetInstitutionsInfoList?categoryid={categoryid}&page={page}&name={name}",
    URL_AVAILABLE_AREA_LIST = "v1.1/MockExams/GetOpenedAreaList",
    URL_GET_WEB_MODULE = "v1.1/UserCenter/GetWebModule?typeid={typeid}";

    private static final String TAG = DiscoveryManager.class.getSimpleName();

    private static DiscoveryManager sManager = null;

    public static synchronized DiscoveryManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new DiscoveryManager(context.getApplicationContext());
        }
        return sManager;
    }

    private DiscoveryManager(Context context) {
        super(context);
    }

    /**
     * @param typeId 0 院校分类，1 高分卷年度分类，2 设计造型分类，3 素描色彩分类
     */
    public void getAreaList (int typeId, final RequestCallback<List<Area>> callback) {
        String request = URL_AREA_LIST.replace("{typeid}", typeId + "");
        NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                //Log.v(TAG, "result(90,100)=" + result.substring(90, result.length()));
                ReturnInfo<List<Area>> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<List<Area>>>() {
                }.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    public String getCollegeList (long categoryid, int page, String name, final RequestCallback<List<College>> callback) {
        String request = null;
        try {
            request = URL_COLLEGE_LIST
                    .replace("{categoryid}", categoryid + "")
                    .replace("{page}", page + "")
                    .replace("{name}", URLEncoder.encode(name, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<List<College>> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<List<College>>>() {
                }.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    public String getAvailableAreaList (final RequestCallback<List<SimpleProvince>> callback) {
        String request = URL_AVAILABLE_AREA_LIST;
        Log.v(TAG, "getAvailableAreaList request=" + request);
        return NetProxy.getInstance(getContext()).doGetRequest(request, new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<List<SimpleProvince>> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<List<SimpleProvince>>>(){}.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }

    public void getWebModule (final int typeId, final RequestCallback<List<DiscoveryItem>> callback) {
        NetProxy.getInstance(getContext()).doGetRequest(URL_GET_WEB_MODULE.replace("{typeid}", typeId + ""), new NetProxy.OnResponseListener() {
            @Override
            public void onResponse(String result, String requestId) {
                ReturnInfo<List<DiscoveryItem>> returnInfo = getGson().fromJson(result, new TypeToken<ReturnInfo<List<DiscoveryItem>>>(){}.getType());
                if (callback != null) {
                    callback.callback(returnInfo, requestId);
                }
            }
        });
    }
}

package com.metis.msnetworklib.framework.network;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by wudi on 3/15/2015.
 */
public class HttpParameter
{
    public String url;
    public List<NameValuePair> params;
    public HttpMethod httpMethod;
    public String uploadPath;
}

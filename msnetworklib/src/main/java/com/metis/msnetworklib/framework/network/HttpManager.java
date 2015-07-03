package com.metis.msnetworklib.framework.network;

import android.annotation.SuppressLint;

import com.metis.msnetworklib.framework.runner.AsyncResult;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by wudi on 3/15/2015.
 */
public class HttpManager
{
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

    public static HttpResponse getHttpResponse(String url) throws IOException
    {
        return getHttpResponse(url, null);
    }

    public static HttpResponse getHttpResponse(String url, List<NameValuePair> params) throws IOException
    {
        String urlStr = url;
        if (params != null && params.size() > 0)
        {
            String paramStr = URLEncodedUtils.format(params, "UTF-8");
            if (url.indexOf("?") == -1)
            {
                urlStr += "?" + paramStr;
            }
            else
            {
                urlStr += "&" + paramStr;
            }
        }
        HttpClient client = HttpClientFactory.getHttpClient();
        HttpGet httpGet = new HttpGet(urlStr);

        try
        {
            return client.execute(httpGet);
        }
        catch (IOException e)
        {
            if (httpGet != null)
            {
                httpGet.abort();
            }
            // Log.e("HttpManager", "failed to openUrl URL: " + url
            // + ";  exception:" + e.getMessage());
            throw e;
        }
    }

    public static AsyncResult<String> postHttpResultWithStatus(String url) throws IOException
    {
        AsyncResult<String> result = new AsyncResult<String>();
        HttpResponse response = postHttpResponse(url);
        StatusLine status = response.getStatusLine();
        result.setStatusCode(status.getStatusCode());
        result.setResult(readHttpResponse(response));
        return result;
    }

    public static HttpResponse postHttpResponse(String url) throws IOException
    {
        return postHttpResponse(url, null);
    }

    public static String postHttpResult(String url, List<NameValuePair> params) throws IOException
    {
        HttpResponse response = postHttpResponse(url, params);
        return readHttpResponse(response);
    }

    public static HttpResponse postHttpResponse(String url, List<NameValuePair> params) throws IOException
    {
        HttpClient client = HttpClientFactory.getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        if (params != null && params.size() > 0)
        {
            httpPost.setHeader("Content-Type", CONTENT_TYPE);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(entity);
        }

        try
        {
            return client.execute(httpPost);
        }
        catch (IOException e)
        {
            if (httpPost != null)
            {
                httpPost.abort();
            }
            // Log.e("HttpManager", "failed to openUrl URL: " + url
            // + ";  exception:" + e.getMessage());
            throw e;
        }
    }

    @SuppressLint("DefaultLocale")
    private static ByteArrayOutputStream readOutputStream(HttpResponse response)
    {
        HttpEntity entity = response.getEntity();
        if (entity == null)
        {
            return null;
        }
        InputStream inputStream = null;
        try
        {
            inputStream = entity.getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();

            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null && header.getValue().toLowerCase().indexOf("gzip") != -1)
            {
                inputStream = new GZIPInputStream(inputStream);
            }

            int readBytes = 0;
            byte[] sBuffer = new byte[512];
            while ((readBytes = inputStream.read(sBuffer)) != -1)
            {
                content.write(sBuffer, 0, readBytes);
            }

            return content;
        }
        catch (IllegalStateException e)
        {
        }
        catch (IOException e)
        {
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                }
            }
            try
            {
                entity.consumeContent();
            }
            catch (Exception e)
            {
            }
        }
        return null;
    }

    private static String readHttpResponse(HttpResponse response)
    {
        ByteArrayOutputStream out = readOutputStream(response);

        String result = (out == null) ? null : new String(out.toByteArray());
        try
        {
            if (out != null)
            {
                out.close();
            }
        }
        catch (IOException e)
        {
        }
        return result;
    }

    static String getBoundry()
    {
        StringBuffer _sb = new StringBuffer();
        for (int t = 1; t < 12; t++)
        {
            long time = System.currentTimeMillis() + t;
            if (time % 3 == 0)
            {
                _sb.append((char) time % 9);
            }
            else if (time % 3 == 1)
            {
                _sb.append((char) (65 + time % 26));
            }
            else
            {
                _sb.append((char) (97 + time % 26));
            }
        }
        return _sb.toString();
    }
}
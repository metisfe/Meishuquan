package com.metis.msnetworklib.contract;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by wudi on 3/15/2015.
 */
public class ContractUtility {
    private static String DataTypeKey = "DataType";
    private static String ShowTypeKey = "ShowType";

    public static SimpleDateFormat DateFormatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
    static
    {
        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        if (utcTimeZone != null)
        {
            DateFormatter.setTimeZone(utcTimeZone);
        }
    }

    public interface ArrayItemParser<T>
    {
        public T parse(JSONObject jsonObject);
    }

    public static <T> boolean equals(ContractBase<T> source, ContractBase<T> target)
    {
        return calculateDifferentItemCountBetweenOldAndNewData(source, target) == 0
                && calculateDifferentItemCountBetweenOldAndNewData(target, source) == 0;
    }

    public static <T> List<T> getArrayItems(JSONObject jsonObject, String arrayItemsKey, ArrayItemParser<T> parser)
    {
        if (parser == null || jsonObject == null || arrayItemsKey == null || arrayItemsKey.length() == 0)
        {
            return null;
        }

        JSONArray jsonArray = jsonObject.optJSONArray(arrayItemsKey);
        if (jsonArray == null)
        {
            return null;
        }

        int arraySize = jsonArray.length();
        if (arraySize == 0)
        {
            return null;
        }

        ArrayList<T> result = new ArrayList<T>(arraySize);

        for (int i = 0; i < arraySize; i++)
        {
            JSONObject itemJsonObject = jsonArray.optJSONObject(i);
            if (itemJsonObject == null)
            {
                continue;
            }

            T one = parser.parse(itemJsonObject);
            if (one == null)
            {
                continue;
            }

            result.add(one);
        }

        return result;
    }

    public static Date getDate(JSONObject jsonObject, String key, Date defaultValue)
    {
        return getDate(jsonObject, DateFormatter, key, defaultValue);
    }

    public static Date getDate(JSONObject jsonObject, SimpleDateFormat formatter, String key, Date defaultValue)
    {
        if (jsonObject == null || key == null || formatter == null)
        {
            return defaultValue;
        }

        Date result = defaultValue;
        try
        {
            String tempValue = getString(jsonObject, key, null);
            if (tempValue != null)
            {
                result = formatter.parse(tempValue);
            }
        }
        catch (ParseException ex)
        {
            result = defaultValue;
        }

        return result;
    }

    public static List<URL> getUrls(JSONObject jsonObject, String key, List<URL> defaultValue)
    {
        if (jsonObject == null || key == null)
        {
            return defaultValue;
        }

        JSONArray jsonArray = jsonObject.optJSONArray(key);
        if (jsonArray == null)
        {
            return defaultValue;
        }

        int itemSize = jsonArray.length();
        List<URL> result = new ArrayList<URL>(itemSize);

        if (itemSize == 0)
        {
            return result;
        }

        for (int i = 0; i < itemSize; i++)
        {
            URL url = stringToUrl(getString(jsonArray, i, null));
            if (url == null)
            {
                continue;
            }

            result.add(url);
        }

        return result;
    }

    public static URL getUrl(JSONObject jsonObject, String key, URL defaultValue)
    {
        if (jsonObject == null || key == null)
        {
            return defaultValue;
        }

        URL result = stringToUrl(getString(jsonObject, key, null));

        return (result == null) ? defaultValue : result;
    }

    public static String getString(JSONArray jsonArray, int index, String defaultValue)
    {
        if (jsonArray == null || index < 0)
        {
            return defaultValue;
        }

        if (jsonArray.isNull(index))
        {
            return defaultValue;
        }

        return jsonArray.optString(index, defaultValue);
    }

    public static boolean getBoolean(JSONObject jsonObject, String key, boolean defaultValue)
    {
        if (jsonObject == null || key == null)
        {
            return defaultValue;
        }

        if (jsonObject.isNull(key))
        {
            return defaultValue;
        }

        return jsonObject.optBoolean(key, defaultValue);
    }

    public static int getInt(JSONObject jsonObject, String key, int defaultValue)
    {
        return getInt(jsonObject, key, defaultValue, Integer.MIN_VALUE);
    }

    public static int getInt(JSONObject jsonObject, String key, int defaultValue, int validMinimalValue)
    {
        if (jsonObject == null || key == null)
        {
            return defaultValue;
        }

        if (jsonObject.isNull(key))
        {
            return defaultValue;
        }

        int result = jsonObject.optInt(key, defaultValue);
        return result < validMinimalValue ? validMinimalValue : result;
    }

    public static long getLong(JSONObject jsonObject, String key, long defaultValue)
    {
        return getLong(jsonObject, key, defaultValue, Long.MIN_VALUE);
    }

    public static long getLong(JSONObject jsonObject, String key, long defaultValue, long validMinimalValue)
    {
        if (jsonObject == null || key == null)
        {
            return defaultValue;
        }

        if (jsonObject.isNull(key))
        {
            return defaultValue;
        }

        long result = jsonObject.optLong(key, defaultValue);
        return result < validMinimalValue ? validMinimalValue : result;
    }

    public static float getFloat(JSONObject jsonObject, String key, float defaultValue)
    {
        if (jsonObject == null || key == null)
        {
            return defaultValue;
        }

        if (jsonObject.isNull(key))
        {
            return defaultValue;
        }

        return (float) jsonObject.optDouble(key, defaultValue);
    }

    public static String getString(JSONObject jsonObject, String key, String defaultValue)
    {
        return getString(jsonObject, key, defaultValue, false);
    }

    public static String getString(JSONObject jsonObject, String key, String defaultValue, boolean shouldTrimLineBreak)
    {
        if (jsonObject == null || key == null)
        {
            return defaultValue;
        }

        if (jsonObject.isNull(key))
        {
            return defaultValue;
        }

        String value = jsonObject.optString(key, defaultValue);

        if (shouldTrimLineBreak)
        {
            int trimedStringLength = value.length();
            for(int i = value.length() - 1; i >= 0; i++)
            {
                if (value.indexOf(i) != '\n')
                {
                    trimedStringLength = i + 1;
                    break;
                }
            }

            return (trimedStringLength < value.length()) ? value.substring(0, trimedStringLength) : value;
        }

        return value;
    }

    public static URL stringToUrl(String value)
    {
        if (TextUtils.isEmpty(value))
        {
            return null;
        }

        URL result = null;
        try
        {
            result = new URL(value);
        }
        catch (MalformedURLException ex)
        {
            result = null;
        }
        return result;
    }

    private static <T> int calculateDifferentItemCountBetweenOldAndNewData(ContractBase<T> oldData, ContractBase<T> newData)
    {
        if (newData == null)
        {
            return 0;
        }

        if (oldData == null)
        {
            return newData.getCurrentSize();
        }

        return newData.calculateDifferentItemCount(oldData);
    }

}

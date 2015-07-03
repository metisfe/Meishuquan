package com.metis.msnetworklib.contract;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by wudi on 3/15/2015.
 */

public final class Pagination implements Serializable
{
    private static final long serialVersionUID = 1L;

    private static String HasMoreKey = "HasMore";
    public static String StartKey = "Start";
    public static String VersionKey = "Version";
    public static String DefaultPaginationObjectKey = "Pagination";

    public boolean HasMore;
    public String Start;
    public String Version;

    public static Pagination parse(JSONObject jsonObject)
    {
        return parse(jsonObject, DefaultPaginationObjectKey);
    }

    public static Pagination parse(JSONObject jsonObject, String paginationKey)
    {
        if (jsonObject == null)
        {
            return null;
        }

        JSONObject rootObject = jsonObject.optJSONObject(paginationKey);
        if (rootObject != null)
        {
            // Override with Pagination Object
            // In most case, pagiantion is contained in other object,
            // This's why we need to check if pagination is single object or
            // merged with others
            jsonObject = rootObject;
        }

        Pagination pagination = new Pagination();
        pagination.HasMore = jsonObject.optBoolean(HasMoreKey, false);
        pagination.Start = ContractUtility.getString(jsonObject, StartKey, null);
        pagination.Version = ContractUtility.getString(jsonObject, VersionKey, null);

        return pagination.isValid() ? pagination : null;
    }

    public static Pagination defaultValue()
    {
        final Pagination pagination = new Pagination();

        pagination.HasMore = false;
        pagination.Start = "";
        pagination.Version = "";

        return pagination;
    }

    private Pagination()
    {

    }

    private boolean isValid()
    {
        return !this.HasMore || (this.Start != null && this.Version != null);
    }
}
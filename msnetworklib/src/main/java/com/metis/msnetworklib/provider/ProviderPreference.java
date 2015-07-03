package com.metis.msnetworklib.provider;

/**
 * Created by wudi on 3/15/2015.
 */
public enum ProviderPreference
{
    /*
     * Load data from Cache only.
     */
    CacheOnly,

    /*
     * Load data from Api only
     */
    ApiOnly,

    /*
     * Load data from Api firstly, but will fall-back to cache
     * As long as Api call failed.
     */
    PreferApi,
}
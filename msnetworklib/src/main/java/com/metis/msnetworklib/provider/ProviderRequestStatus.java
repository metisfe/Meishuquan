package com.metis.msnetworklib.provider;

/**
 * Created by wudi on 3/15/2015.
 */
public enum ProviderRequestStatus
{
    Load_Succeeded_From_Api,
    Load_Succeeded_From_Cache,
    Load_Succeeded,
    Load_Failed,
    Load_Failed_DueTo_NoInternetConnection,
    Load_Failed_DueTo_InvalidJSONResult,
    Load_Failed_DueTo_WrongInputParameters,
    Load_Failed_DueTo_Exception,
    Load_Failed_DueTo_NoMoreResult,
    Load_Failed_DueTo_IncompatibleResult,
    Load_Failed_DueTo_CacheMissing,
}
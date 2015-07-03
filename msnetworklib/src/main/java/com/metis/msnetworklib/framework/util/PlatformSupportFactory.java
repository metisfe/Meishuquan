package com.metis.msnetworklib.framework.util;

import android.os.Build;
import android.util.Log;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by wudi on 3/15/2015.
 */
public abstract class PlatformSupportFactory<T>
{
    private final Class<T> managedInterface;
    private final T defaultImplementation;
    private final SortedMap<Integer, String> implementations;

    protected PlatformSupportFactory(Class<T> managedInterface, T defaultImplementation)
    {
        this.managedInterface = managedInterface;
        this.defaultImplementation = defaultImplementation;
        this.implementations = new TreeMap<Integer, String>(Collections.reverseOrder());
    }

    protected void addImplementationClass(int minVersion, String className)
    {
        implementations.put(minVersion, className);
    }

    public T build()
    {
        for (Integer minVersion : implementations.keySet())
        {
            if (Build.VERSION.SDK_INT >= minVersion)
            {
                String className = implementations.get(minVersion);
                try
                {
                    Class<? extends T> clazz = Class.forName(className).asSubclass(managedInterface);
                    return clazz.getConstructor().newInstance();
                }
                catch (Exception e)
                {
                    Log.w("PlatformSupportFactory", e.getMessage());
                }
            }
        }
        return defaultImplementation;
    }
}

package com.metis.base.widget.delegate;

import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.util.SparseIntArray;

import com.metis.base.Debug;
import com.metis.base.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Beak on 2015/7/2.
 */
public class TypeLayoutProvider {

    public static final int TYPE_NONE = 0;
    public static final int LAYOUT_TYPE_NONE = R.layout.layout_base_type_none;

    /**
     * key type
     * value layout resource id
     */
    private static SparseIntArray sTypeLayoutIdArray = new SparseIntArray();

    static {
        put(TYPE_NONE, LAYOUT_TYPE_NONE);
    }

    private TypeLayoutProvider () {
    }

    private static boolean put (int type, @LayoutRes int layoutId) {
        if (sTypeLayoutIdArray.indexOfKey(type) >= 0) {
            if (sTypeLayoutIdArray.get(type) == layoutId) {
                return true;
            } else {
                if (Debug.DEBUG) {
                    throw new IllegalArgumentException("a different value has exist for type:" + type);
                }

            }
        } else {
            sTypeLayoutIdArray.put(type, layoutId);
        }
        return sTypeLayoutIdArray.indexOfValue(layoutId) >= 0;
    }

    public static int getLayoutResource (int type) {
        if (sTypeLayoutIdArray.indexOfKey(type) >= 0) {
            return sTypeLayoutIdArray.get(type);
        }
        if (Debug.DEBUG) {
            throw new Resources.NotFoundException("can not found layout resource for type:" + type);
        }
        return LAYOUT_TYPE_NONE;
    }
}

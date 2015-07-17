package com.metis.base.widget.adapter.delegate;

import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.util.SparseIntArray;

import com.metis.base.Debug;
import com.metis.base.utils.Log;

/**
 * Created by Beak on 2015/7/2.
 */
public class TypeLayoutProvider {

    private static final String TAG = TypeLayoutProvider.class.getSimpleName();

    /**
     * key type
     * value layout resource id
     */
    private static SparseIntArray sTypeLayoutIdArray = new SparseIntArray();

    private TypeLayoutProvider () {
    }

    public static boolean put (int type, @LayoutRes int layoutId) {
        Log.v(TAG, "put type=" + type + " layoutId=" + layoutId);
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
        return DelegateType.TYPE_NONE.getLayoutResId();
    }
}

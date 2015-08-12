package com.metis.base.utils;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

/**
 * Created by Beak on 2015/7/6.
 */
public class FragmentUtils {

    private static final String TAG = FragmentUtils.class.getSimpleName();

    public static void showFragment (FragmentManager manager, Fragment fragment, @IdRes int at) {
        showFragment(manager, fragment, at, false);
    }
    public static void showFragment (FragmentManager manager, Fragment fragment, @IdRes int at, boolean addToBackStack) {
        FragmentTransaction ft = manager.beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(at, fragment);
        }
        if (addToBackStack) {
            boolean allow = ft.isAddToBackStackAllowed();
            Log.v(TAG, "allow=" + allow);
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public static void hideFragment (FragmentManager manager, Fragment fragment) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.hide(fragment);
        ft.commit();
    }

    public static void removeFragment (FragmentManager manager, Fragment fragment) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
}

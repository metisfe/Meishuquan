package com.metis.base.utils;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Beak on 2015/7/6.
 */
public class FragmentUtils {
    public static void showFragment (FragmentManager manager, Fragment fragment, @IdRes int at) {
        FragmentTransaction ft = manager.beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(at, fragment);
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

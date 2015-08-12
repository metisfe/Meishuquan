package com.metis.base.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Beak on 2015/8/10.
 */
public class SystemUtils {
    private SystemUtils () {}

    public static void hideIME (Context context, EditText text) {
        InputMethodManager manager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(text.getWindowToken(), 0);
    }

    public static void showIME (Context context, View view) {
        InputMethodManager manager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(view, 0);
    }
}

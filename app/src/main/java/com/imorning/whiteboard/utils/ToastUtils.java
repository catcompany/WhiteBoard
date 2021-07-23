package com.imorning.whiteboard.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * A simple toast utils.
 *
 * @author iMorning
 */
public class ToastUtils {

    /**
     * show toast for a msg msg
     * @param context The context
     * @param msg message
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * show toast from recourse
     * @param context Context
     * @param stringId The msg id in string.xml
     */
    public static void showToast(Context context, int stringId) {
        Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
    }
}

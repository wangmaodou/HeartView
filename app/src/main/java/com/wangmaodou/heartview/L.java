package com.wangmaodou.heartview;

import android.util.Log;

/**
 * Created by Maodou on 2016/8/17.
 */
public class L {

    private static boolean isDebug=true;

    public static void d(String logString){
        if (isDebug)
            Log.d("=====",logString);
    }
}

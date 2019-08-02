package com.yjy.matrixplugin.utils;

import android.os.Looper;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/30
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class Utils {
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}

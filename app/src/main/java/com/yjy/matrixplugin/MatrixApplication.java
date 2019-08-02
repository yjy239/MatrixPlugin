package com.yjy.matrixplugin;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/29
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class MatrixApplication extends Application {

    private static Context sContext;
    public static Context getContext() {
        return sContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        MatrixPlugin.initMatrix(this,"com.yjy.matrixplugin.SplashActivity");
    }
}

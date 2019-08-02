package com.yjy.matrixplugin.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;

import java.io.FileInputStream;
import java.util.List;

import static android.content.pm.PackageManager.GET_SERVICES;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/29
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class SystemUtils {
    /**
     * 返回当前的进程名
     *
     * @return
     */
    public static boolean isReportProcess(Context context,Class<? extends ContentProvider> providerClass) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PROVIDERS);
        } catch (Exception e) {
            return false;
        }
        String mainProcess = packageInfo.applicationInfo.processName;

        ComponentName component = new ComponentName(context, providerClass);
        ProviderInfo providerInfo;
        try {
            providerInfo = packageManager.getProviderInfo(component, PackageManager.GET_DISABLED_COMPONENTS);
        } catch (PackageManager.NameNotFoundException ignored) {
            // Service is disabled.
            return false;
        }

        if (providerInfo.processName.equals(mainProcess)) {
            // Technically we are in the service process, but we're not in the service dedicated process.
            return false;
        }

        int myPid = android.os.Process.myPid();
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningAppProcessInfo myProcess = null;
        List<ActivityManager.RunningAppProcessInfo> runningProcesses;
        try {
            runningProcesses = activityManager.getRunningAppProcesses();
        } catch (SecurityException exception) {
            // https://github.com/square/leakcanary/issues/948
            return false;
        }
        if (runningProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo process : runningProcesses) {
                if (process.pid == myPid) {
                    myProcess = process;
                    break;
                }
            }
        }
        if (myProcess == null) {
            return false;
        }

        return myProcess.processName.equals(providerInfo.processName);
    }

    public static boolean isMainProcess(Context context,Class<? extends Activity> activityClass) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
            return false;
        }
        String mainProcess = packageInfo.applicationInfo.processName;

        ComponentName component = new ComponentName(context, activityClass);
        ActivityInfo activityInfo;
        try {
            activityInfo = packageManager.getActivityInfo(component, PackageManager.GET_DISABLED_COMPONENTS);
        } catch (PackageManager.NameNotFoundException ignored) {
            // Service is disabled.
            return false;
        }

        if (activityInfo.processName.equals(mainProcess)) {
            // Technically we are in the service process, but we're not in the service dedicated process.
            return true;
        }

        int myPid = android.os.Process.myPid();
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningAppProcessInfo myProcess = null;
        List<ActivityManager.RunningAppProcessInfo> runningProcesses;
        try {
            runningProcesses = activityManager.getRunningAppProcesses();
        } catch (SecurityException exception) {
            // https://github.com/square/leakcanary/issues/948
            return false;
        }
        if (runningProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo process : runningProcesses) {
                if (process.pid == myPid) {
                    myProcess = process;
                    break;
                }
            }
        }
        if (myProcess == null) {
            return false;
        }

        return myProcess.processName.equals(activityInfo.processName);
    }

}

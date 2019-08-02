package com.yjy.matrixplugin;

import android.content.Context;
import android.database.Cursor;
import android.os.IBinder;
import android.os.RemoteException;

import com.tencent.matrix.report.Issue;
import com.yjy.matrixplugin.binder.ParsedBinder;
import com.yjy.matrixplugin.binder.ParsedCallBack;
import com.yjy.matrixplugin.issue.PluginIssue;
import com.yjy.matrixplugin.matrixserver.PluginContentProvider;
import com.yjy.matrixplugin.matrixserver.data.BinderCursor;
import com.yjy.matrixplugin.matrixserver.data.EvilMethod;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/30
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
class MatrixPluginClient {

    static ParsedBinder.ParsedBinderProxy sRemoteInterface;


    static void linkToReportProcess(Context context){
        if(context == null||sRemoteInterface != null){
            return;
        }
        Cursor cursor = context.getContentResolver().query(PluginContentProvider.BINDER,null,null,null,null);

        if(cursor != null){
            while (cursor.moveToNext()){

            }

            IBinder binder = BinderCursor.getExtra(cursor);
            cursor.close();

            if(binder != null){
                try {
                    binder.linkToDeath(new IBinder.DeathRecipient() {
                        @Override
                        public void binderDied() {
                            //发现断开了，要设置为空
                            sRemoteInterface = null;
                        }
                    },0);
                }catch (RemoteException e){
                    e.printStackTrace();
                }

                sRemoteInterface = new ParsedBinder.ParsedBinderProxy(binder);

            }


        }

    }

    public static void setOnParsedCompleteListener(ParsedCallBack callBack){
        if(sRemoteInterface != null){
            sRemoteInterface.setOnParseCallback(callBack);
        }
    }


    static void setMethodFilePath(String path){
        if(sRemoteInterface == null){
            return;
        }
        try {
            sRemoteInterface.setMethodMappingPath(path);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static void sendUnParsedStack(PluginIssue pluginIssue){
        if(sRemoteInterface == null){
            return;
        }
        try {
            sRemoteInterface.sendToParsedContent(pluginIssue);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

package com.yjy.matrixplugin;

import android.app.Application;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tencent.matrix.Matrix;
import com.tencent.matrix.iocanary.IOCanaryPlugin;
import com.tencent.matrix.iocanary.config.IOConfig;
import com.tencent.matrix.resource.ResourcePlugin;
import com.tencent.matrix.resource.config.ResourceConfig;
import com.tencent.matrix.trace.TracePlugin;
import com.tencent.matrix.trace.config.TraceConfig;
import com.tencent.matrix.util.MatrixLog;
import com.tencent.sqlitelint.SQLiteLint;
import com.tencent.sqlitelint.SQLiteLintPlugin;
import com.tencent.sqlitelint.config.SQLiteLintConfig;
import com.yjy.matrixplugin.binder.ParsedCallBack;
import com.yjy.matrixplugin.component.IssuesListActivity;
import com.yjy.matrixplugin.matrixserver.PluginContentProvider;
import com.yjy.matrixplugin.utils.SystemUtils;

import java.io.File;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/29
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class MatrixPlugin {



    public static void initMatrix(Application client,String splash){
        //会启动异步解析进程
        if(SystemUtils.isMainProcess(client, IssuesListActivity.class) ){
            initClient(client,splash);
        }

    }


    private final static File methodFilePath = new File(Environment.getExternalStorageDirectory(), "pluginMapping.txt");

    private static void initClient(Application context,@NonNull String splash){
        DynamicConfig dynamicConfig = new DynamicConfig();
        Matrix.Builder builder = new Matrix.Builder(context);
        builder.patchListener(new PluginReporterListener(context));
        TraceConfig traceConfig = new TraceConfig.Builder()
                .dynamicConfig(dynamicConfig)
                .enableFPS(true)
                .enableEvilMethodTrace(true)
                .enableAnrTrace(true)
                .enableStartup(true)
                .splashActivities(splash)
                .isDebug(true)
                .isDevEnv(false)
                .build();

        TracePlugin tracePlugin = (new TracePlugin(traceConfig));
        builder.plugin(tracePlugin);



        //resource
        builder.plugin(new ResourcePlugin(new ResourceConfig.Builder()
                .dynamicConfig(dynamicConfig)
                .setDumpHprof(false)
                .setDetectDebuger(true)     //only set true when in sample, not in your app
                .build()));
        ResourcePlugin.activityLeakFixer(context);

        //io
        IOCanaryPlugin ioCanaryPlugin = new IOCanaryPlugin(new IOConfig.Builder()
                .dynamicConfig(dynamicConfig)
                .build());
        builder.plugin(ioCanaryPlugin);


        // prevent api 19 UnsatisfiedLinkError
        //sqlite
        SQLiteLintConfig config = initSQLiteLintConfig();
        SQLiteLintPlugin sqLiteLintPlugin = new SQLiteLintPlugin(config);
        builder.plugin(sqLiteLintPlugin);


        Matrix.init(builder.build());

        tracePlugin.start();
        ioCanaryPlugin.start();
        sqLiteLintPlugin.start();

        MatrixLog.i("Matrix.HackCallback", "end:%s", System.currentTimeMillis());


        MatrixPluginClient.linkToReportProcess(context);

//        MatrixPluginClient.setOnParsedCompleteListener(new ParsedCallBack() {
//            @Override
//            public void finish(String content) {
//                Log.e("callback",content);
//            }
//        });


        MatrixPluginClient.setMethodFilePath(methodFilePath.getAbsolutePath());

    }




    private static SQLiteLintConfig initSQLiteLintConfig() {
        try {
            return new SQLiteLintConfig(SQLiteLint.SqlExecutionCallbackMode.CUSTOM_NOTIFY);
        } catch (Throwable t) {
            return new SQLiteLintConfig(SQLiteLint.SqlExecutionCallbackMode.CUSTOM_NOTIFY);
        }
    }

}

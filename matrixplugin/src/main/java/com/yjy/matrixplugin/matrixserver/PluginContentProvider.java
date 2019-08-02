package com.yjy.matrixplugin.matrixserver;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yjy.matrixplugin.matrixserver.data.BinderCursor;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/29
 *     desc   :启动常驻进程入口，跨进程初始化MethodMapping
 *     承担了类似Android系统的service_manager的角色，能够跨端找Binder
 *     思想来源于RePlugin，可以看本人的文章:https://www.jianshu.com/p/d824056f510b
 *     也能通过Service去做。
 *     github:yjy239@gitub.com
 * </pre>
 */
public class PluginContentProvider extends ContentProvider {

    private static final String TAG = PluginContentProvider.class.getName();
    public static final String AUTH = "content://matrixplugin.component.plugin.report";
    public static final String AUTH_PREFIX = "matrixplugin.component.plugin.report";
    public static UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static Uri BINDER = Uri.parse(AUTH + "/binder");
    public static Uri METHOD_MAP = Uri.parse(AUTH + "/method_map");

    public static int BINDER_CODE = 0;
    public static int METHOD_CODE = 1;


    private MatrixPluginServer server = new MatrixPluginServer();

    static {
        sMatcher.addURI(AUTH_PREFIX,"binder",BINDER_CODE);
    }


    @Override
    public boolean onCreate() {
        Log.e("PluginContentProvider","------------------------create-----------------");
        //reading Map
        server = new MatrixPluginServer();
        return true;
    }

    /**
     * 思考,如果出现了Binder驱动传输的时候，binder底层内存不足怎么办？
     *   可以用浮标读取数据,但是这样就可能会有问题，
     *   假如重新设置了路径，重新读取数据将会可能拿到空数据，
     *   那就通过Binder跨进程,关于异步还是同步的问题。
     *   如果此时正在解析文件怎么办？难道要主进程等待？很明显设计不合理，所以只能选择使用异步处理。
     *   但是异步aidl要传进去callback，可能会引用到其他没有的实现Parcel的类，怎么办？
     *   只能手写Binder解决，只有手写binder才有着aidl的生成的Binder没有的灵活性。
     *
     *   异步回调，原理可以看我的博文，优先度是最低的。
     * @param uri 根据Uri来判断获取的是Binder还是数据
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Log.e(TAG,"------------read "+uri.toString()+"--------------");
        if(sMatcher.match(uri) == BINDER_CODE){
            Log.e(TAG,"------------read Binder---------");
            return new BinderCursor(new String[]{"binder"},server);
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

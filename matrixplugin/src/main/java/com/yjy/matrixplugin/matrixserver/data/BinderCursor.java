package com.yjy.matrixplugin.matrixserver.data;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/30
 *     desc   :为了能够通过ContentProvider返回Binder对象，需要用浮标包装一下
 *     github:yjy239@gitub.com
 * </pre>
 */
public class BinderCursor extends MatrixCursor {

    private Bundle mBinderExtra = new Bundle();
    private static final String BINDER_KEY = "binder";

    public BinderCursor(String[] columnNames,IBinder binder) {
        super(columnNames);
        if (binder != null) {
            Parcelable value = new BinderParcel(binder);
            mBinderExtra.putParcelable(BINDER_KEY, value);
        }
    }

    /**
     * 需要传输的Binder Parcel
     *
     * Parcel 本身内置了Binder的读写，感兴趣的，去看看我的binder系列文章
     * {@link https://www.jianshu.com/p/ba0a34826b27}
     */
    public static class BinderParcel implements Parcelable{
        IBinder mBinder;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStrongBinder(mBinder);
        }

        BinderParcel(IBinder mBinder) {
            this.mBinder = mBinder;
        }

        BinderParcel(Parcel in) {
            this.mBinder = in.readStrongBinder();
        }

        public static final Creator<BinderParcel> CREATOR = new Creator<BinderParcel>() {
            @Override
            public BinderParcel createFromParcel(Parcel source) {
                return new BinderParcel(source);
            }

            @Override
            public BinderParcel[] newArray(int size) {
                return new BinderParcel[size];
            }
        };
    }


    /**
     * 为浮标新增额外数据
     * @return
     */
    @Override
    public Bundle getExtras() {
        return mBinderExtra;
    }

    public static IBinder getExtra(Cursor cursor){
        Bundle bundle = cursor.getExtras();
        //android.os.BadParcelableException: ClassNotFoundException when unmarshalling
        bundle.setClassLoader(BinderCursor.class.getClassLoader());
        BinderParcel parcel = (BinderParcel)bundle.getParcelable(BINDER_KEY);
        return parcel.mBinder;
    }
}

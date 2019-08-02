package com.yjy.matrixplugin.binder;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tencent.matrix.report.Issue;
import com.yjy.matrixplugin.issue.PluginIssue;
import com.yjy.matrixplugin.matrixserver.data.EvilMethod;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/31
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public abstract class ParsedBinder extends Binder implements IParseInterface {

    //接口放出去，让客户端实现，此时的客户端是解析进程

    private static final java.lang.String DESCRIPTOR = "com.yjy.matrixplugin.binder.ParsedBinder";
    private static final java.lang.String PROXY_DESCRIPTOR = "com.yjy.matrixplugin.binder.ParsedBinderProxy";
    static final int TRANSACTION_setMethodMappingPath = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_sendParsedContent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_replyParsedContent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);

    private IBinder mAppBinder;

    public ParsedBinder(){
        attachInterface(this,DESCRIPTOR);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    /**
     * 回复App，解析之后的数据
     */
    public void replyToApp(PluginIssue issue) throws RemoteException{
        if(mAppBinder == null){
            return;
        }
        Parcel data = android.os.Parcel.obtain();
        Parcel reply = android.os.Parcel.obtain();
        try{
            data.writeInterfaceToken(PROXY_DESCRIPTOR);
            data.writeParcelable(issue,0);
            mAppBinder.transact(TRANSACTION_replyParsedContent,data,reply,0);
        }finally {
            data.recycle();
            reply.recycle();
        }
    }


    private void initBinder(){
        try{
            mAppBinder.linkToDeath(new DeathRecipient() {
                @Override
                public void binderDied() {
                    mAppBinder = null;
                }
            },0);
        }catch (RemoteException e){
            e.printStackTrace();
        }

    }

    /**
     * 处理数据到来的情况
     * @param code
     * @param data
     * @param reply
     * @param flags
     * @return
     * @throws RemoteException
     */
    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        String descriptor = DESCRIPTOR;
        //根据返回码判断数据的处理
        switch (code){
            case INTERFACE_TRANSACTION:
                //在Binder中写入标示头
                if(reply != null){
                    reply.writeString(descriptor);
                }
                return true;


            case TRANSACTION_setMethodMappingPath:
                data.enforceInterface(descriptor);
                //读取路径数据
                if(mAppBinder == null){
                    mAppBinder = data.readStrongBinder();
                    initBinder();
                }else {
                    data.readStrongBinder();
                }

                String path = data.readString();
                this.setMethodMappingPath(path);
                if(reply != null){
                    reply.writeNoException();
                }

                return true;

            case TRANSACTION_sendParsedContent:
                data.enforceInterface(descriptor);
                if(mAppBinder == null){
                    mAppBinder = data.readStrongBinder();
                    initBinder();
                }else {
                    data.readStrongBinder();
                }
                //读取路径数据
                PluginIssue issue = data.readParcelable(PluginIssue.class.getClassLoader());
                this.sendToParsedContent(issue);
                if(reply != null){
                    reply.writeNoException();
                }

                return true;


                default:
                    ;


        }

        return super.onTransact(code, data, reply, flags);
    }


    /**
     * 提供给App主进程的代理，让其通信数据
     */
    public static class ParsedBinderProxy extends Binder implements IParseInterface{

        private IBinder mParseBinder;
        private ParsedCallBack mCallback;

        public ParsedBinderProxy(IBinder remote){
            this.mParseBinder = remote;
            attachInterface(this,PROXY_DESCRIPTOR);
        }

        public void setOnParseCallback(ParsedCallBack callback){
            this.mCallback = callback;
        }


        //region App端往数据向解析进程写入数据

        @Override
        public void setMethodMappingPath(String path) throws RemoteException {
            //记住写数据的时候需要注意，要按照顺序写入
            Parcel data = android.os.Parcel.obtain();
            Parcel reply = android.os.Parcel.obtain();
            try{
                data.writeInterfaceToken(DESCRIPTOR);
                //把自己带过去
                data.writeStrongBinder(this);
                data.writeString(path);
                mParseBinder.transact(TRANSACTION_setMethodMappingPath,data,reply,0);
                reply.readException();
            }finally {
                data.recycle();
                reply.recycle();
            }

        }

        @Override
        public void sendToParsedContent(PluginIssue issue) throws RemoteException {
            Parcel data = android.os.Parcel.obtain();
            try{
                data.writeInterfaceToken(DESCRIPTOR);
                //把自己带过去
                data.writeStrongBinder(this);
                data.writeParcelable(issue,0);
                //不需要回应,因为是异步
                mParseBinder.transact(TRANSACTION_sendParsedContent,data,null,IBinder.FLAG_ONEWAY);

            }finally {
                data.recycle();
            }
        }




        @Override
        public IBinder asBinder() {
            return mParseBinder;
        }

        //endregion App端往数据向解析进程写入数据

        //实际上我还需要一个机制，回去告诉App端，虽然Binder底层支持无缝沟通，但是到了Java端就分开了BinderProxy和Binder
        //因此这个代理类还需要实现一个Binder

        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            switch (code){
                case INTERFACE_TRANSACTION:
                    //在Binder中写入标示头
                    if(reply != null){
                        reply.writeString(PROXY_DESCRIPTOR);
                    }
                    return true;
                case TRANSACTION_replyParsedContent:
                    data.enforceInterface(PROXY_DESCRIPTOR);
                    //读取路径数据
                    PluginIssue issue = data.readParcelable(PluginIssue.class.getClassLoader());
                    if(mCallback != null){
                        mCallback.finish(issue);
                    }
                    if(reply != null){
                        reply.writeNoException();
                    }
                    return true;

                    default:
                        ;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }

}

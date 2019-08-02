package com.yjy.matrixplugin.matrixserver;

import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yjy.matrixplugin.PluginReporterListener;
import com.yjy.matrixplugin.binder.ParsedBinder;
import com.yjy.matrixplugin.issue.ParseIssueUtil;
import com.yjy.matrixplugin.issue.PluginIssue;
import com.yjy.matrixplugin.matrixserver.data.EvilMethod;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/29
 *     desc   : 跨进程解析端，这么设计的原因是，我发现methodmapping 高达10M是常有的事情，
 *     为了不影响原App进程的这边选择使用新的进程处理
 *     为了解析这么大的数据，华为P10 测试下来是从 1564476329768到1564476330426
 *     空进程解析9.9M  差不多500毫秒
 *     github:yjy239@gitub.com
 * </pre>
 */
public class MatrixPluginServer extends ParsedBinder implements Runnable{

    private String mPath;
    private volatile boolean isReading = false;

    private static final String TAG = MatrixPluginServer.class.getName();

    private Executor executor = Executors.newScheduledThreadPool(1);

    private ConcurrentHashMap<Integer,String> mMethodMap = new ConcurrentHashMap<>();


    Gson gson =  new Gson();


    /**
     * 有两个时机触发任务队列的处理
     * 1. 没解析完数据，解析完之后
     * 2.已经解析完数据，立即加入队列开始执行
     */

    private volatile CopyOnWriteArrayList<Runnable> mEventQueue = new CopyOnWriteArrayList<>();


    /**
     * 设置新的方法地址
     * @param path
     * @throws RemoteException
     */
    @Override
    public void setMethodMappingPath(String path) throws RemoteException {
        Log.e(TAG,"--------------start load  Path "+ System.currentTimeMillis() +"--------------------");
        mPath = path;
        mMethodMap.clear();
        executor.execute(MatrixPluginServer.this);
    }

    @Override
    public void sendToParsedContent(PluginIssue issue) throws RemoteException {
        if(PluginReporterListener.METHOD.equals(issue.getTag())){
            String content = issue.getContent();

            EvilMethod method = gson.fromJson(content, EvilMethod.class);
            //异步解析
            String stack = method.getStack();


            if(mMethodMap.size() > 0){
                //解析方法栈
                try {
                    String parsedStack =  parseStack(stack).toString();
                    method.setStack(parsedStack);
                    issue.setContent(gson.toJson(method));
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    replyStackToApp(issue);
                }


            }else{
                replyStackToApp(issue);
            }

        }else {
            replyStackToApp(issue);
        }


    }


    private StringBuilder parseStack(String originTack){
        StringBuilder stringBuilder = new StringBuilder(" ");

        Log.e(TAG,originTack+" ");
        String[] lines = originTack.split("\n");
        for (String line : lines) {
            String[] args = line.split(",");
            if(args.length == 1){
                stringBuilder.append(args[0]);
            }else {
                int method = Integer.parseInt(args[1]);
                boolean isContainKey = mMethodMap.containsKey(method);
                if (!isContainKey) {
                    continue;
                }

                args[1] = mMethodMap.get(method);
                stringBuilder.append(args[0]);
                stringBuilder.append(",");
                stringBuilder.append(args[1]);
                stringBuilder.append(",");
                stringBuilder.append(args[2]);
                stringBuilder.append(",");
                stringBuilder.append(args[3] + "\n");
            }

        }

        return stringBuilder;
    }


    public boolean isReading(){
        return isReading;
    }



    @Override
    public void run() {
        //读取数据
        readMappingFile(mMethodMap);
    }

    public void readMappingFile(Map<Integer, String> methodMap) {
        BufferedReader reader = null;
        String tempString = null;
        isReading = true;
        Log.e(TAG,"--------------start loading--------------------");
        try {
            reader = new BufferedReader(new FileReader(mPath));
            while ((tempString = reader.readLine()) != null) {
                String[] contents = tempString.split(",");
                methodMap.put(Integer.parseInt(contents[0]), contents[2].replace('\n', ' '));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            Log.e(TAG,"--------------finish loading "+ System.currentTimeMillis() +"--------------------");


            isReading = false;


        }
    }

    public void replyStackToApp(PluginIssue issue){
        try {
            replyToApp(issue);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }




}

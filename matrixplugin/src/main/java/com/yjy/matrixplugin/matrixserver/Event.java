package com.yjy.matrixplugin.matrixserver;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/08/02
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
 class Event implements Runnable {
    Event next;
    Runnable mRunnable;

    public Event(Runnable runnable){
        this.mRunnable = runnable;
    }


    @Override
    public void run() {
        if(mRunnable != null){
            mRunnable.run();
        }
    }
}

package com.cloudysea.controller;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author roof 2019/9/14.
 * @email lyj@yhcs.com
 * @detail
 */
public class LocalExecutor {
    private static LocalExecutor mLocalExecutor;
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,10,60L,TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(100));

    private LocalExecutor(){

    }

    public static LocalExecutor getInstance(){
        if(mLocalExecutor == null){
            mLocalExecutor = new LocalExecutor();
        }
        return mLocalExecutor;
    }

    public void addWork(Runnable runnable){
        try{
            threadPoolExecutor.execute(runnable);
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public void removeWork(Runnable runnable){
        threadPoolExecutor.remove(runnable);
    }

}

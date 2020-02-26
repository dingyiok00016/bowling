package com.cloudysea.controller;

/**
 * @author roof 2019/9/8.
 * @email lyj@yhcs.com
 * @detail
 */
public abstract class BaseListener<K> {
    public void executeReal(Object object){
        try{
            execute((K) object);
        }catch (Exception e){
           e.printStackTrace();
        }
    }

    public abstract void execute(K k);
}

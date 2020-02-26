package com.cloudysea.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roof 2019/9/8.
 * @email lyj@yhcs.com
 * @detail
 */
public class ListenerManager<T extends BaseListener> {
    private List<T> mListeners = new ArrayList<>();

    public void addListener(T t){
        if(!mListeners.contains(t)){
            mListeners.add(t);
        }else{
            mListeners.remove(t);
            mListeners.add(t);
        }
    }

    public void removeListener(T t){
        mListeners.remove(t);
    }

    public void clearListener(){
        mListeners.clear();
    }

    public void executeListeners(Object object){
        for(int i = 0; i < mListeners.size();i++){
            T t = mListeners.get(i);
            if(t != null){
                t.executeReal(object);
            }
        }
    }
}

package com.cloudysea.controller;

import android.app.Activity;

import com.cloudysea.ui.MainActivity;

import java.util.LinkedList;
import java.util.Stack;

/**
 * @author roof 2019/9/14.
 * @email lyj@yhcs.com
 * @detail
 */
public class ActivityStacks {
    private static ActivityStacks mStacks;
    private LinkedList<Activity> activities = new LinkedList<>();

    private ActivityStacks(){

    }

    public int size(){
        return activities.size();
    }

    public static ActivityStacks getInstance(){
        if(mStacks == null){
            mStacks = new ActivityStacks();
        }
        return mStacks;
    }

    public Activity getTop(){
        if(activities.size() > 0){
            return activities.getFirst();
        }
        return null;
    }

    public Activity getMainActivity(){
        if(activities.size() > 0) {
            for (Activity activity : activities) {
                if (activity instanceof MainActivity) {
                    return activity;
                }
                return null;
            }
        }
        return null;
    }


    public void push(Activity activity){
        activities.addFirst(activity);
    }

    public void pop(Activity activity){
        activities.remove(activity);
    }


    public void exit(){
        while(activities.size() > 0){
            Activity activity = activities.getLast();
            activities.remove(activity);
            activity.finish();
        }
        System.exit(0);
    }
}

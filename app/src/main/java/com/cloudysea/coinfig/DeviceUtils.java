package com.cloudysea.coinfig;

import com.cloudysea.BowlingApplication;

/**
 * @author roof 2019/9/7.
 * @email lyj@yhcs.com
 * @detail
 */
public class DeviceUtils {
    public static float getDestiny(){
        return BowlingApplication.getContext().getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidth(){
        return BowlingApplication.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(){
        return BowlingApplication.getContext().getResources().getDisplayMetrics().heightPixels;
    }
}

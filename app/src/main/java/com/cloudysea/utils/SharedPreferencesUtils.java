package com.cloudysea.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.cloudysea.BowlingApplication;
import com.cloudysea.bean.GetScoreConguration;

/**
 * @author roof 2019/9/28.
 * @email lyj@yhcs.com
 * @detail
 */
public class SharedPreferencesUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "bowling";

    public static final String LANGUAGE = "language";
    public static final String CHANNEL_COUNT = "channel_count";
    public static final String CHANNEL_ARRAYS = "channel_arrays";
    public static final String CHANNEL_IP = "channel_ip";
    public static final String HOST_IP = "host_ip";

    // 广告等配置
    public static final String EnabledAd = "enable_ad";
    public static final String EnableAnimation = "enable_anim";
    public static final String EnabledLargePinState = "enable_pin";
    public static final String EnabledUseCloud = "enable_cloud";


    public static void setAllConfigParam(GetScoreConguration getScoreConguration){
        if(getScoreConguration == null || getScoreConguration.DataBean == null){
            Log.d("SharedPreferencesUtils","没有config数据");
            return;
        }
        setParam(EnabledAd,getScoreConguration.DataBean.EnabledAd);
        setParam(EnableAnimation,getScoreConguration.DataBean.EnableAnimation);
        setParam(EnabledLargePinState,getScoreConguration.DataBean.EnabledLargePinState);
        setParam(EnabledUseCloud,getScoreConguration.DataBean.UseCloudAdAndAnimation);
    }

    public static boolean enableAd(){
        return (boolean) getParam(EnabledAd,Boolean.TRUE);
    }

    public static boolean enabledAnimation(){
        boolean enabledAnimation = (boolean) getParam(EnableAnimation,Boolean.TRUE);
        Log.d("SharedPreferencesUtils","enabledAnimation=" + enabledAnimation);
        return enabledAnimation;
    }

    public static boolean enabledPinState(){
        boolean enabledPinState = (boolean) getParam(EnabledLargePinState,Boolean.TRUE);
        Log.d("SharedPreferencesUtils","enabledPinState=" + enabledPinState);
        return enabledPinState;
    }

    public static boolean enabledUseCloud(){
        boolean enabledUseCloud = (boolean) getParam(EnabledUseCloud,Boolean.TRUE);
        Log.d("SharedPreferencesUtils","enabledUseCloud=" + enabledUseCloud);
        return enabledUseCloud;
    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param key
     * @param object
     */
    public static void setParam(String key, Object object){

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = BowlingApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }

        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context,String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }

        return null;
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = BowlingApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }

        return null;
    }
}

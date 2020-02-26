package com.cloudysea.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author roof 2019/10/26.
 * @email lyj@yhcs.com
 * @detail
 */
public class DateCovertUtils {
    private static DateCovertUtils sInstance;

    public static DateCovertUtils getInstance(){
        if(sInstance == null){
            sInstance = new DateCovertUtils();
        }
        return sInstance;
    }


    private DateCovertUtils(){

    }

    private SimpleDateFormat mTargetFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
    private SimpleDateFormat mSourceFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss",Locale.getDefault());


    public String covertPlayingTime(String timeStr){
        if(TextUtils.isEmpty(timeStr)){
            return "";
        }
        try {
            Date date = mSourceFormat.parse(timeStr);
            return mTargetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}

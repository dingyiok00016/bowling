package com.cloudysea;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.cloudysea.coinfig.ColorConfig;
import com.cloudysea.coinfig.ColorConfigManager;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.net.BallSocketServer;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.net.BowlingUdpClient;
import com.cloudysea.net.BowlingUdpServer;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.LogcatFileManager;
import com.cloudysea.utils.SharedPreferencesUtils;
import com.danikula.videocache.HttpProxyCacheServer;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.Locale;

import static com.cloudysea.utils.SharedPreferencesUtils.HOST_IP;
import static com.cloudysea.utils.SharedPreferencesUtils.LANGUAGE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * @author roof 2019/9/7.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static BowlingApplication mApplication;
    Handler mHandler = new Handler();
    public static String HUB_URL="http://bowlingdebug.cloudysea.com/";
    private final static String HUB_URL_DEBUG ="http://bowlingdebug.cloudysea.com/";
    private final static String HUB_URL_RELEASE ="http://bowling.cloudysea.com/";
    private final static String HUB_URL_LOCAL ="http://192.168.1.199:8081/";

    private void initLanguage() {
        if (Build.VERSION.SDK_INT < 26) {
            //Application这种方式适用于8.0之前(不包括8.0)的版本
            int language = (int) SharedPreferencesUtils.getParam(LANGUAGE, 1);
            String lang = Locale.SIMPLIFIED_CHINESE.getLanguage();
            if (language == 1) {
                lang = Locale.SIMPLIFIED_CHINESE.getLanguage();
            } else if (language == 2) {
                lang = Locale.ENGLISH.getLanguage();
            } else if (language == 3) {
                lang = Locale.KOREAN.getLanguage();
            }
            BowlingUtils.initAppLanguage(getApplicationContext(), lang);
        }
    }


    // 获取共享目录下
    private void getPcShareDirectory(){
        Intent intent = new Intent(this,UploadShareService.class);
        intent.putExtra(UploadShareService.EXTRA_TASK_NAME,UploadShareService.UPLOAD_IMAGE_AND_ANIMATION);
        startService(intent);
    }

    @Override
    protected void attachBaseContext(Context base) {
        if (Build.VERSION.SDK_INT < 26) {
            super.attachBaseContext(base);
        } else {
            //zh：中文
            int language = (int) SharedPreferencesUtils.getParam(base,LANGUAGE, 1);
            String lang = Locale.SIMPLIFIED_CHINESE.getLanguage();
            if (language == 1) {
                lang = Locale.SIMPLIFIED_CHINESE.getLanguage();
            } else if (language == 2) {
                lang = Locale.ENGLISH.getLanguage();
            } else if (language == 3) {
                lang = Locale.KOREAN.getLanguage();
            }
            super.attachBaseContext(BowlingUtils.initAppLanguage(base, lang));
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        getPcShareDirectory();
        initLanguage();
        // 手机适配
        if(!BowlingUtils.isPad()){
            BowlingUtils.Glbal_SIZE_RADIO = 0.8F;
            BowlingUtils.Gobal_SIZE_SCORE_RADIO = 0.5F;
            BowlingUtils.Global_SIZE_BOTTOM = 0.6F;
        }
        int host_env = (int) SharedPreferencesUtils.getParam(HOST_IP,0);
        if(host_env == 0){
            HUB_URL = HUB_URL_DEBUG;
        }else if(host_env == 1){
            HUB_URL = HUB_URL_RELEASE;
        }else{
            HUB_URL = HUB_URL_LOCAL;
        }
        LogcatFileManager.getInstance().startLogcatManager(this);
        Log.d("BowlingApplication","当前环境:" + HUB_URL);
        registerActivityLifecycleCallbacks(this);
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_BOX, null);
        getInitOption();
        initConnection();
        ColorConfigManager.getInstance().init();
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        config.locale = BowlingUtils.getLanuage();
        resources.updateConfiguration(config, dm);
        return resources;
    }

    public static BowlingApplication getContext(){
        return mApplication;
    }

    private void getInitOption(){

    }

    private void initConnection(){
        // 服务器、client有连接
        BallSocketServer.getInstance().connect();
        BowlingClient.getInstance().connect();
        BowlingUdpServer.SendUtils();
        BowlingUdpClient.getInstance().connect();
        // 获取配置和检测机器是否有效
        BowlingClient.getInstance().shouldGetConfig();
    //    BowlingClient.getInstance().checkDevice();
    }



    @Override
    public void onTerminate() {
        super.onTerminate();
        ActivityStacks.getInstance().exit();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        BowlingClient.getInstance().connect();
        ActivityStacks.getInstance().push(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        MobclickAgent.onPageStart(activity.getClass().getName());
        MobclickAgent.onResume(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        MobclickAgent.onPageEnd(activity.getClass().getName());
        MobclickAgent.onPause(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }
    private HttpProxyCacheServer proxy;
    public static HttpProxyCacheServer getProxy(Context context) {
        BowlingApplication app = (BowlingApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }



    @Override
    public void onActivityDestroyed(Activity activity) {
        ActivityStacks.getInstance().pop(activity);
        if(ActivityStacks.getInstance().size() == 0){
            BowlingClient.getInstance().close();
            BallSocketServer.getInstance().onStop();
        }
    }
}

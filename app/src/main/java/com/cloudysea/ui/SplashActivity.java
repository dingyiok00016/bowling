package com.cloudysea.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cloudysea.R;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.utils.AnimationFactory;
import com.cloudysea.utils.FtpDownFiles;
import com.cloudysea.utils.PermissionUtils;
import com.cloudysea.utils.SharedPreferencesUtils;
import com.cloudysea.utils.ToastUtil;
import com.cloudysea.views.BowlingConnectVipDialog;
import com.cloudysea.views.BowlingFunctionSetDialog;

import java.io.File;
import java.util.Locale;

import static com.cloudysea.utils.SharedPreferencesUtils.LANGUAGE;

/**
 * @author roof 2019/9/15.
 * @email lyj@yhcs.com
 * @detail 启动界面
 */
public class SplashActivity extends BaseLanguageActivity {

    private static final int DELAY_TIME = 1000;
    private String permissions = Manifest.permission.READ_PHONE_STATE;
    private static final int REQUEST_PERMISSION_CODE_PHONE_STATE = 0x997;
    public static final String EXTRA_START_CLOCK = "extra_start_clock";
    static class SplashHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
    private SplashHandler handler;
    private BowlingFunctionSetDialog dialog;

    private ImageView mBgView1;
    private ImageView mBgView2;
    private ImageView mBgView3;
    private ImageView mBgView4;
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initLanguageConfig();
        findViewById(R.id.ll_splash_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nineClick();
            }
        });
        handler = new SplashHandler();
        checkPermission();
    }

    private void checkPermission() {
        PermissionUtils.checkAndRequestPermission(this, permissions, REQUEST_PERMISSION_CODE_PHONE_STATE, new PermissionUtils.PermissionRequestSuccessCallBack() {
            @Override
            public void onHasPermission() {
               startMainActivity();
            }
        });
    }

    private void  startMainActivity(){
        boolean startClock = getIntent().getBooleanExtra(EXTRA_START_CLOCK,true);
        if(startClock){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //进入主程序页面
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, DELAY_TIME);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) {
            return;
        }
        if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
            startMainActivity();
        }else{
            //权限申请失败
            ToastUtil.showText(this,R.string.sd_failure);
            checkPermission();
        }
    }

    //点击9次
    private final int CLICK_NUM = 7;
    //点击时间间隔5秒
    private final int CLICK_INTERVER_TIME = 500;
    //上一次的点击时间
    private long lastClickTime = 0;
    //记录点击次数
    private int clickNum = 0;
    /**
     * 点击9次
     */
    public void nineClick() {
        //点击的间隔时间不能超过5秒
        long currentClickTime = SystemClock.uptimeMillis();
        if (currentClickTime - lastClickTime <= CLICK_INTERVER_TIME || lastClickTime == 0) {
            lastClickTime = currentClickTime;
            clickNum = clickNum + 1;
        } else {
            //超过5秒的间隔
            //重新计数 从1开始
            clickNum = 1;
            lastClickTime = 0;
            return;
        }
        if (clickNum == CLICK_NUM) {
            //重新计数
            clickNum = 0;
            lastClickTime = 0;
            /*实现点击多次后的事件*/
            if(dialog == null){
                dialog = new BowlingFunctionSetDialog(this);
            }
            dialog.show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(handler != null){
            handler.removeCallbacks(null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    private void remoteBattler(){
        BowlingConnectVipDialog vipDialog = new BowlingConnectVipDialog(this);
        vipDialog.show();
    }

    public void initLanguageConfig(){
        // 获得res资源对象
        Resources resources = getResources();
        // 获得屏幕参数：主要是分辨率，像素等。
        DisplayMetrics metrics = resources.getDisplayMetrics();
        // 获得配置对象
        Configuration config = resources.getConfiguration();
        //区别17版本（其实在17以上版本通过 config.locale设置也是有效的，不知道为什么还要区别）
        //在这里设置需要转换成的语言，也就是选择用哪个values目录下的strings.xml文件
        int language = (int) SharedPreferencesUtils.getParam(LANGUAGE,1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(language == 1){
                config.setLocale(Locale.SIMPLIFIED_CHINESE);//设置简体中文
            }else if(language == 2){
                config.setLocale(Locale.ENGLISH);
            }else if(language == 3){
                config.setLocale(Locale.KOREAN);
            }
            //config.setLocale(Locale.ENGLISH);//设置英文
        } else {
            if(language == 1){
                config.locale = Locale.SIMPLIFIED_CHINESE;//设置简体中文
            }else if(language == 2){
                config.locale = Locale.ENGLISH;
            }else if(language == 3){
                config.locale = Locale.KOREAN;
            }
            //config.locale = Locale.ENGLISH;//设置英文
        }
        resources.updateConfiguration(config, metrics);
    }

    private void initView(){
        mFrameLayout = (FrameLayout) findViewById(R.id.ll_splash_view);
        mBgView1 = (ImageView) findViewById(R.id.login_bg_image1);
        mBgView2 = (ImageView) findViewById(R.id.login_bg_image2);
        mBgView3 = (ImageView) findViewById(R.id.login_bg_image3);
        mBgView4 = (ImageView) findViewById(R.id.login_bg_image4);

        // 广告赋值
        String adDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + FtpDownFiles.BOWLING_AD + "Image";
        // 遍历
        File directoryFile = new File(adDirectory);
        File[] files = directoryFile.listFiles();
        if(files == null){
            mFrameLayout.setBackgroundResource(R.drawable.bg_startup);
            return;
        }
        for(int i = 0; i < files.length;i++){
            Log.d("SplashActivity","file=" + files[i].getAbsolutePath());
            Bitmap bitmap = BitmapFactory.decodeFile(files[i].getAbsolutePath());
            switch (i){
                case 0:
                    mBgView1.setImageBitmap(bitmap);
                    break;
                case 1:
                    mBgView2.setImageBitmap(bitmap);
                    break;
                case 2:
                    mBgView3.setImageBitmap(bitmap);
                    break;
                case 3:
                    mBgView4.setImageBitmap(bitmap);
                    break;
            }
        }
        initAnimation();
    }

    private void initAnimation(){
        AnimatorSet animatorSet1 = AnimationFactory.getAnimationStyle(AnimationFactory.ANIMATION_STYLE_ONE,mBgView1,mBgView2);
        AnimatorSet animatorSet2 = AnimationFactory.getAnimationStyle(AnimationFactory.ANIMATION_STYLE_TWO,mBgView2,mBgView3);
        AnimatorSet animatorSet3 = AnimationFactory.getAnimationStyle(AnimationFactory.ANIMATION_STYLE_TWO,mBgView3,mBgView4);
        AnimatorSet animatorSet4 = AnimationFactory.getAnimationStyle(AnimationFactory.ANIMATION_STYLE_FOUR,mBgView4,mBgView1);
        animatorSet3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 放大的View复位
                mBgView1.setScaleX(1.0f);
                mBgView1.setScaleY(1.0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        // 顺序循环
        animatorSet.playSequentially(animatorSet1, animatorSet2, animatorSet3, animatorSet4);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 将放大的View 复位
                mBgView2.setScaleX(1.0f);
                mBgView2.setScaleY(1.0f);
                mBgView3.setScaleX(1.0f);
                mBgView3.setScaleY(1.0f);
                mBgView4.setScaleX(1.0f);
                mBgView4.setScaleY(1.0f);
                // 循环播放
                animation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }


}

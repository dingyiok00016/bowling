package com.cloudysea.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cloudysea.R;
import com.cloudysea.utils.AnimationFactory;
import com.cloudysea.utils.FtpDownFiles;

import java.io.File;

/**
 * @author roof 2020-02-29.
 * @email lyj@yhcs.com
 * @detail 广告dialog
 */
public class BowlingAdDialog extends Dialog {
    private ImageView mBgView1;
    private ImageView mBgView2;
    private ImageView mBgView3;
    private ImageView mBgView4;
    private boolean mCanShow;

    public BowlingAdDialog(@NonNull Context context) {
        super(context, R.style.loading_style);
        initView();
    }

    private void initView() {
        View contentView = View.inflate(getContext(), R.layout.dialog_ad, null);
        setContentView(contentView);
        mBgView1 = (ImageView) contentView.findViewById(R.id.login_bg_image1);
        mBgView2 = (ImageView) contentView.findViewById(R.id.login_bg_image2);
        mBgView3 = (ImageView) contentView.findViewById(R.id.login_bg_image3);
        mBgView4 = (ImageView) contentView.findViewById(R.id.login_bg_image4);

        // 广告赋值
        String adDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + FtpDownFiles.BOWLING_AD + "Image";
        // 遍历
        File directoryFile = new File(adDirectory);
        File[] files = directoryFile.listFiles();
        if (files == null) {
            mCanShow = false;
            return;
        }
        if (files.length >= 1) {
            mCanShow = true;
            setCancelable(false);
            setCanceledOnTouchOutside(false);
        }
        for (int i = 0; i < files.length; i++) {
            Log.d("SplashActivity", "file=" + files[i].getAbsolutePath());
            Bitmap bitmap = BitmapFactory.decodeFile(files[i].getAbsolutePath());
            switch (i) {
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

    private void initAnimation() {
        AnimatorSet animatorSet1 = AnimationFactory.getAnimationStyle(AnimationFactory.ANIMATION_STYLE_ONE, mBgView1, mBgView2);
        AnimatorSet animatorSet2 = AnimationFactory.getAnimationStyle(AnimationFactory.ANIMATION_STYLE_TWO, mBgView2, mBgView3);
        AnimatorSet animatorSet3 = AnimationFactory.getAnimationStyle(AnimationFactory.ANIMATION_STYLE_TWO, mBgView3, mBgView4);
        AnimatorSet animatorSet4 = AnimationFactory.getAnimationStyle(AnimationFactory.ANIMATION_STYLE_FOUR, mBgView4, mBgView1);
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

    @Override
    public void show() {
        if (mCanShow) {
            super.show();
            Window dialogWindow = getWindow();
            if (dialogWindow != null) {
                dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialogWindow.setAttributes(lp);
            }
        }
    }
}

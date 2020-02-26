package com.cloudysea.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.cloudysea.R;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.utils.BowlingBallScoreViewManager;
import com.cloudysea.utils.CloudSeaVideoController;
import com.cloudysea.utils.FtpDownFiles;
import com.cloudysea.utils.LogcatFileManager;
import com.cloudysea.utils.SharedPreferencesUtils;
import com.dueeeke.videoplayer.listener.OnVideoViewStateChangeListener;
import com.dueeeke.videoplayer.player.VideoView;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * @author roof 2020-02-23.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingAnimationDialog extends Dialog {

    private BowlingSeaVideoView mSeaVideoView;
    private static final int MSG_PLAY_ANIMATION = 0x01;
    private View mLayoutPinState;
    private FrameLayout mFrameLayout;
    private BowlingBallScoreViewManager mMananger;
    WeakHandler addSubmitHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MSG_PLAY_ANIMATION:
                    String string = (String) msg.obj;
                    if(SharedPreferencesUtils.enabledAnimation()){
                        playAnimation(string);
                    }else{
                        dismiss();
                    }
                    break;
            }
            return false;
        }
    });

    public BowlingAnimationDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView(){
        ViewGroup viewGroup = (ViewGroup) View.inflate(getContext(), R.layout.dialog_show_animation,null);
        mFrameLayout = (FrameLayout) viewGroup.findViewById(R.id.frame_container);
        mLayoutPinState = viewGroup.findViewById(R.id.layout_score);
        mMananger = new BowlingBallScoreViewManager((ViewGroup) mLayoutPinState);
        setCanceledOnTouchOutside(false);
        addVideoView(mFrameLayout);
        setContentView(viewGroup);
    }

    private void addVideoView(View frameLayout){
        mSeaVideoView = (BowlingSeaVideoView) frameLayout.findViewById(R.id.player);
        mSeaVideoView.setOnVideoViewStateChangeListener(new OnVideoViewStateChangeListener() {
            @Override
            public void onPlayerStateChanged(int playerState) {
                Log.d("AnimationConfigHolder","playerState=" + playerState);
            }

            @Override
            public void onPlayStateChanged(int playState) {
                if(playState == VideoView.STATE_PLAYBACK_COMPLETED){
                    mSeaVideoView.release();
                    mSeaVideoView.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.GONE);
                }
                Log.d("AnimationConfigHolder","playerState=" + playState);
            }
        });
    }

    public void playPinState(List<Boolean> playState, String string){
        if(SharedPreferencesUtils.enabledPinState()) {
            Log.d("AnimationConfigHolder", "显示瓶位图");
            LogcatFileManager.getInstance().writeLog("BowlingAnimationDialog","显示瓶位图");
            mFrameLayout.setVisibility(View.VISIBLE);
            mLayoutPinState.setVisibility(View.VISIBLE);
            mMananger.setBallScoreByCharArrays(playState);

            Message msg = Message.obtain();
            msg.what = MSG_PLAY_ANIMATION;
            msg.obj = string;
            addSubmitHandler.sendMessageDelayed(msg,2000);
        }
    }


    private File getAviPathByCategoryName(String categoryName){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                FtpDownFiles.BOWLING_ANIMATION + categoryName);
        LogcatFileManager.getInstance().writeLog("AnimationConfigHolder","file=" + file.getAbsolutePath());
        Log.d("AnimationConfigHolder","file=" + file.getAbsolutePath());
        if(file.exists()){
            return file;
        }
        return null;
    }


    public void videoDismiss(){
        if(mSeaVideoView != null){
            mSeaVideoView.release();
        }
    }

    public void playAnimation(String categoryName){
        Log.d("AnimationConfigHolder","categoryName" + categoryName);
        LogcatFileManager.getInstance().writeLog("AnimationConfigHolder","categoryName" + categoryName);
        if(TextUtils.isEmpty(categoryName)){
            dismiss();
            return;
        }
        File file = getAviPathByCategoryName(categoryName);
        Log.d("AnimationConfigHolder","file是否存在" + (file == null));
        LogcatFileManager.getInstance().writeLog("AnimationConfigHolder","file是否存在" + (file == null));
        if(file == null){
            dismiss();
            return;
        }
        File[] files = file.listFiles();
        if(files == null || files.length == 0){
            return;
        }
        mSeaVideoView.setVisibility(View.VISIBLE);
        mFrameLayout.setVisibility(View.VISIBLE);
        Random random = new Random();
        int number = random.nextInt(files.length);
        mSeaVideoView.setUrl(files[number].getAbsolutePath());
        Log.d("AnimationConfigHolder","file=" + files[number].getAbsolutePath());
        LogcatFileManager.getInstance().writeLog("AnimationConfigHolder","file=" + files[number].getAbsolutePath());
        mSeaVideoView.start();
        Log.d("AnimationConfigHolder","执行 addNewScore 动画");
    }

    public void show() {
        super.show();
        Window dialogWindow = getWindow();
        dialogWindow.getDecorView().setPadding(0,0,0,0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
    }


}

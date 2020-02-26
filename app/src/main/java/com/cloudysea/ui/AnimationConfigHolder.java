package com.cloudysea.ui;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cloudysea.R;
import com.cloudysea.bean.AddNewScore;
import com.cloudysea.controller.BaseListener;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.utils.BowlingBallScoreViewManager;
import com.cloudysea.utils.CloudSeaVideoController;
import com.cloudysea.utils.SharedPreferencesUtils;
import com.cloudysea.views.BowlingAnimationDialog;
import com.cloudysea.views.BowlingSeaVideoView;
import com.cloudysea.views.WeakHandler;
import com.dueeeke.videoplayer.listener.OnVideoViewStateChangeListener;
import com.dueeeke.videoplayer.player.VideoView;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * @author roof 2020-02-16.
 * @email lyj@yhcs.com
 * @detail 动画及其 瓶位图显示
 */
public class AnimationConfigHolder {

    private BowlingAnimationDialog mDialog;
    private Context mContext;

    private BowlingAnimationDialog getDialog(){
        if(mDialog == null){
            mDialog = new BowlingAnimationDialog(mContext,R.style.loading_style);
        }
        return mDialog;
    }


    // 初始化动画
    private BaseListener<AddNewScore> config = new BaseListener<AddNewScore>() {
        @Override
        public void execute(AddNewScore addNewScore) {
            Log.d("AnimationConfigHolder","执行 addNewScore execute");
            if(addNewScore.Data == null){
                return;
            }
            switch (addNewScore.Data.ScoreNumber){
                case 1:
                    getDialog().show();
                    getDialog().playPinState(addNewScore.Data.PinStates,addNewScore.Data.AnimationCategory);
                    break;
                case 2:
                    if(SharedPreferencesUtils.enabledAnimation()){
                        getDialog().show();
                        getDialog().playAnimation(addNewScore.Data.AnimationCategory);
                    }
                    break;
                case 3:
                    if(SharedPreferencesUtils.enabledAnimation()){
                        getDialog().show();
                        getDialog().playAnimation(addNewScore.Data.AnimationCategory);
                    }
                    break;
            }
        }
    };

    public AnimationConfigHolder(Context context){
        mContext = context;
        BowlingManager.getInstance().addScoreThenController.addListener(config);
    }

    public void onDestory(){
        getDialog().videoDismiss();
        getDialog().dismiss();
        BowlingManager.getInstance().addScoreThenController.removeListener(config);
    }

}

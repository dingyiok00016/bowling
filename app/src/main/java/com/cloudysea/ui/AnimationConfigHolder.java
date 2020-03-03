package com.cloudysea.ui;

import android.content.Context;
import android.util.Log;

import com.cloudysea.bean.AddNewScore;
import com.cloudysea.controller.BaseListener;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.utils.SharedPreferencesUtils;
import com.cloudysea.views.BowlingAnimationDialog;

/**
 * @author roof 2020-02-16.
 * @email lyj@yhcs.com
 * @detail 动画及其 瓶位图显示
 */
public class AnimationConfigHolder {

    private BowlingAnimationDialog mDialog;
    private Context mContext;
    // 初始化动画
    private BaseListener<AddNewScore> config = new BaseListener<AddNewScore>() {
        @Override
        public void execute(AddNewScore addNewScore) {
            Log.d("AnimationConfigHolder","执行 addNewScore execute");
            if(addNewScore.Data == null){
                return;
            }
            // 取消当前播放的动画
            if(getDialog().isShowing()){
                getDialog().stopVideoView();
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

    private BowlingAnimationDialog getDialog(){
        if(mDialog == null){
            mDialog = new BowlingAnimationDialog(mContext);
        }
        return mDialog;
    }

    public void onDestory(){
        getDialog().videoDismiss();
        getDialog().dismiss();
        BowlingManager.getInstance().addScoreThenController.removeListener(config);
    }

}

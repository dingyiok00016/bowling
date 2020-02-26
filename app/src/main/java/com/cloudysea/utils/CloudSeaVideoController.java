package com.cloudysea.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;

import com.cloudysea.views.BowlingVideoPlayDialog;
import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.util.PlayerUtils;

/**
 * @author roof 2019/11/24.
 * @email lyj@yhcs.com
 * @detail
 */
public class CloudSeaVideoController extends StandardVideoController {
    private BowlingVideoPlayDialog mDialog;
    private boolean mIsFullScreen;

    public CloudSeaVideoController(Context context) {
        super(context);
    }

    public void setDialog(BowlingVideoPlayDialog dialog) {
        mDialog = dialog;
    }


    @Override
    protected void doStartStopFullScreen() {
        super.doStartStopFullScreen();
        mIsFullScreen = !mIsFullScreen;
        if (mIsFullScreen) {
            mDialog.superDismiss();
        } else {
            mDialog.show();
        }
    }


    @Override
    protected void startFullScreenFromUser() {
        super.startFullScreenFromUser();
        Log.d("VideoController","startFullScreenFromUser");
    }

    /**
     * 子类中请使用此方法来退出全屏
     */
    @Override
    protected void stopFullScreenFromUser() {
        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (activity == null) return;
        mMediaPlayer.stopFullScreen();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mFromUser = true;
        Log.d("VideoController","stopFullScreenFromUser");

    }
}

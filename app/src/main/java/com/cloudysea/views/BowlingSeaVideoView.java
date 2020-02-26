package com.cloudysea.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cloudysea.R;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.ui.MainActivity;
import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.player.VideoView;

import java.lang.ref.WeakReference;

/**
 * @author roof 2019/11/24.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingSeaVideoView extends VideoView {
    private MyHandler mHandler;
    private static StandardVideoController mController;
    public BowlingSeaVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler = new MyHandler(this);
    }

    public static class MyHandler extends Handler {
        private WeakReference<BowlingSeaVideoView> weakReference;
        public MyHandler(BowlingSeaVideoView activity) {
            this.weakReference = new WeakReference<BowlingSeaVideoView>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            BowlingSeaVideoView videoView = weakReference.get();
            Log.d("BowlingSeaVideoView","isnull" + (videoView == null));
            if(videoView != null){
                Log.d("BowlingSeaVideoView","state-righgt=" + videoView.isInPlaybackState());
                if(!videoView.isInPlaybackState()){
                    sendEmptyMessageDelayed(0,100);
                    Log.d("BowlingSeaVideoView","send");
                }else{
                    View view = mController.findViewById(R.id.iv_play);
                    view.performClick();
                    Log.d("BowlingSeaVideoView","start");
                }
            }
        }
    }


    @Override
    public void release() {
        super.release();
        mHandler.removeCallbacksAndMessages(null);
        mController = null;
    }

    @Override
    public void replay(boolean resetPosition) {
        super.replay(resetPosition);
        if(mController == null){
            mController = (StandardVideoController) mVideoController;
        }
        if(isFullScreen()){
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(0,2500);
        }
        Log.d("BowlingSeaVideoView","replay");
    }
}

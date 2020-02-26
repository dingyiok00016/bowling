package com.cloudysea.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cloudysea.R;
import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.player.VideoView;

import java.lang.ref.WeakReference;

/**
 * @author roof 2019/11/24.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingSeaFullVideoView extends VideoView {
    private MyHandler mHandler;
    private static StandardVideoController mController;
    public BowlingSeaFullVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler = new MyHandler(this);
    }

    public static class MyHandler extends Handler {
        private WeakReference<BowlingSeaFullVideoView> weakReference;
        public MyHandler(BowlingSeaFullVideoView activity) {
            this.weakReference = new WeakReference<BowlingSeaFullVideoView>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            BowlingSeaFullVideoView videoView = weakReference.get();
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
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

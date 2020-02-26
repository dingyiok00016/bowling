package com.cloudysea.views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.utils.CloudSeaVideoController;
import com.danikula.videocache.HttpProxyCacheServer;
import com.dueeeke.videoplayer.player.VideoView;


/**
 * @author roof 2019/10/18.
 * @email lyj@yhcs.com
 * @detail 视频播放dialog
 */
public class BowlingVideoPlayDialog extends BowlingCommonDialog {

    private VideoView videoPlayer;
    private LinearLayout mLLVideoPlayer;
    private String mVideoUrl;
    private String mVideoLeft;
    private String mVideoRight;
    private TextView mTvVideoPlayLeft;
    private TextView mTvVideoPlayRight;
    public BowlingVideoPlayDialog(@NonNull Context context,String urlLeft,String urlRight) {
        super(context);
        try {
            if(urlRight != null){
                mVideoRight = urlRight;
                mVideoUrl = urlRight;
                if(urlLeft == null){
                    setRightConfig();
                }
            }
            if(urlLeft != null){
                mVideoLeft = urlLeft;
                mVideoUrl = urlLeft;
            }
            if(mVideoLeft != null){
                mTvVideoPlayLeft.setVisibility(View.VISIBLE);
            }else{
                mTvVideoPlayLeft.setVisibility(View.GONE);
            }
            if(mVideoRight != null){
                mTvVideoPlayRight.setVisibility(View.VISIBLE);
            }else{
                mTvVideoPlayRight.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        startPlay();
    }

    private void startPlay(){
        try {
            HttpProxyCacheServer cacheServer = BowlingApplication.getProxy(getContext());
            String proxyUrl = cacheServer.getProxyUrl(mVideoUrl);
            videoPlayer.setUrl(proxyUrl);
            videoPlayer.start();
            Log.d("BowlingSea-VideoDialog","url=" + mVideoUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void rePlay(){
        try {
            videoPlayer.release();
            HttpProxyCacheServer cacheServer = BowlingApplication.getProxy(getContext());
            String proxyUrl = cacheServer.getProxyUrl(mVideoUrl);
            videoPlayer.setUrl(proxyUrl);
            videoPlayer.start();
            Log.d("BowlingSea-VideoDialog","url=" + mVideoUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setUrl(String url){
        try {
            mVideoUrl =  url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        startPlay();
    }

    @Override
    public void dismiss() {
        videoPlayer.release();
        super.dismiss();
    }

    public void superDismiss(){
        super.dismiss();
    }

    @Override
    protected int getDialogStyle() {
        return VEDIO_SCREEN_STYLE;
    }

    @Override
    protected void initView(View view) {
        videoPlayer = (BowlingSeaVideoView) View.inflate(getContext(),R.layout.layout_video_view,null);
        Log.d("BowlingSeaVideoView","videoPlayer" + videoPlayer.hashCode());
        mLLVideoPlayer  = (LinearLayout) findViewById(R.id.ll_video_view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        mLLVideoPlayer.addView(videoPlayer,params);
        CloudSeaVideoController mVideoController = new CloudSeaVideoController(getContext());
        videoPlayer.setVideoController(mVideoController);
        mVideoController.setDialog(this);
        mTvVideoPlayLeft = (TextView) findViewById(R.id.iv_video_play_switch_left);
        mTvVideoPlayLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoUrl = mVideoLeft;
                rePlay();
                setLeftConfig();
            }
        });
        mTvVideoPlayRight = (TextView) findViewById(R.id.iv_video_play_switch_right);
        mTvVideoPlayRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoUrl = mVideoRight;
                rePlay();
                setRightConfig();
            }
        });
    }

    private void setLeftConfig(){
        mTvVideoPlayLeft.setTextColor(Color.WHITE);
        mTvVideoPlayLeft.setTextSize(18);
        mTvVideoPlayRight.setTextSize(16);
        mTvVideoPlayRight.setTextColor(Color.GRAY);
    }

    private void setRightConfig(){
        mTvVideoPlayLeft.setTextColor(Color.GRAY);
        mTvVideoPlayRight.setTextColor(Color.WHITE);
        mTvVideoPlayLeft.setTextSize(16);
        mTvVideoPlayRight.setTextSize(18);
    }




    @Override
    public int getChildViewLayout() {
        return R.layout.dialog_video_play;
    }
}

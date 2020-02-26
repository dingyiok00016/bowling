package com.cloudysea.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.GameBasicInfo;
import com.cloudysea.bean.PlayingGame;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.ui.NetPresenter;
import com.cloudysea.ui.NetView;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.ToastUtil;
import com.cloudysea.utils.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * @author roof 2019/10/17.
 * @email lyj@yhcs.com
 * @detail 远程对战头部信息
 */
public class RemoteBattlerTopView extends LinearLayout {

    private static final int MARGIN_13 = 13;
    private static final int WIDTH_BUTTON = 100;
    private static final int WEIGHT_LARGE = 312;
    private static final int WEIGHT_SMALL = 163;
    private BowlerGameForTurn mTurn;
    private Button mButton;
    private RemoteBattlerTopItem mScore;
    public static final int STYLE_DEVICE = 0x01;
    public static final int STYLE_CHANNEL = 0x02;
    public static final int STYLE_SCORE = 0x03;
    private RemoteBattlerTopItem devices;



    public RemoteBattlerTopView(@NonNull Context context) {
        super(context);
        initView();
    }

    public RemoteBattlerTopView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        addDevice();
        addChannel();
        addScore();
        addBattlerButton();
    }

    private void addDevice(){
        devices = new RemoteBattlerTopItem(getContext());
        String[] arrays = new String[]{getStringByResourceId(R.string.bin_area),"AMF",getStringByResourceId(R.string.mid_area),getStringByResourceId(R.string.xima_area),getStringByResourceId(R.string.other_area)};
        devices.setTitle(R.string.battler_title_device)
               .setContent(arrays)
                .setSelectArray(arrays)
                .setStyle(RemoteBattlerTopView.STYLE_DEVICE)
                .setOnSelectLisenter(new RemoteBattlerTopItem.RemoteBattlerTopItemSelectListener() {
                    @Override
                    public void select(String str, int positon,boolean select,boolean all) {
                        // 比较特别
                        if( mTopListener != null){
                            mTopListener.selectText(STYLE_DEVICE,str,positon,select,all);
                        }
                    }
                });
        addView(devices,new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,2F * BowlingUtils.Glbal_SIZE_RADIO));
    }

    public void setDeviceContent(String str){
        if(devices != null){
            devices.setContent(str);
            devices.setPopwindowContent(str);
        }
    }

    private void addChannel(){
        final RemoteBattlerTopItem channel = new RemoteBattlerTopItem(getContext());
        String[] arrays = new String[]{BowlingApplication.getContext().getResources().getString(R.string.not_starnd),BowlingApplication.getContext().getResources().getString(R.string.starnd),BowlingApplication.getContext().getResources().getString(R.string.all)};
        String[] contents = new String[]{BowlingApplication.getContext().getResources().getString(R.string.starnd)};
        channel.setTitle(R.string.battler_title_channel)
                .setContent(contents)
                .setSelectArray(arrays)
                .setStyle(RemoteBattlerTopView.STYLE_CHANNEL)
                .setOnSelectLisenter(new RemoteBattlerTopItem.RemoteBattlerTopItemSelectListener() {
                @Override
                public void select(String str, int positon,boolean select,boolean all) {
                    channel.setContent(str);
                    if( mTopListener != null){
                        if(positon < 2){
                            str = positon  + "";
                        }else{
                            str = "0,1";
                        }
                        mTopListener.selectText(STYLE_CHANNEL,str,positon,select,all);
                }
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,1F);
        params.leftMargin = (int) (DeviceUtils.getDestiny() * MARGIN_13);
        addView(channel,params);
    }

    // 非创建模式下计分添加all
    public void setShowStyle(){
        String[] arrays = new String[]{getStringByResourceId(R.string.new_score),getStringByResourceId(R.string.old_score),getStringByResourceId(R.string.all)};
        mScore.setSelectArray(arrays);
    }


    private String getStringByResourceId(int resoureId){
        return BowlingApplication.getContext().getResources().getString(resoureId);
    }


    private void addScore(){
        mScore = new RemoteBattlerTopItem(getContext());
        String[] arrays = new String[]{getStringByResourceId(R.string.new_score),getStringByResourceId(R.string.old_score)};
        String[] contents = new String[]{getStringByResourceId(R.string.new_score)};
        mScore.setTitle(R.string.battler_title_score)
                .setContent(contents)
                .setSelectArray(arrays)
                .setStyle(RemoteBattlerTopView.STYLE_SCORE)
                .setOnSelectLisenter(new RemoteBattlerTopItem.RemoteBattlerTopItemSelectListener() {
                    @Override
                    public void select(String str, int positon,boolean select,boolean all) {
                        mScore.setContent(str);
                        if( mTopListener != null){
                            if(positon < 2){
                                str = positon + "";
                            }else{
                                str = "0,1";
                            }
                            mTopListener.selectText(STYLE_SCORE,str,positon,select,all);
                        }
                    }
                });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,1F);
        params.leftMargin = (int) (DeviceUtils.getDestiny() * MARGIN_13);
        addView(mScore,params);
    }

    private void addBattlerButton(){
        mButton = new Button(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) ( WIDTH_BUTTON * DeviceUtils.getDestiny()),LayoutParams.MATCH_PARENT);
        params.leftMargin = (int) (DeviceUtils.getDestiny() * MARGIN_13);
        addView(mButton,params);
        mButton.setText(getContext().getResources().getString(R.string.battler_title_create));
        mButton.setTypeface(TypefaceUtil.getStyleOneInstance());
        mButton.setTextColor(Color.WHITE);
        mButton.setGravity(Gravity.CENTER);
        mButton.setBackgroundResource(R.drawable.btn_common);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) BowlingUtils.getMainActivity();
                if(activity != null && activity.hasRemoetOnlineId()){
                    ToastUtil.showText(getContext(),R.string.room_has_create);
                    return;
                }
                if(activity != null){
                    if(activity.hasLocalMatch()){
                        BowlingCreateNewRoomDialog dialog = new BowlingCreateNewRoomDialog(activity);
                        dialog.show();
                    }
                }
            }
        });
    }


    public void setBattlerButtonVisible(int visible){
        if(mButton != null){
            mButton.setVisibility(visible);
        }
    }

    public void setScoreCategoryVisible(int visible){
        if(mScore != null){
            mScore.setVisibility(visible);
        }
    }

    private TopViewSelectListener mTopListener;
    public interface TopViewSelectListener{
        void selectText(int style,String str,int position,boolean select,boolean all);
    }

    public void setTopViewListener(TopViewSelectListener topViewListener){
        mTopListener = topViewListener;
    }



    public void setPlayingGameInfo(BowlerGameForTurn playingGameInfo){
        mTurn = playingGameInfo;
    }
}

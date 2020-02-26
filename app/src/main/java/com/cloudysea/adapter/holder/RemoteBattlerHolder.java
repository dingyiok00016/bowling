package com.cloudysea.adapter.holder;

import android.app.Activity;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.bean.OnLineGameIdBean;
import com.cloudysea.bean.PlayingGame;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.net.WebSocketClientService;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.ui.NetPresenter;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.DateCovertUtils;
import com.cloudysea.utils.ToastUtil;
import com.cloudysea.views.BowlingPasswordDialog;
import com.cloudysea.views.BowlingRemoteScoreDiloag;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.UUID;

/**
 * @author roof 2019/10/17.
 * @email lyj@yhcs.com
 * @detail
 */
public class RemoteBattlerHolder extends RecyclerView.ViewHolder {
    private TextView mTvLocation;
    private TextView mTvName;
    private TextView mTvChannel;
    private TextView mTvNewCount;
    private TextView mTvStyle;
    private TextView mTvStartTime;
    private TextView mTvPeopleCount;
    private TextView mTvVs;
    private TextView mTvNow;
    private TextView mTvJoin;
    private TextView mTvWatch;
    private PlayingGame.DataBean mDataBean;
    private NetPresenter mPresenter;




    public RemoteBattlerHolder(View itemView) {
        super(itemView);
        mTvName = (TextView) itemView.findViewById(R.id.tv_stadiums_name);
        mTvLocation = (TextView) itemView.findViewById(R.id.tv_remote_battler_location);
        mTvStyle = (TextView) itemView.findViewById(R.id.tv_remote_battler_style);
        mTvChannel = (TextView) itemView.findViewById(R.id.tv_remote_battler_channel);
        mTvNewCount = (TextView) itemView.findViewById(R.id.tv_remote_battler_people_count);
        mTvStartTime = (TextView) itemView.findViewById(R.id.tv_remote_battler_start_time);
        mTvPeopleCount = (TextView) itemView.findViewById(R.id.tv_remote_battler_people_count);
        mTvVs = (TextView) itemView.findViewById(R.id.tv_remote_battler_vs);
        mTvNow = (TextView) itemView.findViewById(R.id.tv_remote_battler_score);
        mTvJoin = (TextView) itemView.findViewById(R.id.tv_remote_battler_join);
        mTvWatch = (TextView) itemView.findViewById(R.id.tv_remote_battler_watch);
        LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.ll_content_container);



        // 手机适配

        if(!BowlingUtils.isPad()){
            mTvJoin.getLayoutParams().height *= 0.6F;
            mTvJoin.getLayoutParams().width *= 0.6F;
            mTvWatch.getLayoutParams().height *= 0.6F;
            mTvWatch.getLayoutParams().width *= 0.6F;
            mTvJoin.setTextSize(20 * BowlingUtils.Glbal_SIZE_RADIO);
            mTvWatch.setTextSize(20 * BowlingUtils.Glbal_SIZE_RADIO);
            RelativeLayout.LayoutParams marginParams = (RelativeLayout.LayoutParams) mTvWatch.getLayoutParams();
            marginParams.rightMargin = (int) (100 * DeviceUtils.getDestiny());
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
            params.rightMargin = (int) (140F * DeviceUtils.getDestiny());
        }

        mTvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
                // 是否有远程id,有的情况下,说明有对局,不能加入.除非是处于旁观情况.这个情况下也有远程id -_-
                if(mainActivity.hasRemoetOnlineId() && !mainActivity.isWatchStatus()){
                    ToastUtil.showText(BowlingApplication.getContext(),R.string.you_have_a_game);
                    return;
                }
                // 判断是否球局已经开始,没有不让加入
                if(!mainActivity.hasLocalMatch()){
                    ToastUtil.showText(BowlingApplication.getContext(),R.string.current_channel_no_start);
                    return;
                }
                // 下面是加入的密码
                // 判断是否有密码
                if(mDataBean.getConfigurationObj() != null && !TextUtils.isEmpty(mDataBean.getConfigurationObj().getEntryPassword())){
                    final BowlingPasswordDialog dialog = new BowlingPasswordDialog(mainActivity,BowlingPasswordDialog.SOURCE_JOIN_GAME);
                    dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(v.getTag() == null){
                                return;
                            }
                            String tag = (String) v.getTag();
                            if(!TextUtils.isEmpty(tag) && tag.equals(mDataBean.getConfigurationObj().getEntryPassword())){
                                joinGame(mDataBean.getId());
                                dialog.dismiss();
                            }else{
                                ToastUtil.showText(BowlingApplication.getContext(),R.string.pwd_enter_error);
                            }
                        }
                    });
                    dialog.show();
                }else{
                    joinGame(mDataBean.getId());
                }
            }
        });
        mTvWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 旁观按钮的显示逻辑在bind中处理，必须当前球道没有开始远程球局
                watchGame();
            }
        });
    }

    private void watchGame(){
        MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
        OnLineGameIdBean onLineGameIdBean = new OnLineGameIdBean();
        onLineGameIdBean.onLineGameId = mDataBean.getId();
        onLineGameIdBean.isWatch = true;
        try {
            onLineGameIdBean.laneNumber = BowlingUtils.Global_LANE_NUMBER;
        } catch (Exception e) {
            e.printStackTrace();
        }
        BowlingManager.getInstance().onLineId.executeListeners(onLineGameIdBean);
        if (mainActivity != null) {
            //
            WebSocketClientService.getInstance().handleMsg(onLineGameIdBean.onLineGameId);
            mainActivity.switchToRemotePage();
        }
    }

    public void setPresenter(NetPresenter netPresenter){
        mPresenter = netPresenter;
    }

    private void joinGame(String onlineGameId){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("AuthCode","120");
            jsonObject.put("Id",UUID.randomUUID().toString());
            jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER);
            jsonObject.put("Name","JoinOnlineGame");
            jsonObject.put("Type","0");
            JSONObject child = new JSONObject();
            child.put("OnlineGameId",onlineGameId);
            jsonObject.put("Data",child);
            BowlingClient.getInstance().handleMsg(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void bind(PlayingGame.DataBean dataBean,int position){
        // 背景
        if(position % 3 == 0){
            itemView.setBackgroundResource(R.drawable.bg_01);
        }else if(position % 3 == 1){
            itemView.setBackgroundResource(R.drawable.bg_02);
        }else{
            itemView.setBackgroundResource(R.drawable.bg_03);
        }
        // 是否显示旁观和加入
        MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
        if(mainActivity != null && mainActivity.hasRemoetOnlineId() && !mainActivity.isWatchStatus()){
            mTvJoin.setVisibility(View.GONE);
            mTvWatch.setVisibility(View.GONE);
        }else if(dataBean.getConfigurationObj() != null && !dataBean.getConfigurationObj().isAllowWatch()){
            mTvWatch.setVisibility(View.GONE);
            mTvJoin.setVisibility(View.VISIBLE);
        }else{
            mTvJoin.setVisibility(View.VISIBLE);
            mTvWatch.setVisibility(View.VISIBLE);
        }

        mDataBean = dataBean;
        mTvName.setText(dataBean.getCreateMerchantName());
        mTvStartTime.setText(DateCovertUtils.getInstance().covertPlayingTime(dataBean.getStartTime()));
        mTvPeopleCount.setText(String.format(Locale.getDefault(),"%d",dataBean.getTurnCount()));
        if(dataBean.getConfigurationObj() != null){
            PlayingGame.DataBean.ConfigurationObjBean configurationObjBean = dataBean.getConfigurationObj();
            // 球道
            if(!TextUtils.isEmpty(configurationObjBean.getAllowedLaneCategory())){
                if(configurationObjBean.getAllowedLaneCategory().equals("0")){
                    mTvChannel.setText("非标准");
                }else if(configurationObjBean.getAllowedLaneCategory().equalsIgnoreCase("1")){
                    mTvChannel.setText("标准");
                }else{
                    mTvChannel.setText("全部");
                }
            }
            // 计分模式
            if(!TextUtils.isEmpty(configurationObjBean.getScoreCategory())){
                if(configurationObjBean.getScoreCategory().equalsIgnoreCase("New")){
                    mTvStyle.setText("新计分");
                }else{
                    mTvStyle.setText("旧计分");
                }
            }
            // 设备
            if(!TextUtils.isEmpty(configurationObjBean.getAllowedDeviceCategory())){
                if(configurationObjBean.getAllowedDeviceCategory().contains("0")){
                    mTvLocation.setText("AM");
                }else if(configurationObjBean.getAllowedDeviceCategory().contains("1")){
                    mTvLocation.setText("宾世域");
                }else if(configurationObjBean.getAllowedDeviceCategory().contains("2")){
                    mTvLocation.setText("中路");
                }else if(configurationObjBean.getAllowedDeviceCategory().equalsIgnoreCase("3")){
                    mTvLocation.setText("希玛");
                }
            }

            mTvVs.setText(String.format(Locale.getDefault(),"%d",configurationObjBean.getTurnCount()));
            /*if(configurationObjBean.getAllowedLaneCategory().equalsIgnoreCase("All")){
                mTvNewCount.setText("全部");
            }else if(configurationObjBean.getAllowedLaneCategory().equalsIgnoreCase("New")){
                mTvNewCount.setText("新计分");
            }else {
                mTvNewCount.setText("旧计分");
            }*/
        }
        mTvNow.setText(String.format(Locale.getDefault(),"%d",dataBean.getCurrentTurnNumber()));


    }
}

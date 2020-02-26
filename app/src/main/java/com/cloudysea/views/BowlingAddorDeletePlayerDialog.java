package com.cloudysea.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cloudysea.R;
import com.cloudysea.adapter.AddPlayerAdapter;
import com.cloudysea.adapter.CurrentPlayerAdapter;
import com.cloudysea.bean.GetMembership;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.TypefaceUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author roof 2019/9/17.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingAddorDeletePlayerDialog extends BowlingCommonDialog {

    private static final float HON_SET_ADD = 17.5F;
    private static final float HON_SET_DEL = 25.5F;

    private List<PlayerBean> mBeans = new ArrayList<>();
    private CurrentPlayerAdapter mCurrentPlayerAdapter;
    private AddPlayerAdapter mAddPlayerAdapter;
    public BowlingAddorDeletePlayerDialog setPlayerList(@NonNull List<PlayerBean> playerList){
        if(playerList != null){
            this.mBeans.clear();
            this.mBeans.addAll(playerList);
            if(mCurrentPlayerAdapter != null){
                mCurrentPlayerAdapter.setPlayerList(this.mBeans);
                mCurrentPlayerAdapter.notifyDataSetChanged();
            }
        }
        return this;
    }

    @Override
    public int getDialogStyle() {
        return ALL_SCREEN_STYLE;
    }

    public BowlingAddorDeletePlayerDialog(@NonNull Context context) {
        super(context);
    }



    @Override
    public int getChildViewLayout() {
        return R.layout.dialog_add_or_delete_player;
    }


    protected void initView(View view){

        // 初始化字体
        initTypeFace(view);
        // 现有成员
        initCurrentRecylerView(view);
        // 添加成员
        initAddOrDeleteRecylerView(view);
    }


    private void initTypeFace(View view){
        TextView textViewTop = (TextView) view.findViewById(R.id.tv_title_current_player);
        textViewTop.setTypeface(TypefaceUtil.getStyleOneInstance());
        TextView textViewBottom = (TextView) view.findViewById(R.id.tv_title_add_player);
        textViewBottom.setTypeface(TypefaceUtil.getStyleOneInstance());
        TextView mTvAddDelete = (TextView) view.findViewById(R.id.tv_complete_action);

        if(!BowlingUtils.isPad()){
            textViewTop.setText(R.string.current_players_up_and_down);
            textViewTop.setTextSize(20);
            textViewBottom.setTextSize(20);
            mTvAddDelete.setTextSize(25);
        }
        mTvAddDelete.setTypeface(TypefaceUtil.getStyleTwoInstance());
        mTvAddDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PlayerBean> mCurrentBeans= mCurrentPlayerAdapter.getCurrentArrays();
                List<PlayerBean> mAddBeans = mAddPlayerAdapter.getAddPlayers();
                List<PlayerBean> finalBeans = new ArrayList<>();
                finalBeans.addAll(mCurrentBeans);
                finalBeans.addAll(mAddBeans);
                List<String> mRemoveIds= mCurrentPlayerAdapter.getRemovedId();
                if(mRemoveIds.size() > 0 || mAddBeans.size() > 0){
                    buildAddOrRemoveInfo(mRemoveIds,mAddBeans);
                //    BowlingManager.getInstance().addOrDeleteListener.executeListeners(finalBeans);
                }
                dismiss();
            }
        });
    }

    private void buildAddOrRemoveInfo(List<String> removeArray,List<PlayerBean> players){
        JSONObject jsonObject = new JSONObject();
        JSONObject Data = new JSONObject();
        try {
            jsonObject.put("Data",Data);
            JSONArray RemovedBowlerIds = new JSONArray();
            if(removeArray.size() > 0){
                for(String str:removeArray){
                    RemovedBowlerIds.put(str);
                }
            }
            Data.put("RemovedBowlerIds",RemovedBowlerIds);
            if(players.size() > 0){
                JSONArray AddBowlers = new JSONArray();
                Data.put("AddBowlers",AddBowlers);
                for(PlayerBean playerBean:players){
                    JSONObject playObject = new JSONObject();
                    playObject.put("HeadPortrait",playerBean.HeadPortrait);
                    playObject.put("Name",playerBean.BowlerName);
                    playObject.put("IsMembership",playerBean.IsMembership);
                    playObject.put("MembershipNumber",playerBean.MembershipNumber);
                    AddBowlers.put(playObject);
                }
            }
            jsonObject.put("Id",UUID.randomUUID().toString());
            jsonObject.put("Name","ChangeBowlers2");
            jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER);
            jsonObject.put("AuthCode","120");
            jsonObject.put("Type","0");
            BowlingClient.getInstance().handleMsg(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        BowlingManager.getInstance().addOrDeleteListener.clearListener();
        BowlingManager.getInstance().imageListener.clearListener();
        super.dismiss();
    }

    private void initCurrentRecylerView(View view){
        RecyclerView currentRecylerView = (RecyclerView) view.findViewById(R.id.rv_current_players);
        currentRecylerView.addItemDecoration(new SpacesItemDecoration(HON_SET_DEL));
        setLayoutManager(currentRecylerView);
        mCurrentPlayerAdapter = new CurrentPlayerAdapter(mBeans);
        currentRecylerView.setAdapter(mCurrentPlayerAdapter);
    }

    private void initAddOrDeleteRecylerView(View view){
        RecyclerView addRecylerView = (RecyclerView) view.findViewById(R.id.rv_current_add_players);
        setLayoutManager(addRecylerView);
        addRecylerView.addItemDecoration(new SpacesItemDecoration(HON_SET_ADD));
        mAddPlayerAdapter = new AddPlayerAdapter();
        mAddPlayerAdapter.bindAdapter(mCurrentPlayerAdapter);
        addRecylerView.setAdapter(mAddPlayerAdapter);
    }


    public void setVipInfo(GetMembership getMembership){
        if(mAddPlayerAdapter != null){
            PlayerBean playerBean = mAddPlayerAdapter.getAddPlayers().get(BowlingUtils.Global_ME_VIP_POSITION);
            playerBean.HeadPortrait = getMembership.getData().getHeadPortrait();
            playerBean.BowlerName = getMembership.getData().getNameX();
            playerBean.IsMembership = true;
            playerBean.MembershipNumber = getMembership.getData().getMembershipNumber();
            mAddPlayerAdapter.setNewPlayer(BowlingUtils.Global_ME_VIP_POSITION,playerBean);
            mAddPlayerAdapter.notifyItemChanged(BowlingUtils.Global_ME_VIP_POSITION);
        }
    }


}

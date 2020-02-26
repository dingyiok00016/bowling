package com.cloudysea.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.adapter.ChangeScoreAdapter;
import com.cloudysea.adapter.CurrentPlayerAdapter;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.ToastUtil;
import com.cloudysea.utils.TypefaceUtil;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author roof 2019/9/21.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingChangeScoreDialog extends BowlingCommonDialog {

    private final static int HON_SET_CHNAGE_SCORE = 93;
    private PlayerBean mPlayeBean;
    private int mIndex;
    private ChangeScoreAdapter changeScoreAdapter;
    private RecyclerView currentRecylerView;
    private String mCurrentTurnId;


    public BowlingChangeScoreDialog(@NonNull Context context) {
        super(context);
        mCurrentTurnId = BowlingUtils.CURRENT_TURN_ID;
    }

    @Override
    protected int getDialogStyle() {
        return ALL_SCREEN_STYLE;
    }

    @Override
    protected void initView(View view) {
        TextView textViewComplete = (TextView) findViewById(R.id.tv_complete_action);
        textViewComplete.setTypeface(TypefaceUtil.getStyleTwoInstance());
        TextView textView = (TextView) findViewById(R.id.tv_title_change_score);
        if(!BowlingUtils.isPad()){
            textView.setText(R.string.modify_score_up_down);
        }
        textViewComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildChangeScoreInfo();
                dismiss();
            }
        });
        initRecylerView(view);
        initTypeface(view);
    }

    private void buildChangeScoreInfo(){
        if(mCurrentTurnId != null && !mCurrentTurnId.equalsIgnoreCase(BowlingUtils.CURRENT_TURN_ID)){
            ToastUtil.showText(BowlingApplication.getContext(),"未在当前局下，不能修改");
            dismiss();
        }

        JSONObject jsonObject = new JSONObject();
        JSONObject Data= new JSONObject();
        try {
            jsonObject.put("Data",Data);
            jsonObject.put("AuthCode","120");
            jsonObject.put("Type","0");
            jsonObject.put("Name","ModifyScore");
            jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER);
            Data.put("BowlerId",mPlayeBean.Id);
            Data.put("RoundNumber",mIndex);
            JSONObject First = new JSONObject();
            fillPinState(First,0);
            Data.put("First",First);
            JSONObject Second = new JSONObject();
            fillPinState(Second,1);
            Data.put("Second",Second);
            JSONObject Third = new JSONObject();
            fillPinState(Third,2);
            Data.put("Third",Third);
            BowlingClient.getInstance().handleMsg(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillPinState(JSONObject jsonObject,int i){
        mPlayeBean = changeScoreAdapter.getPlayerBean();
        if(mPlayeBean.pppp.get(i) != null){
            JSONArray PinStandStates = new JSONArray();
            try {
                jsonObject.put("PinStandStates",PinStandStates);
                List<Boolean> booleanList = mPlayeBean.pppp.get(i);
                List<Boolean> initList = mPlayeBean.qqqq.get(i);
                boolean isModified = false;
                for(int k = 0; k < booleanList.size();k++){
                    PinStandStates.put(booleanList.get(k));
                    if(booleanList.get(k) != initList.get(k)){
                        isModified = true;
                    }
                }
                if(mPlayeBean.rrrr.get(i) != mPlayeBean.oooo.get(i)){
                    isModified = true;
                }
                if(mPlayeBean.mmmm.get(i) != mPlayeBean.nnnn.get(i)){
                    isModified = true;
                }
                jsonObject.put("HasScore",mPlayeBean.HasScore.get(i));
                jsonObject.put("IsModified",isModified);
                jsonObject.put("IsBreakRule",mPlayeBean.oooo.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initRecylerView(View view){
        currentRecylerView = (RecyclerView) view.findViewById(R.id.rl_change_score);
        currentRecylerView.addItemDecoration(new SpacesItemDecoration(HON_SET_CHNAGE_SCORE));
        setLayoutManager(currentRecylerView);
        changeScoreAdapter = new ChangeScoreAdapter();
    }

    private void initTypeface(View view){
        TextView textView = (TextView) findViewById(R.id.tv_title_change_score);
        textView.setTypeface(TypefaceUtil.getStyleOneInstance());
    }

    public void setPlayBean(PlayerBean playBean,int index){
        mPlayeBean = playBean;
        mIndex = index;
        if(changeScoreAdapter != null){
            changeScoreAdapter.setPlayerBean(mPlayeBean,mIndex);
            if(mPlayeBean.index == 9 && !mPlayeBean.isNew){
                changeScoreAdapter.setCount(3);
            }
        }
        currentRecylerView.setAdapter(changeScoreAdapter);
    }


    @Override
    public int getChildViewLayout() {
        return R.layout.dialog_change_score;
    }
}

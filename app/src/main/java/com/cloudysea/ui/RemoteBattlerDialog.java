package com.cloudysea.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.adapter.RemoteBattlerAdapter;
import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.PlayingGame;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.views.BowlingCommonDialog;
import com.cloudysea.views.RemoteBattlerTopItem;
import com.cloudysea.views.RemoteBattlerTopView;
import com.cloudysea.views.SpacesItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roof 2019/10/17.
 * @email lyj@yhcs.com
 * @detail
 */
public class RemoteBattlerDialog extends BowlingCommonDialog {

    private RecyclerView mRvList;
    private RemoteBattlerAdapter mAdapter;
    private RemoteBattlerTopView mTopView;
    private List<Integer> mDeviceCategory;
    private String mDeviceStr;
    private String mLaneStr;
    private String mScoreStr;
    private String mId;

    public RemoteBattlerDialog(@NonNull Context context) {
        super(context);
    }

    public void setId(String id){
        mId = id;
    }

    public void setPlayingGame(PlayingGame bowlerGameForTurn){
        mAdapter.setPlayingGame(bowlerGameForTurn);
    }

    public void setCurrentTurn(BowlerGameForTurn turn){
        mTopView.setPlayingGameInfo(turn);
    }

    @Override
    protected int getDialogStyle() {
        return ALL_SCREEN_STYLE;
    }

    @Override
    protected void initView(View view) {
        mDeviceCategory = new ArrayList<>();
        mDeviceCategory.add(0);
        mDeviceCategory.add(1);
        mDeviceCategory.add(2);
        mDeviceCategory.add(3);
        mDeviceCategory.add(4);
        mDeviceStr  = "0,1,2,3,4";
        mLaneStr = "1";
        mScoreStr = "New";
        mTopView = (RemoteBattlerTopView) findViewById(R.id.rt_remote_battler);
        mTopView.setShowStyle();
        mRvList = (RecyclerView) findViewById(R.id.rv_data_info);
        mRvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, (int) (10 * DeviceUtils.getDestiny()));
            }
        });
        mAdapter = new RemoteBattlerAdapter();
        mRvList.setAdapter(mAdapter);
        mTopView.setTopViewListener(new RemoteBattlerTopView.TopViewSelectListener() {
            @Override
            public void selectText(int sthye, String str, int position,boolean select,boolean all) {
                MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
                if(mainActivity != null && !mainActivity.isFinishing()){
                    JSONObject jsonObject = new JSONObject();
                    try {
                        if(sthye == RemoteBattlerTopView.STYLE_DEVICE){
                            if (select) {
                                if (!mDeviceCategory.contains(position)) {
                                    mDeviceCategory.add(position);
                                }
                            } else {
                                mDeviceCategory.remove(Integer.valueOf(position));
                            }
                            StringBuilder sb = new StringBuilder();
                            if (mDeviceCategory.contains(0)) {
                                sb.append("0,");
                            }
                            if (mDeviceCategory.contains(1)) {
                                sb.append("1,");
                            }
                            if (mDeviceCategory.contains(2)) {
                                sb.append("2,");
                            }
                            if (mDeviceCategory.contains(3)) {
                                sb.append("3,");
                            }
                            if(mDeviceCategory.contains(4)){
                                sb.append("4,");
                            }
                            if(sb.length() > 0){
                                sb.delete(sb.length() - 1,sb.length());
                            }
                            mDeviceStr = sb.toString();

                            StringBuilder sbContent = new StringBuilder();
                            if (mDeviceCategory.contains(0)) {
                                sbContent.append(BowlingApplication.getContext().getResources().getString(R.string.bin_area) + "，");
                            }
                            if (mDeviceCategory.contains(1)) {
                                sbContent.append("AMF,");
                            }
                            if (mDeviceCategory.contains(2)) {
                                sbContent.append( BowlingApplication.getContext().getResources().getString(R.string.xima_area)+ ",");
                            }
                            if (mDeviceCategory.contains(3)) {
                                sbContent.append(BowlingApplication.getContext().getResources().getString(R.string.mid_area) + ",");
                            }
                            if(mDeviceCategory.contains(4)){
                                sbContent.append(BowlingApplication.getContext().getResources().getString(R.string.other_area) + ",");
                            }
                            if(sbContent.length() > 0){
                                sbContent.delete(sbContent.length() - 1,sbContent.length());
                            }
                            mTopView.setDeviceContent(sbContent.toString());

                        }else if(sthye == RemoteBattlerTopView.STYLE_CHANNEL){
                            mLaneStr  = str;
                        }else if(sthye == RemoteBattlerTopView.STYLE_SCORE){
                            if(str.equals("0")){
                                mScoreStr = "New";
                            }else if(str.equalsIgnoreCase("1")){
                                mScoreStr = "Old";
                            }else{
                                mScoreStr = "New,Old";
                            }

                        }
                        jsonObject.put("AllowedDeviceCategory",mDeviceStr);
                        jsonObject.put("AllowedLaneCategory",mLaneStr);
                        jsonObject.put("ScoreCategory",mScoreStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mainActivity.getPlayingGame(jsonObject);
                }
            }
        });
    }



    @Override
    public int getChildViewLayout() {
        return R.layout.dialog_remote_batter;
    }
}

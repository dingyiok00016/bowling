package com.cloudysea.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.adapter.holder.RemoteBattlerHolder;
import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.BowlerGameSingLine;
import com.cloudysea.bean.GameBasicInfo;
import com.cloudysea.bean.PlayingGame;
import com.cloudysea.controller.BaseListener;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.ui.NetPresenter;
import com.cloudysea.ui.NetView;
import com.cloudysea.utils.BowlingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

/**
 * @author roof 2019/10/17.
 * @email lyj@yhcs.com
 * @detail 远程队长adapter
 */
public class RemoteBattlerAdapter extends RecyclerView.Adapter<RemoteBattlerHolder> implements NetView {
    private List<PlayingGame.DataBean> mDatas;
    private NetPresenter mPresenter;


    public RemoteBattlerAdapter(){
        mPresenter = new NetPresenter(this);
    }


    public void setPlayingGame(PlayingGame playingGame){
        mDatas = playingGame.getData();
        notifyDataSetChanged();
    }



    @Override
    public RemoteBattlerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(BowlingApplication.getContext()).inflate(R.layout.item_remote_battler
        ,parent,false);
        RemoteBattlerHolder remoteBattlerHolder = new RemoteBattlerHolder(view);
        remoteBattlerHolder.setPresenter(mPresenter);
        return remoteBattlerHolder;
    }

    @Override
    public void onBindViewHolder(RemoteBattlerHolder holder, int position) {
        PlayingGame.DataBean dataBean = mDatas.get(position);
        holder.bind(dataBean,position);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public void getBasicGameInfo(PlayingGame gameBasicInfo) {

    }

    @Override
    public void getBowlerGameForTurn(BowlerGameForTurn bowlerGameForTurn) {

    }

    @Override
    public void getBowlerGame(BowlerGameSingLine singLine) {


    }

    @Override
    public void getBowlerBasicInfo(GameBasicInfo gameBasicInfo, String onLineId) {

    }
}

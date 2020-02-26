package com.cloudysea.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.adapter.holder.ChangeScoreHolder;
import com.cloudysea.adapter.holder.ChannelSetHolder;
import com.cloudysea.bean.PlayerBean;

/**
 * @author roof 2019/9/21.
 * @email lyj@yhcs.com
 * @detail
 */
public class ChangeScoreAdapter extends RecyclerView.Adapter {

    private int mCount = 2;
    private PlayerBean mPlayerBean;
    private int mIndex;

    public void setPlayerBean(PlayerBean playerBean,int index){
        mPlayerBean = playerBean;
        mIndex = index;
    }

    public PlayerBean getPlayerBean(){
        return mPlayerBean;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(BowlingApplication.getContext()).inflate(R.layout.item_ball_score_view,parent,false);
        ChangeScoreHolder holder = new ChangeScoreHolder(view);
        holder.setAdapter(this,mPlayerBean,mIndex);
        return holder;
    }

    public void setCount(int count){
        mCount = count;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChangeScoreHolder changeScoreHolder = (ChangeScoreHolder) holder;
        changeScoreHolder.bindData(position);
    }


    @Override
    public int getItemCount() {
        return mCount;
    }
}

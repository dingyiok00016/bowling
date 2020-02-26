package com.cloudysea.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.adapter.holder.RemoteScoreHolder;
import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.GameBasicInfo;
import com.cloudysea.views.HorizontalRemoteScoreContainer;

/**
 * @author roof 2019/10/26.
 * @email lyj@yhcs.com
 * @detail
 */
public class RemoteBallScoreAdapter extends RecyclerView.Adapter<RemoteScoreHolder> {

    private BowlerGameForTurn mTurnInfo;
    private GameBasicInfo.DataBean mBasicInfo;

    public RemoteBallScoreAdapter(GameBasicInfo.DataBean gameBasicInfo) {
        mBasicInfo= gameBasicInfo;
    }

    public void setTurnInfo(BowlerGameForTurn turnInfo) {
        mTurnInfo = turnInfo;
    }


    @Override
    public RemoteScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HorizontalRemoteScoreContainer convertView = (HorizontalRemoteScoreContainer) LayoutInflater.from(BowlingApplication.getContext())
                .inflate(R.layout.horizontal_remote_score_view, parent, false);
        return new RemoteScoreHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RemoteScoreHolder holder, int position) {
        BowlerGameForTurn.DataBean.BowlerGamesBean bowlerGamesBean = mTurnInfo.getData().getBowlerGames().get(position);
        holder.bindData(position, bowlerGamesBean,mBasicInfo);
    }

    @Override
    public int getItemCount() {
        return (mTurnInfo == null || mTurnInfo.getData() == null || mTurnInfo.getData().getBowlerGames() == null) ? 0 : mTurnInfo.getData().getBowlerGames().size();
    }
}

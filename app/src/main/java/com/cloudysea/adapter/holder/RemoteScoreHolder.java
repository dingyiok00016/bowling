package com.cloudysea.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.GameBasicInfo;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.views.HorizontalRemoteScoreContainer;
import com.cloudysea.views.HorizontalScoreContainer;

/**
 * @author roof 2019/10/26.
 * @email lyj@yhcs.com
 * @detail
 */
public class RemoteScoreHolder extends RecyclerView.ViewHolder {
    private HorizontalRemoteScoreContainer mContainer;
    public RemoteScoreHolder(View itemView) {
        super(itemView);
        mContainer = (HorizontalRemoteScoreContainer) itemView;
    }

    public void bindData(int index, BowlerGameForTurn.DataBean.BowlerGamesBean gamesBean, GameBasicInfo.DataBean basicInfo){
        MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
        if(mainActivity != null){
            mContainer.setMode(basicInfo != null  && basicInfo.getConfigurationObj() != null &&
                    basicInfo.getConfigurationObj().getScoreCategory().equalsIgnoreCase("New"));
            mContainer.initView(mainActivity,false,index,false,gamesBean,false);
        }
    }

}

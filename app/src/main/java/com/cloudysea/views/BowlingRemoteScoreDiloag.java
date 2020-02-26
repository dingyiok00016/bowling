package com.cloudysea.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cloudysea.R;
import com.cloudysea.adapter.RemoteBallScoreAdapter;
import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.BowlerGameSingLine;
import com.cloudysea.controller.BaseListener;
import com.cloudysea.controller.BowlingManager;

/**
 * @author roof 2019/10/26.
 * @email lyj@yhcs.com
 * @detail 远程分界面
 */
public class BowlingRemoteScoreDiloag extends BowlingCommonDialog {

    private BowlerGameForTurn mTurn;
    private RemoteBallScoreAdapter mScoreAdapter;
    private RecyclerView mRecylerView;
    public void setRemoteInfo(BowlerGameForTurn gameForTurn){
        mTurn = gameForTurn;
        mScoreAdapter.setTurnInfo(gameForTurn);
        mRecylerView.setAdapter(mScoreAdapter);
    }

    private void getBaseListener(){
        if(baseListener == null){
            baseListener = new BaseListener<BowlerGameSingLine>() {
                @Override
                public void execute(BowlerGameSingLine o) {
                    if(mTurn != null && mTurn.getData() != null && mTurn.getData().getBowlerGames() != null
                            && mTurn.getData().getBowlerGames().size() > 0){
                        // 遍历寻找需要修改的id
                        for(int i = 0; i < mTurn.getData().getBowlerGames().size();i++){
                            if(mTurn.getData().getBowlerGames().get(i).getId().equals(o.getData().getId())){
                                final int n = i;
                                // 获取到当前i
                                if(mTurn.getData().getBowlerGames().get(i).getScore() == null){
                                    mTurn.getData().getBowlerGames().get(i).setScore(new BowlerGameForTurn.DataBean.BowlerGamesBean.ScoreBean());
                                }
                                mTurn.getData().getBowlerGames().get(i).getScore().setScores(o.getData().getScore().getScores());
                                mTurn.getData().getBowlerGames().get(i).getScore().setRoundTotalScores(o.getData().getScore().getRoundTotalScores());
                                mTurn.getData().getBowlerGames().get(i).getScore().setTotalScore(o.getData().getScore().getTotalScore());
                                mRecylerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mScoreAdapter.notifyItemChanged(n);//更新数据
                                    }
                                });
                                break;
                            }

                        }
                    }
                }
            };
        }
    }


    BaseListener baseListener;
    @Override
    public void dismiss() {
        BowlingManager.getInstance().singleGame.removeListener(baseListener);
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
        getBaseListener();
        BowlingManager.getInstance().singleGame.addListener(baseListener);
    }

    public BowlingRemoteScoreDiloag(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getDialogStyle() {
        return ALL_SCREEN_STYLE;
    }

    @Override
    protected void initView(View view) {
        mRecylerView = (RecyclerView) view.findViewById(R.id.rv_remote_score_info);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
    //    mScoreAdapter = new RemoteBallScoreAdapter();
    }

    @Override
    public int getChildViewLayout() {
        return R.layout.dialog_remote_score;
    }
}

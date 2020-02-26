package com.cloudysea.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cloudysea.R;
import com.cloudysea.adapter.RemoteBallScoreAdapter;
import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.BowlerGameSingLine;
import com.cloudysea.bean.GameBasicInfo;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.BaseListener;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.fragment.BaseScoreFragment;
import com.cloudysea.utils.BowlingUtils;

/**
 * @author roof 2019/10/27.
 * @email lyj@yhcs.com
 * @detail 远程对战界面
 */
public class RemoteScoreFragment extends BaseScoreFragment {


    private boolean isCreated;
    private boolean hasDataShouldRefresh;
    private int mCurrentLane;
    private GameBasicInfo.DataBean mDatabean;


    private BaseListener<BowlerGameForTurn> currentGame = new BaseListener<BowlerGameForTurn>() {
        @Override
        public void execute(final BowlerGameForTurn scoreListBean) {
            if (!isDetached()) {
                // 刷新球局信息
                getView().post(new Runnable() {
                    @Override
                    public void run() {
                        if(scoreListBean.getData().getTurnNumber() - 1 == mCurrentLane){
                            setRemoteInfo(mDatabean, scoreListBean);
                        }
                    }
                });
            }
        }
    };

    public boolean isCreated(){
        return isCreated;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.dialog_remote_score,null);
        mDatabean = (GameBasicInfo.DataBean) getArguments().getSerializable(RemoteMatchFragment.EXTRA_OBJECT);
        mCurrentLane = getArguments().getInt(RemoteMatchFragment.EXTRA_LANE,0);
        BowlingManager.getInstance().currentGame.addListener(currentGame);
        return view;
    }

    public BowlerGameForTurn getRemoteInfo(){
        return mTurn;
    }

    public void setSingleInfo(int position){
        mScoreAdapter.notifyItemChanged(position);
    }

    private BowlerGameForTurn mTurn;
    private GameBasicInfo.DataBean mDataBean;
    private RemoteBallScoreAdapter mScoreAdapter;
    private RecyclerView mRecylerView;
    private LinearLayout linearLayout;
    public void setRemoteInfo(GameBasicInfo.DataBean dataBean, BowlerGameForTurn gameForTurn){
        if(!isCreated){
            mTurn = gameForTurn;
            mDataBean = dataBean;
            hasDataShouldRefresh  = true;
        }else{
            setLocalRemoteInfo(dataBean,gameForTurn);
        }
    }

    private void setLocalRemoteInfo(GameBasicInfo.DataBean dataBean,BowlerGameForTurn gameForTurn){
        if(gameForTurn != null && gameForTurn.getData() != null &&
                gameForTurn.getData().getBowlerGames() != null && gameForTurn.getData().getBowlerGames().size() > 0){
            addTitle(linearLayout,dataBean.getConfigurationObj() != null
                    && dataBean.getConfigurationObj().getScoreCategory().equalsIgnoreCase("New"));
        }
        mTurn = gameForTurn;
        mScoreAdapter.setTurnInfo(gameForTurn);
        if(mRecylerView.getAdapter() == null){
            mRecylerView.setAdapter(mScoreAdapter);
        }else{
            mScoreAdapter.notifyDataSetChanged();
        }
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isCreated = true;
        if(hasDataShouldRefresh){
            hasDataShouldRefresh = false;

            setLocalRemoteInfo(RemoteMatchFragment.mDatabean,mTurn);
        }
        initView(getView());
        getBaseListener();
        BowlingManager.getInstance().singleGame.addListener(baseListener);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isCreated = false;
        BowlingManager.getInstance().singleGame.removeListener(baseListener);
        BowlingManager.getInstance().currentGame.addListener(currentGame);
    }

    BaseListener baseListener;

    protected void initView(View view) {
        linearLayout = (LinearLayout) view.findViewById(R.id.ll_cover_for_score_title);
        mRecylerView = (RecyclerView) view.findViewById(R.id.rv_remote_score_info);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(!BowlingUtils.isPad()){
            View bottomView = view.findViewById(R.id.view_bottom_image);
            RelativeLayout.LayoutParams bottomParams = (RelativeLayout.LayoutParams) bottomView.getLayoutParams();
            bottomParams.bottomMargin = (int) (45 * DeviceUtils.getDestiny());
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRecylerView.getLayoutParams();
            params.bottomMargin = (int) (50 * DeviceUtils.getDestiny());
        }
        mScoreAdapter = new RemoteBallScoreAdapter(RemoteMatchFragment.mDatabean);
    }
}

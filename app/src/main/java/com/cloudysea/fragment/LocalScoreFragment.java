package com.cloudysea.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudysea.R;
import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.OnLineGameIdBean;
import com.cloudysea.bean.PlayingGame;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.controller.BaseListener;
import com.cloudysea.controller.BowlerGameForTurnListener;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.adapter.BallScoreAdapter;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.bean.ScoreListBean;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.controller.GetScoreListListener;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.ui.RemoteBattlerDialog;
import com.cloudysea.utils.BottomPersonInfoHelper;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.TypefaceUtil;
import com.cloudysea.views.BowlingAdDialog;
import com.cloudysea.views.BowlingRemoteScoreDiloag;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author roof 2019/9/21.
 * @email lyj@yhcs.com
 * @detail
 */
public class LocalScoreFragment extends BaseScoreFragment  {

    private static String TAG = "LocalScoreFragment";
    private boolean mIsCreated;
    private ListView mLvScore;
    private ImageView mProgrssBar;
    private List<PlayerBean> mPlayers = new ArrayList<>();
    private BallScoreAdapter adapter;
    public static final String EXTRA_LANE_NUMBER = "extra_lane_number";
    private int mLaneNumber;
    public static boolean mIsLoading;
    private LinearLayout linearLayout;
    private LinearLayout mLlLoading;
    private static final int DEFAULT_SCORE_COUNT = 10;
    private static final int ROUND_COUT = 6;
    private boolean mNowMode = true;
    private String mOnLineId = "";
    private boolean mIsWatch;

    public ListView getListView(){
        return mLvScore;
    }

    public boolean hasRemoteGame(){
        return !TextUtils.isEmpty(mOnLineId);
    }

    public String getRemoteOnLineId(){
        return mOnLineId;
    }


    private BaseListener<OnLineGameIdBean> baseListener = new BaseListener<OnLineGameIdBean>() {
        @Override
        public void execute(OnLineGameIdBean onLineGameIdBean) {
            if(onLineGameIdBean.laneNumber == mLaneNumber){
                mOnLineId = onLineGameIdBean.onLineGameId;
                mIsWatch = onLineGameIdBean.isWatch;
            }
        }
    };

    public List<PlayerBean> getPlayBeans(){
        return mPlayers;
    }


    private GetScoreListListener getScoreListListener = new GetScoreListListener() {
        @Override
        public void execute(final ScoreListBean scoreListBean) {
            if(scoreListBean == null || scoreListBean.Data == null){
                return;
            }
            if(!scoreListBean.LaneNumber.equals(mLaneNumber + "")){
                return;
            }

            final MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
            if(mainActivity != null && !mainActivity.isFinishing()){
                // 判断是否有球局
                if(scoreListBean.LaneNumber.equalsIgnoreCase(BowlingUtils.Global_LANE_NUMBER + "")){
                    if(!scoreListBean.Data.HasScore){
                        BowlingUtils.CURRENT_BOWLER_ID = null;
                    }
                    if(scoreListBean.Data.Scores != null && scoreListBean.Data.Scores.size() > 0){
                        BowlingUtils.CURRENT_TURN_ID = scoreListBean.Data.Scores.get(0).CurrentTurnId;
                    }else{
                        BowlingUtils.CURRENT_TURN_ID = "";
                    }
                    mLvScore.post(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.showOrHideBottomBowlerInfo(scoreListBean.Data.HasScore);
                        }
                    });
                }
                mLvScore.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        mainActivity.setGameInfo(scoreListBean.Data.GameInfo,scoreListBean.Data.IsExchangeMode);
                    }
                });
            }
            if(scoreListBean.Data.Scores != null){
                mPlayers = scoreListBean.Data.Scores;
                mLvScore.post(new Runnable() {
                    @Override
                    public void run() {
                        BottomPersonInfoHelper.getInstance().setPlayBean(mPlayers);
                    }
                });
                adapter.setMode(scoreListBean.Data.ScoreMode == null || scoreListBean.Data.ScoreMode.equals("New"));
                if(!scoreListBean.Data.IncludeHeadPortrait){
                    managerHeadPortrait(mPlayers);
                }else{
                    saveHeadPortrait(mPlayers);
                }
                adapter.setNewDatas(mPlayers,scoreListBean.Data.IncludeHeadPortrait);
                if(MainActivity.vpIsAllScreen()){
                    mLvScore.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                boolean isNewMode = scoreListBean.Data.ScoreMode == null || scoreListBean.Data.ScoreMode.equals("New");
                                if(scoreListBean.Data.Scores.size() > 0 && (linearLayout.getChildCount() == 0 || mNowMode != isNewMode)){
                                    addTitle(linearLayout,isNewMode);
                                    dismissDialog();
                                }else if(scoreListBean.Data.Scores.size() == 0){
                                    if(linearLayout.getChildCount() > 0){
                                        linearLayout.removeAllViews();
                                    }
                                    Log.d("LocalScoreFragment","showAdDialog");
                                    showAdDialog();
                                }
                                endAnimation();
                                mLvScore.setAdapter(adapter);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    mLvScore.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                boolean isNewMode = scoreListBean.Data.ScoreMode == null || scoreListBean.Data.ScoreMode.equals("New");
                                if(scoreListBean.Data.Scores.size() > 0  && (linearLayout.getChildCount() == 0 || mNowMode != isNewMode)){
                                    addTitle(linearLayout,isNewMode);
                                    dismissDialog();
                                }else if(scoreListBean.Data.Scores.size() == 0){
                                    if(linearLayout.getChildCount() > 0){
                                        linearLayout.removeAllViews();
                                    }
                                    showAdDialog();
                                    Log.d("LocalScoreFragment","showAdDialog");
                                }
                                endAnimation();
                                mLvScore.setAdapter(adapter);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    },500);
                }
            }
        }
    };

    private void managerHeadPortrait(List<PlayerBean> playerBeans){
        for(int i = 0; i < playerBeans.size();i++){
            if(playerBeans.get(i).HeadPortrait != null){
                continue;
            }else{
                if(BowlingUtils.getHead(playerBeans.get(i).Id) != null){
                    playerBeans.get(i).HeadPortrait = BowlingUtils.getHead(playerBeans.get(i).Id);
                }else{
                    playerBeans.get(i).resourcePath = "head" + (i  % ROUND_COUT + 1);;
                }

            }
        }
    }

    public boolean isWatch(){
        return mIsWatch;
    }

    public boolean hasScore(){
        return adapter != null && adapter.getCount() > 0 ;
    }

    private void saveHeadPortrait(List<PlayerBean> playerBeans){
        for(int i = 0; i < playerBeans.size();i++){
            if(playerBeans.get(i).HeadPortrait != null){
                BowlingUtils.addHead(playerBeans.get(i).Id, (String) playerBeans.get(i).HeadPortrait);
                continue;
            }
        }
    }



    public void getScore(){
            startAnimation();
            BowlingUtils.Global_LANE_NUMBER = mLaneNumber;
            // 重新连接球道
            BowlingClient.getInstance().connectRequest();
            // 查询本地球道分数
            BowlingClient.getInstance().getScore(mLaneNumber);
            mLvScore.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 查询bowlerInfo
                    BowlingClient.getInstance().getBowlerInfo(mLaneNumber);
                }
            },500);
            // 查询远程信息
            BowlingClient.getInstance().buildRemoteRoundInfo();
            mLvScore.postDelayed(new Runnable() {
                @Override
                public void run() {
                    endAnimation();
                }
            },3000);
    }


    private void startAnimation(){
        if(getContext() == null){
            return;
        }
        Animation rotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim);
        LinearInterpolator interpolator = new LinearInterpolator();
        rotateAnimation.setInterpolator(interpolator);
        mProgrssBar.startAnimation(rotateAnimation);
        mLlLoading.setVisibility(View.VISIBLE);
    }

    private void endAnimation(){
        mProgrssBar.clearAnimation();
        mLlLoading.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mIsCreated = true;
        return View.inflate(getContext(),R.layout.fragment_comment_view,null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        if(getArguments() != null){
            mLaneNumber = getArguments().getInt(EXTRA_LANE_NUMBER);
        }
        if(!mIsLoading){
            mIsLoading = true;
            getScore();
        }
        BowlingManager.getInstance().onLineId.addListener(baseListener);
        BowlingManager.getInstance().getScoreListListener.addListener(getScoreListListener);
    }

    private void initView(){
        if(!BowlingUtils.isPad()){
            RelativeLayout relativeLayout= (RelativeLayout) getView().findViewById(R.id.rl_comment_view_container);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) relativeLayout.getLayoutParams();
            params.bottomMargin = (int) (55 * DeviceUtils.getDestiny());
        }
        mLvScore = (ListView) getView().findViewById(R.id.container);
        mProgrssBar = (ImageView) getView().findViewById(R.id.loading);
        mLlLoading = (LinearLayout) getView().findViewById(R.id.ll_loading);
        linearLayout = (LinearLayout) getView().findViewById(R.id.ll_cover_for_score_title);
        adapter = new BallScoreAdapter((MainActivity) getActivity(),mPlayers);
        TextView textView = (TextView) getView().findViewById(R.id.tv_loading);
        textView.setTypeface(TypefaceUtil.getStyleTwoInstance());
        mLvScore.setAdapter(adapter);
    }

    public boolean isCreated(){
        return this.mIsCreated;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mIsCreated = false;
        BowlingManager.getInstance().onLineId.removeListener(baseListener);
        BowlingManager.getInstance().getScoreListListener.removeListener(getScoreListListener);
    }
    private boolean mIsShowAd;
    public void showAdDialog(){
        MainActivity mainActivity = (MainActivity) ActivityStacks.getInstance().getMainActivity();
        if(mainActivity != null && !mainActivity.isFinishing()){
            mIsShowAd = true;
            mainActivity.showAdDialog();
        }
    }

    public void dismissDialog(){
        if(!mIsShowAd){
            return;
        }
        MainActivity mainActivity = (MainActivity) ActivityStacks.getInstance().getMainActivity();
        if(mainActivity != null && !mainActivity.isFinishing()){
            mainActivity.dismissDialog();
        }
        mIsShowAd = false;
    }




    public List<PlayerBean> getPlayers(){
        return mPlayers;
    }
}

package com.cloudysea.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.adapter.BallScoreAdapter;
import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.CurrentGameInfo;
import com.cloudysea.bean.ImageSetBean;
import com.cloudysea.bean.ModifyPlayderBean;
import com.cloudysea.bean.ModifyPlayderNameBean;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.coinfig.ColorConfig;
import com.cloudysea.coinfig.ColorConfigManager;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.controller.ImageSetListener;
import com.cloudysea.controller.NameResetListener;
import com.cloudysea.controller.PlayerEditListener;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.TypefaceUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author roof 2019/9/7.
 * @email lyj@yhcs.com
 * @detail 装载水平分值的容器
 */
public class HorizontalRemoteScoreContainer extends LinearLayout {
    private final static int DEFAULT_SCORE_COUNT = 10; // 每行的分值个数
    private  float mCountMoreThanThree = 1F;
    private   int DEFAULT_IMG_WIDTH = (int) (100);
    private   int DEFAULT_IMG_WIDTH_WITH_TOTAL = (int) (120);
    private TextView mTvPlayerName;
    private boolean mHasReset = false;
    private boolean mNowStatus = false;
    private static final String BG_COVER = "#88000000";
    private MainActivity mActivity;
    private BowlerGameForTurn.DataBean.BowlerGamesBean mPlayerBean;
    private static final int ROUND_COUT = 6; // 一轮为6个
    private boolean mNewMode;
    private View mCoverView;
    private LinearLayout mLLTitle;
    private BallScoreAdapter mAdapter;
    private ColorConfig mColorConfig;
    private SparseArray<String> sparseArray = new SparseArray();
    private SparseArray<String> sparseArrayReplay = new SparseArray<>();

    public HorizontalRemoteScoreContainer(Context context) {
        super(context);
    }

    public HorizontalRemoteScoreContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    public void setMode(boolean isNewMode){
        mNewMode = isNewMode;
    }

    public void bindAdapter(BallScoreAdapter adapter){
        mAdapter = adapter;
    }

    private ColorConfig getColorConfig(){
        return mColorConfig;
    }

    public void initView(MainActivity activity, boolean hasTitle, int index, boolean moreThanThree, BowlerGameForTurn.DataBean.BowlerGamesBean playerBean, boolean changeHead){
        mActivity = activity;
        playerBean.position = index;
        mPlayerBean = playerBean;
        // 获取对应位置球员的颜色配置
        mColorConfig = ColorConfigManager.getInstance().getColorConfig(index);
        sparseArray.clear();
        if(mPlayerBean.getReplayInfos() != null && mPlayerBean.getReplayInfos().size() > 0){
            for(int i = 0; i < mPlayerBean.getReplayInfos().size();i++){
                BowlerGameForTurn.DataBean.BowlerGamesBean.ReplayInfosClass  replayInfosBean = (BowlerGameForTurn.DataBean.BowlerGamesBean.ReplayInfosClass) mPlayerBean.getReplayInfos().get(i);
                sparseArray.append(replayInfosBean.number,replayInfosBean.url);

            }
        }
        if(mPlayerBean.getBowlerReplayInfos() != null && mPlayerBean.getBowlerReplayInfos().size() > 0){
            for(int i = 0; i < mPlayerBean.getBowlerReplayInfos().size();i++){
                BowlerGameForTurn.DataBean.BowlerGamesBean.ReplayInfosClass  replayInfosBean = (BowlerGameForTurn.DataBean.BowlerGamesBean.ReplayInfosClass) mPlayerBean.getBowlerReplayInfos().get(i);
                sparseArrayReplay.append(replayInfosBean.number,replayInfosBean.url);

            }
        }
        if(getChildCount() == 0){

            linearLayout = addAllScoreContent();
            // 添加头像
            addPlayer(linearLayout,index,mColorConfig,hasTitle,playerBean,true,changeHead);


            // 添加积分部分
            addScoreContent(linearLayout,mColorConfig,hasTitle,playerBean,true);

            // 添加总分部分
            addScoreTotal(linearLayout,mColorConfig,hasTitle,playerBean,true);
            return;
        }
        // 重新设置头像
    //    dispatchCoverIfCovert();
        addPlayer(linearLayout,index,mColorConfig,false,playerBean,false,changeHead);
        addScoreContent(linearLayout,mColorConfig,false,playerBean,false);
        addScoreTotal(linearLayout,mColorConfig,false,playerBean,false);
    }
    private LinearLayout linearLayout;

    private LinearLayout addAllScoreContent(){
        RelativeLayout relativeLayout = (RelativeLayout) View.inflate(getContext(),R.layout.view_cover_for_score,null);
        LinearLayout linearLayout = (LinearLayout) relativeLayout.findViewById(R.id.ll_cover_for_score_container);
        mCoverView = relativeLayout.findViewById(R.id.view_cover_for_score_cover);
        mLLTitle = (LinearLayout) relativeLayout.findViewById(R.id.ll_cover_for_score_title);
        LayoutParams layoutParams =new LayoutParams(0,LayoutParams.MATCH_PARENT,11.5F);
        addView(relativeLayout,layoutParams);
        return linearLayout;
    }




    private TextView playerName;
    private ImageView player;
    private void addPlayer(LinearLayout linearLayout,int positon,ColorConfig colorConfig,boolean hasTitle,final BowlerGameForTurn.DataBean.BowlerGamesBean playerBean,boolean add,boolean changeHead){
            ViewGroup viewGroup = null;
            if(add){
                viewGroup = (ViewGroup) View.inflate(getContext().getApplicationContext(), R.layout.item_ball_player,null);
            }else{
                viewGroup = (ViewGroup) linearLayout.getChildAt(0);
            }
            player = (ImageView) viewGroup.findViewById(R.id.iv_player_avatar);
            playerName = (TextView) viewGroup.findViewById(R.id.tv_player_name);
            try {
                    // 缓存md5，避免重复刷新头像
                    if(playerBean.headPortraitInfo != null && playerBean.headPortraitInfo.fileUrl != null){
                        Picasso.with(BowlingApplication.getContext()).
                                load(playerBean.headPortraitInfo.fileUrl).resize(200,200).centerCrop().into(player);

                    }else{
                        playerName.setBackgroundResource(getContext().getResources().getIdentifier("bg_shape_for_player_name" + "_" + (positon  % ROUND_COUT + 1),"drawable",getContext().getPackageName()));
                    }

                playerName.setTypeface(TypefaceUtil.getStyleOneInstance());
                playerName.setText(playerBean.getName());
            }catch (Exception e){
                e.printStackTrace();
            }
            if(!BowlingUtils.isPad()){
                playerName.setTextSize(12);
            }
            if(add){
                linearLayout.addView(viewGroup, getTotalParams(hasTitle,1.0F));
            }
    }


    private void addScoreTotal(LinearLayout linearLayout,ColorConfig colorConfig,boolean hasTitle,BowlerGameForTurn.DataBean.BowlerGamesBean playerBean,boolean add){
        View  view = null;
        if(add){
            view = View.inflate(getContext().getApplicationContext(), R.layout.item_ball_score_total_no_title,null);
        }else{
            view = linearLayout.getChildAt(linearLayout.getChildCount() - 1);
        }

        TextView item = (TextView) view.findViewById(R.id.tv_score_total);
        if(!BowlingUtils.isPad()){
            item.setTextSize(20);
        }
        item.setTextColor(Color.parseColor(colorConfig.scoreTotalColor));
        item.setTypeface(TypefaceUtil.getStyleOneInstance());
        item.setBackgroundColor(Color.parseColor(colorConfig.thirdBg));
        if(playerBean.getScore() != null){
            if(playerBean != null){
                item.setText(String.format(Locale.getDefault(),"%d",playerBean.getScore().getTotalScore()));
            }else{
                item.setText("");
            }
        }
        if(add){
            linearLayout.addView(view, getLayoutScoreParams(hasTitle,1.5F));
        }
    }


    private void showVideoDialog(final int integer){
        MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
        if(mainActivity != null){
            if(sparseArray.get(integer) != null || sparseArrayReplay.get(integer) != null){
                mainActivity.showVideoDialog(sparseArray.get(integer),sparseArrayReplay.get(integer));
            }
        }
    }

    private void showVideoIcon(int integer,TextView textView){
        if(sparseArray.get(integer) != null){
            Drawable top = getResources().getDrawable(R.drawable.btn_play);
            top.setBounds(0,0, (int) (10 * DeviceUtils.getDestiny()), (int) (10 * DeviceUtils.getDestiny()));
            textView.setCompoundDrawables(null,top,null,null);
        }else if(sparseArrayReplay.get(integer) != null){
            Drawable top = getResources().getDrawable(R.drawable.btn_play);
            top.setBounds(0,0, (int) (10 * DeviceUtils.getDestiny()), (int) (10 * DeviceUtils.getDestiny()));
            textView.setCompoundDrawables(null,top,null,null);
        }
        else{
            textView.setCompoundDrawables(null,null,null,null);
        }
    }



    private void addScoreContent(LinearLayout linearLayout,ColorConfig colorConfig,boolean hasTitle,BowlerGameForTurn.DataBean.BowlerGamesBean playerBean,boolean add){
            for(int i = 0;i < DEFAULT_SCORE_COUNT;i++){
                final int z = i;
                char[] dataFirstPoint = null;
                char[] dataSecondPoint= null;
                int scoreFirstPoint = 0;
                int scoreSceondPoint = 0;




                // 第一次机会
                if(playerBean.getScore() != null && playerBean.getScore().getScores() != null){
                    if(playerBean.getScore().getScores().size() > (i * 2) && playerBean.getScore().getScores().get(i * 2)!= null && playerBean.getScore().getScores().get(i * 2) != 0){
                        dataFirstPoint = getRealScore(playerBean.getScore().getScores().get(i * 2));
                        scoreFirstPoint = 10;
                        for(int k= 2; k < dataFirstPoint.length - 1;k++) {
                            int scoreChild = dataFirstPoint[k];
                            if (scoreChild == 49) {
                                scoreFirstPoint -= 1;
                            }
                        }

                    }
                    // 第二次机会
                    if(playerBean.getScore().getScores().size() > (i * 2 + 1) && playerBean.getScore().getScores().get(i * 2 + 1) != null && playerBean.getScore().getScores().get(i * 2 + 1) != 0){

                        dataSecondPoint = getRealScore(playerBean.getScore().getScores().get(i * 2 + 1));
                        scoreSceondPoint = 10;
                        for(int k= 2; k < dataSecondPoint.length - 1;k++){
                            int scoreChild = dataSecondPoint[k];
                            if(scoreChild == 49){
                                scoreSceondPoint-=1;
                            }
                        }
                    }
                }





                View item = null;
                if(add){
                   item = View.inflate(getContext().getApplicationContext(),
                            R.layout.item_ball_socre_no_title,null);
                }else{
                    item = linearLayout.getChildAt(i+1);
                }

                View llBallScore = item.findViewById(R.id.ll_ball_score);

                TextView tvBallScoreItem1 = (TextView) item.findViewById(R.id.tv_ball_score_item_1);
                TextView tvBallScoreItem2 = (TextView) item.findViewById(R.id.tv_ball_score_item_2);
                TextView tvBallScoreItem3 = (TextView) item.findViewById(R.id.tv_ball_score_item_3);
                tvBallScoreItem2.setBackgroundColor(Color.parseColor(colorConfig.secondBallBg));
                showVideoIcon(i * 2,tvBallScoreItem1);
                showVideoIcon(i * 2 + 1,tvBallScoreItem2);
                if(add){
                    tvBallScoreItem2.setTypeface(TypefaceUtil.getStyleOneInstance());
                    tvBallScoreItem1.setTypeface(TypefaceUtil.getStyleOneInstance());
                    tvBallScoreItem3.setTypeface(TypefaceUtil.getStyleOneInstance());
                    if(!BowlingUtils.isPad()){
                        tvBallScoreItem1.setTextSize(20);
                        tvBallScoreItem2.setTextSize(20);
                        tvBallScoreItem3.setTextSize(20);
                    }
                    tvBallScoreItem1.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int integer = z * 2;
                            showVideoDialog(integer);
                            return false;
                        }
                    });
                    tvBallScoreItem2.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int integer = z * 2 + 1;
                            showVideoDialog(integer);
                            return false;
                        }
                    });
                }
                llBallScore.setBackgroundColor(Color.parseColor(colorConfig.firstBallBg));
                tvBallScoreItem3.setBackgroundColor(Color.parseColor(colorConfig.thirdBg));
                tvBallScoreItem3.setTextColor(Color.parseColor(colorConfig.scoreColor));
                if(add){
                    linearLayout.addView(item,getLayoutScoreParams(hasTitle,!mNewMode && i ==9  ? 1.5F : 1.0F));
                    item.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            BowlingChangeScoreDialog changeScoreDialog = new BowlingChangeScoreDialog(getContext());
                            changeScoreDialog.show();
                            return false;
                        }
                    });
                }
                //赋值
                setScoreTextForChild(tvBallScoreItem1,dataFirstPoint,scoreFirstPoint,1,0,false);
                setScoreTextForChild(tvBallScoreItem2,dataSecondPoint,scoreSceondPoint,2,scoreFirstPoint,dataFirstPoint != null && dataFirstPoint.length > 1 &&  dataFirstPoint[1] == '1');
                // 特殊处理，旧模式下的第10轮
                if(!mNewMode && i == 9){
                    final TextView textView = (TextView) item.findViewById(R.id.tv_ball_score_item_4);
                    if(!BowlingUtils.isPad()){
                        textView.setTextSize(20);
                    }
                    textView.setVisibility(View.VISIBLE);
                    showVideoIcon(z * 2 + 2,textView);
                    if(add){
                        textView.setOnLongClickListener(new OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                showVideoDialog(z * 2 + 2);
                                return false;
                            }
                        });
                        textView.setTypeface(TypefaceUtil.getStyleOneInstance());
                    }
                    View view = item.findViewById(R.id.view_middle_middle_line_right);
                    view.setVisibility(View.VISIBLE);

                    char[] dataThirdPoint = null;
                    int scoreThirdPoint = 0;
                    // 第三次机会
                    if(playerBean.getScore() != null){
                        if(playerBean.getScore().getScores().get(i * 2 + 2) != null && playerBean.getScore().getScores().get(i * 2 + 2) != 0){

                            dataThirdPoint = getRealScore(playerBean.getScore().getScores().get(i * 2 + 2));
                            scoreThirdPoint = 10;
                            for(int k= 2; k < dataThirdPoint.length - 1;k++){
                                int scoreChild = dataThirdPoint[k];
                                if(scoreChild == 49){
                                    scoreThirdPoint-=1;
                                }
                            }
                        }if(playerBean.getScore().getScores().get(i * 2 + 2) != null && playerBean.getScore().getScores().get(i * 2 + 2) != 0){

                            dataThirdPoint = getRealScore(playerBean.getScore().getScores().get(i * 2 + 2));
                            scoreThirdPoint = 10;
                            for(int k= 2; k < dataThirdPoint.length - 1;k++){
                                int scoreChild = dataThirdPoint[k];
                                if(scoreChild == 49){
                                    scoreThirdPoint-=1;
                                }
                            }
                        }
                    }
                    textView.setBackgroundColor(Color.parseColor(colorConfig.secondBallBg));
                    setScoreTextForChild(textView,dataThirdPoint,scoreThirdPoint,3,scoreSceondPoint,dataSecondPoint != null && dataSecondPoint.length > 1 &&  dataSecondPoint[1] == '1');
                }else if(mNewMode && i == 9){
                    TextView textView = (TextView) item.findViewById(R.id.tv_ball_score_item_4);
                    if(!BowlingUtils.isPad()){
                        textView.setTextSize(20);
                    }
                    textView.setVisibility(View.GONE);
                    View view = item.findViewById(R.id.view_middle_middle_line_right);
                    view.setVisibility(View.GONE);

                }
                if(playerBean.getScore() != null){
                    if(playerBean.getScore().getRoundTotalScores() != null && i < playerBean.getScore().getRoundTotalScores().size()){
                        setScoreTextForRound(tvBallScoreItem3,playerBean.getScore().getRoundTotalScores().get(i));
                    }else{
                        tvBallScoreItem3.setText("");
                    }
                }
                if(add){
                    ViewGroup viewGroup = (ViewGroup) item.findViewById(R.id.ll_score_container);
                    viewGroup.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            return false;
                        }
                    });
                }
            }
    }


    // 球员编辑
    private void playerEdit(PlayerBean playerBean){
        BowlingEditPlayerDialog dialog = new BowlingEditPlayerDialog(getContext());
        dialog.setPlayBean(playerBean);
        dialog.show();
    }

    private void setScoreTextForRound(TextView scoreItem,Integer scorePoint){
        if(scorePoint != null){
            scoreItem.setText(String.format(Locale.getDefault(),"%d",scorePoint));
        }else{
            scoreItem.setText("");
        }
    }



    private void setScoreTextForChild(TextView scoreItem, char[] dataPoint, int scorePoint, int col, int preScore,boolean preisF){
        if(dataPoint != null){
            if(col == 1){
                if(dataPoint[1] == '1'){
                    scoreItem.setText("F");
                }else
                if(scorePoint == 10){
                      scoreItem.setText("X");
                }else{
                    scoreItem.setText(String.format(Locale.getDefault(),"%d",scorePoint));
                }
                // 修改
                if(dataPoint[0] == '1'){
                    scoreItem.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                }else{
                    scoreItem.getPaint().setFlags(0);
                }
            }else if(col == 2){
                if(dataPoint[1] == '1'){
                    scoreItem.setText("F");
                }else
                if(preScore == 10 && scorePoint == 10){
                    scoreItem.setText("X");
                }else if(preScore != 10 && scorePoint == 10){
                    scoreItem.setText("/");
                }else if(preScore == 10 && scorePoint != 10)
                {
                    scoreItem.setText(String.format(Locale.getDefault(),"%d",scorePoint));
                }
                else{
                    if(preisF){
                        scoreItem.setText(String.format(Locale.getDefault(),"%d",scorePoint));
                    }else {
                        scoreItem.setText(String.format(Locale.getDefault(),"%d",scorePoint - preScore > 0 ? scorePoint - preScore : 0));
                    }
                }
                // 修改
                if(dataPoint[0] == '1'){
                    scoreItem.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                }else{
                    scoreItem.getPaint().setFlags(0);
                }
            }else if(col == 3){
                if(dataPoint[1] == '1'){
                    scoreItem.setText("F");
                }else
                if(preScore == 10 && scorePoint == 10){
                    scoreItem.setText("X");
                }else if(preScore != 10 && scorePoint == 10){
                    scoreItem.setText("/");
                }else if(preScore == 10 && scorePoint != 10)
                {
                    scoreItem.setText(String.format(Locale.getDefault(),"%d",scorePoint));
                }
                else{
                    if(preisF){
                        scoreItem.setText(String.format(Locale.getDefault(),"%d",scorePoint));
                    }else {
                        scoreItem.setText(String.format(Locale.getDefault(),"%d",scorePoint - preScore > 0 ? scorePoint - preScore : 0));
                    }
                }
                // 修改
                if(dataPoint[0] == '1'){
                    scoreItem.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                }else{
                    scoreItem.getPaint().setFlags(0);
                }
            }
        }else{
            scoreItem.setText("");
        }
    }



    private char[] getRealScore(int score){
            String scores = Integer.toBinaryString(score);
            StringBuilder sb = new StringBuilder(scores);
            for(int i = scores.length(); i < 13;i++){
                sb.insert(0,'0');
            }
            char[] datas = sb.toString().toCharArray();
            return datas;
    }



    private LayoutParams getTotalParams(boolean hasTitle,float weight){
        LayoutParams params = new LayoutParams(0, (int) (DEFAULT_IMG_WIDTH * BowlingUtils.Gobal_SIZE_SCORE_RADIO *  DeviceUtils.getDestiny()),weight);
        if(hasTitle){
            params.topMargin = (int) (DeviceUtils.getDestiny() * 20);
        }
        return params;
    }

    private LayoutParams getLayoutScoreParams(boolean hasTitle,float weight){
        return new LayoutParams(0, (int) ((hasTitle ? 20 +DEFAULT_IMG_WIDTH * BowlingUtils.Gobal_SIZE_SCORE_RADIO  :DEFAULT_IMG_WIDTH * BowlingUtils.Gobal_SIZE_SCORE_RADIO) * DeviceUtils.getDestiny()),weight);
    }
}

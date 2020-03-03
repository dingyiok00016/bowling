package com.cloudysea.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudysea.R;
import com.cloudysea.controller.ActivityStacks;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.adapter.BallScoreAdapter;
import com.cloudysea.bean.ImageSetBean;
import com.cloudysea.bean.ModifyPlayderBean;
import com.cloudysea.bean.ModifyPlayderNameBean;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.coinfig.ColorConfig;
import com.cloudysea.coinfig.ColorConfigManager;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.controller.ImageSetListener;
import com.cloudysea.controller.NameResetListener;
import com.cloudysea.controller.PlayerEditListener;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.utils.BottomPersonInfoHelper;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.UUID;

import javax.microedition.khronos.opengles.GL;

/**
 * @author roof 2019/9/7.
 * @email lyj@yhcs.com
 * @detail 装载水平分值的容器
 */
public class HorizontalScoreContainer extends LinearLayout {
    private final static int DEFAULT_SCORE_COUNT = 10; // 每行的分值个数
    private  float mCountMoreThanThree = 1F;
    private   int DEFAULT_IMG_WIDTH = (int) (100);
    private   int DEFAULT_IMG_WIDTH_WITH_TOTAL = (int) (120);
    private TextView mTvPlayerName;
    private boolean mHasReset = false;
    private boolean mNowStatus = false;
    private static final String BG_COVER = "#88000000";
    private MainActivity mActivity;
    private PlayerBean mPlayerBean;
    private static final int ROUND_COUT = 6; // 一轮为6个
    private boolean mNewMode;
    private View mCoverView;
    private LinearLayout mLLTitle;
    private BallScoreAdapter mAdapter;
    private ColorConfig mColorConfig;

    public HorizontalScoreContainer(Context context) {
        super(context);
    }

    public HorizontalScoreContainer(Context context, AttributeSet attrs) {
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

    public void initView(MainActivity activity, boolean hasTitle, int index, boolean moreThanThree, PlayerBean playerBean,boolean changeHead){
        mActivity = activity;
        playerBean.position = index;
        mPlayerBean = playerBean;
        mPlayerBean.isNew = mNewMode;
        // 获取对应位置球员的颜色配置
        mColorConfig = ColorConfigManager.getInstance().getColorConfig(index);
        if(getChildCount() == 0){

            linearLayout = addAllScoreContent();
            // 添加头像
            addPlayer(linearLayout,index,mColorConfig,false,playerBean,true,changeHead);


            // 添加积分部分
            addScoreContent(linearLayout,mColorConfig,false,playerBean,true);

            // 添加总分部分
            addScoreTotal(linearLayout,mColorConfig,false,playerBean,true);

            // 添加交换道标志
            dispatchCoverIfCovert();
            return;
        }
        dispatchCoverIfCovert();
        addPlayer(linearLayout,index,mColorConfig,hasTitle,playerBean,false,changeHead);
        addScoreContent(linearLayout,mColorConfig,hasTitle,playerBean,false);
        addScoreTotal(linearLayout,mColorConfig,hasTitle,playerBean,false);
        // 添加交换道标志

    }
    private LinearLayout linearLayout;
    // 头像->箭头动画
    private ObjectAnimator mObjectAnimatorOne;
    private ObjectAnimator mObjectAnimatorTwo;


    private void startScoreAnimation(boolean isLeftLane){
        if(mObjectAnimatorOne == null){
            // 先1步获取imagebitmap 对象
            final Bitmap bitmap = ((ImageView) player).getDrawable() == null ? null : ((BitmapDrawable) ((ImageView) player).getDrawable()).getBitmap();
            mObjectAnimatorOne = ObjectAnimator.ofFloat(player,"alpha",1F,0F);
            mObjectAnimatorOne.setDuration(2000);
            mObjectAnimatorTwo = ObjectAnimator.ofFloat(player,"alpha",0F,1F);
            mObjectAnimatorTwo.setDuration(2000);
            mObjectAnimatorOne.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // 左道 右道
                    if(isLeftLane){
                        player.setImageResource(R.drawable.exchange_left);
                    }else{
                        player.setImageResource(R.drawable.exchange_right);
                    }
                    mObjectAnimatorTwo.start();

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mObjectAnimatorTwo.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    player.setImageBitmap(bitmap);
                    mObjectAnimatorOne.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        mObjectAnimatorOne.start();
        Log.d("HorizontalScore","执行动画");
    }




    private void stopAnimation(){
        if(mObjectAnimatorOne != null){
            mObjectAnimatorOne.cancel();
            mObjectAnimatorOne = null;
        }
        if(mObjectAnimatorTwo != null){
            mObjectAnimatorTwo.cancel();
            mObjectAnimatorTwo = null;
        }
    }

    // 处于交换道的球员
    public void dispatchExchangeStartEvent(boolean isLeftLane){
        Log.d("HorizontalScore","dispatchExchangeStartEvent");
        if(mObjectAnimatorOne == null || mObjectAnimatorTwo == null){
            Log.d("HorizontalScore","startScoreAnimation");
            startScoreAnimation(isLeftLane);
        }
    }

    public void dispatchExchangeEndEvent(){
        stopAnimation();
    }


    private LinearLayout addAllScoreContent(){
        RelativeLayout relativeLayout = (RelativeLayout) View.inflate(getContext(),R.layout.view_cover_for_score,null);
        LinearLayout linearLayout = (LinearLayout) relativeLayout.findViewById(R.id.ll_cover_for_score_container);
        mCoverView = relativeLayout.findViewById(R.id.view_cover_for_score_cover);
        mLLTitle = (LinearLayout) relativeLayout.findViewById(R.id.ll_cover_for_score_title);
        LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,11.5F);
        addView(relativeLayout,layoutParams);
        return linearLayout;
    }
    private boolean mIsCurrent;
    public void dispatchCoverEvent(){
        if(mIsCurrent){
            return;
        }
        if(mAdapter.getCurrentView() != mCoverView){
            mIsCurrent = true;
            if(mAdapter.getCurrentView() != null){
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mAdapter.getCurrentView().getLayoutParams();
                params.topMargin = (int) (DeviceUtils.getDestiny() * 2);
                params.bottomMargin = (int) (DeviceUtils.getDestiny() * 2);
                params.leftMargin = (int) (DeviceUtils.getDestiny() * 10);
                params.rightMargin = (int) (DeviceUtils.getDestiny() * 10);
                mAdapter.getCurrentView().setBackgroundResource(R.drawable.bg_shape_cover_normal);
            }
            if(mAdapter.getCurrentLiner() != null){
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mAdapter.getCurrentLiner().getLayoutParams();
                params.leftMargin = (int) (DeviceUtils.getDestiny() * 8);
                params.rightMargin = (int) (DeviceUtils.getDestiny() * 8);
                mAdapter.getCurrentLiner().setLayoutParams(params);
            }
            ColorConfig colorConfig = getColorConfig();
            GradientDrawable background = (GradientDrawable) getResources().getDrawable(R.drawable.bg_shape_cover_selector);
            background.setStroke((int) (DeviceUtils.getDestiny() * 2),Color.parseColor(colorConfig.scoreColor));
            mCoverView.setBackground(background);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCoverView.getLayoutParams();
            params.topMargin = 0;
            params.bottomMargin = 0;
            params.leftMargin = 0;
            params.rightMargin = 0;

            RelativeLayout.LayoutParams linearLayoutLayoutParams = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
            linearLayoutLayoutParams.leftMargin = 0;
            linearLayoutLayoutParams.rightMargin = 0;
            linearLayout.setLayoutParams(linearLayoutLayoutParams);
            mAdapter.setCurretView(mCoverView,linearLayout,mPlayerBean.position);
        }
    }

    // 当前球员
    public void dispatchCoverIfCovert(){
            if(!mPlayerBean.Id.equalsIgnoreCase(BowlingUtils.CURRENT_BOWLER_ID)){
                mIsCurrent = false;
                if(mAdapter.getCurrentView() != null){
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mAdapter.getCurrentView().getLayoutParams();
                    params.topMargin = (int) (DeviceUtils.getDestiny() * 2);
                    params.bottomMargin = (int) (DeviceUtils.getDestiny() * 2);
                    params.leftMargin = (int) (DeviceUtils.getDestiny() * 10);
                    params.rightMargin = (int) (DeviceUtils.getDestiny() * 10);
                    mAdapter.getCurrentView().setBackgroundResource(R.drawable.bg_shape_cover_normal);
                }
                if(mAdapter.getCurrentLiner() != null){
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mAdapter.getCurrentLiner().getLayoutParams();
                    params.leftMargin = (int) (DeviceUtils.getDestiny() * 8);
                    params.rightMargin = (int) (DeviceUtils.getDestiny() * 8);
                    mAdapter.getCurrentLiner().setLayoutParams(params);
                }
            }else{
                mIsCurrent = true;
                ColorConfig colorConfig = getColorConfig();
                GradientDrawable background = (GradientDrawable) getResources().getDrawable(R.drawable.bg_shape_cover_selector);
                background.setStroke((int) (DeviceUtils.getDestiny() * 2),Color.parseColor(colorConfig.scoreColor));
                mCoverView.setBackground(background);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCoverView.getLayoutParams();
                params.topMargin = 0;
                params.bottomMargin = 0;
                params.leftMargin = 0;
                params.rightMargin = 0;

                RelativeLayout.LayoutParams linearLayoutLayoutParams = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
                linearLayoutLayoutParams.leftMargin = 0;
                linearLayoutLayoutParams.rightMargin = 0;
                linearLayout.setLayoutParams(linearLayoutLayoutParams);
                BottomPersonInfoHelper.getInstance().setCurrentBowlerInfo();
            }
    }


    public void setOnNameResetListener(){
        if(mTvPlayerName != null){
            mTvPlayerName.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    BowlingManager.getInstance().resetListener.addListener(new NameResetListener() {

                        @Override
                        public void execute(String s) {
                            mTvPlayerName.setText(s);
                            // 同步上传报文
                            buildPlayerNameData(0,s);
                        }
                    });
                    NameReSetDialog dialog = new NameReSetDialog(getContext());
                    dialog.show();
                    return false;
                }
            });
        }
    }


    // 设置图片接口
    private  ImageSetListener listener = new ImageSetListener() {
        @Override
        public void execute(ImageSetBean imageSetBean) {
            if(mActivity != null && !mActivity.isFinishing()){
                String imagePath = mActivity.getRealImagePathFromUri(imageSetBean.uri);
                mActivity.showImage(getImageView(),imagePath);
                // 同步上传报文
           //     buildPlayerAvatarData(getPosition(),imagePath);
            }
        }
    };

    private void buildPlayerNameData(int position,String name){
        ModifyPlayderNameBean bean = new ModifyPlayderNameBean();
        bean.AuthCode = "120";
        bean.Id = UUID.randomUUID().toString();
        bean.LaneNumber = position + "";
        bean.Name = bean.getName();
        ModifyPlayderNameBean.ModifyPlayerNameData data = new ModifyPlayderNameBean.ModifyPlayerNameData();
        data.BowlerId = mPlayerBean.Id;
        data.Name = name;
        bean.Data = data;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AuthCode",bean.AuthCode);
            jsonObject.put("Id",bean.Id);
            jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER);
            jsonObject.put("Name",bean.Name);
            jsonObject.put("Type",bean.Type);
            JSONObject child = new JSONObject();
            child.put("Name",data.Name);
            child.put("BowlerId",data.BowlerId);
            jsonObject.put("Data",child);
            BowlingClient.getInstance().handleMsg(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 构建图片上传
    private void buildPlayerAvatarData(int position,String imagePath){
        ModifyPlayderBean bean = new ModifyPlayderBean();
        bean.AuthCode = "120";
        bean.Id = UUID.randomUUID().toString();
        bean.LaneNumber = position + "";
        bean.Name = bean.getName();
        ModifyPlayderBean.ModifyPlayerAvatarData data = new ModifyPlayderBean.ModifyPlayerAvatarData();
        data.BowlerId = "";
        data.HeadPortrait = imagePath;
    //    bean.Data = data;

        BowlingClient.getInstance().handleMsg(bean.toString());
    }


    private void addPlayerEditListener(PlayerBean playerBean,ImageView player,TextView playerName){
        BowlingManager.getInstance().playerEditListener.addListener(playerEditListener);
    }

    PlayerEditListener playerEditListener = new PlayerEditListener() {
        @Override
        public void execute(PlayerBean playerBean) {
            if(playerBean.position == mPlayerBean.position){
                playerName.setText(playerBean.BowlerName);
                if(playerBean.HeadPortrait != null){
                    Bitmap bitmap = BowlingUtils.stringToBitmap((String) playerBean.HeadPortrait);
                    player.setImageBitmap(bitmap);
                }
            }
        }
    };

    private TextView playerName;
    private ImageView player;
    private void addPlayer(LinearLayout linearLayout,int positon,ColorConfig colorConfig,boolean hasTitle,final PlayerBean playerBean,boolean add,boolean changeHead){
            ViewGroup viewGroup = null;
            if(add){
                viewGroup = (ViewGroup) View.inflate(getContext().getApplicationContext(), R.layout.item_ball_player,null);
            }else{
                viewGroup = (ViewGroup) linearLayout.getChildAt(0);
            }
            ImageView vipIcon = (ImageView) viewGroup.findViewById(R.id.iv_vip_icon);
            if(playerBean.IsMembership){
                vipIcon.setVisibility(View.VISIBLE);
            }else{
                vipIcon.setVisibility(View.GONE);
            }
            player = (ImageView) viewGroup.findViewById(R.id.iv_player_avatar);
            playerName = (TextView) viewGroup.findViewById(R.id.tv_player_name);
            if(!BowlingUtils.isPad()){
                playerName.setTextSize(12);
            }
            try {
                    if(changeHead){
                        if(playerBean.HeadPortrait != null){
                            Bitmap bitmap = BowlingUtils.stringToBitmap((String) playerBean.HeadPortrait);
                            BowlingUtils.addHead(playerBean.Id, (String) playerBean.HeadPortrait);
                            player.setImageBitmap(bitmap);
                        }else{
                            String reourcePath = "head" + (positon  % ROUND_COUT + 1);
                            player.setImageResource(getContext().getResources().getIdentifier(reourcePath,"drawable",getContext().getPackageName()));
                            BowlingUtils.addResource(playerBean.Id,reourcePath);
                        }
                    }else{
                        if(BowlingUtils.getHead(playerBean.Id) != null){
                            Bitmap bitmap = BowlingUtils.stringToBitmap(BowlingUtils.getHead(playerBean.Id));
                            player.setImageBitmap(bitmap);
                        }else{
                            String reourcePath = "head" + (positon  % ROUND_COUT + 1);
                            player.setImageResource(getContext().getResources().getIdentifier(reourcePath,"drawable",getContext().getPackageName()));
                            BowlingUtils.addResource(playerBean.Id,reourcePath);
                        }
                    }

                playerName.setBackgroundResource(getContext().getResources().getIdentifier("bg_shape_for_player_name" + "_" + (positon  % ROUND_COUT + 1),"drawable",getContext().getPackageName()));
                if(!TextUtils.isEmpty(playerBean.BowlerName)){
                    playerName.setText(playerBean.BowlerName);
                }else{
                    playerName.setText("Bowling" + (positon + 1));
                }
                playerName.setTypeface(TypefaceUtil.getStyleOneInstance());
            }catch (Exception e){
                e.printStackTrace();
            }
            viewGroup.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // 先判断是否有远程对战
                    MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
                    if(mainActivity != null){
                        if(mainActivity.hasRemoetOnlineId()){
                            return false;
                        }
                    }
                    if(BowlingUtils.getHead(playerBean.Id) != null){
                        playerBean.HeadPortrait = BowlingUtils.getHead(playerBean.Id);
                    }
                    playerEdit(playerBean);
                    return false;
                }
            });
            addPlayerEditListener(playerBean,player,playerName);
            if(add){
                linearLayout.addView(viewGroup, getTotalParams(hasTitle,1.0F));
            }
    }

    // 修改分数
    private void modifyScore(int index,PlayerBean playerBean){
        BowlingPasswordDialog passwordDialog = new BowlingPasswordDialog(getContext(),BowlingPasswordDialog.SOURCE_MODIFY_SCORE);
        playerBean.index = index;
        passwordDialog.setPlayerBean(playerBean);
        passwordDialog.show();
    }

    private void addScoreTotal(LinearLayout linearLayout,ColorConfig colorConfig,boolean hasTitle,PlayerBean playerBean,boolean add){
        View  view = null;
        if(add){
            view = View.inflate(getContext().getApplicationContext(), R.layout.item_ball_score_total_no_title,null);
        }else{
            view = linearLayout.getChildAt(linearLayout.getChildCount() - 1);
        }

        TextView item = (TextView) view.findViewById(R.id.tv_score_total);
        item.setTextColor(Color.parseColor(colorConfig.scoreTotalColor));
        item.setTypeface(TypefaceUtil.getStyleOneInstance());
        item.setBackgroundColor(Color.parseColor(colorConfig.thirdBg));
        if(!BowlingUtils.isPad()){
            item.setTextSize(20);
        }
        if(playerBean.TotalScore != null){
            item.setText(String.format(Locale.getDefault(),"%d",playerBean.TotalScore));
        }else{
            item.setText("");
        }
        if(add){
            linearLayout.addView(view, getLayoutScoreParams(hasTitle,1.5F));
        }
    }




    private void addScoreContent(LinearLayout linearLayout,ColorConfig colorConfig,boolean hasTitle,PlayerBean playerBean,boolean add){
            for(int i = 0;i < DEFAULT_SCORE_COUNT;i++){
                final int z = i;
                char[] dataFirstPoint = null;
                char[] dataSecondPoint= null;
                int scoreFirstPoint = 0;
                int scoreSceondPoint = 0;

                if(playerBean.Score != null){
                    // 第一次机会
                    if(playerBean.Score.size() > (i * 2) && playerBean.Score.get(i * 2)!= null && playerBean.Score.get(i * 2) != 0){
                        dataFirstPoint = getRealScore(playerBean.Score.get(i * 2));
                        scoreFirstPoint = 10;
                        for(int k= 2; k < dataFirstPoint.length - 1;k++) {
                            int scoreChild = dataFirstPoint[k];
                            if (scoreChild == 49) {
                                scoreFirstPoint -= 1;
                            }
                        }

                    }
                    // 第二次机会
                    if(playerBean.Score.size() > (i * 2 + 1) && playerBean.Score.get(i * 2 + 1) != null && playerBean.Score.get(i * 2 + 1) != 0){
                        dataSecondPoint = getRealScore(playerBean.Score.get(i * 2 + 1));
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
                if(add){
                    tvBallScoreItem2.setTypeface(TypefaceUtil.getStyleOneInstance());
                    tvBallScoreItem1.setTypeface(TypefaceUtil.getStyleOneInstance());
                    tvBallScoreItem3.setTypeface(TypefaceUtil.getStyleOneInstance());
                    if(!BowlingUtils.isPad()){
                        tvBallScoreItem1.setTextSize(20);
                        tvBallScoreItem2.setTextSize(20);
                        tvBallScoreItem3.setTextSize(20);
                    }
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
                setScoreTextForChild(tvBallScoreItem2,dataSecondPoint,scoreSceondPoint,2,scoreFirstPoint,dataFirstPoint != null && dataFirstPoint.length > 0 &&  dataFirstPoint[1] == '1');
                // 特殊处理，旧模式下的第10轮
                if(!mNewMode && i == 9){
                    TextView textView = (TextView) item.findViewById(R.id.tv_ball_score_item_4);
                    if(!BowlingUtils.isPad()){
                        textView.setTextSize(20);
                    }
                    textView.setVisibility(View.VISIBLE);
                    if(add){
                        textView.setTypeface(TypefaceUtil.getStyleOneInstance());
                    }
                    View view = item.findViewById(R.id.view_middle_middle_line_right);
                    view.setVisibility(View.VISIBLE);

                    char[] dataThirdPoint = null;
                    int scoreThirdPoint = 0;
                    // 第三次机会
                    if(playerBean.Score.get(i * 2 + 2) != null && playerBean.Score.get(i * 2 + 2) != 0){

                        dataThirdPoint = getRealScore(playerBean.Score.get(i * 2 + 2));
                        scoreThirdPoint = 10;
                        for(int k= 2; k < dataThirdPoint.length - 1;k++){
                            int scoreChild = dataThirdPoint[k];
                            if(scoreChild == 49){
                                scoreThirdPoint-=1;
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
                if(playerBean.RoundTotalScore != null && i < playerBean.RoundTotalScore.size()){
                    setScoreTextForRound(tvBallScoreItem3,playerBean.RoundTotalScore.get(i));
                }else{
                    tvBallScoreItem3.setText("");
                }
                if(add){
                    ViewGroup viewGroup = (ViewGroup) item.findViewById(R.id.ll_score_container);
                    viewGroup.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            modifyScore(z,mPlayerBean);
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
        MainActivity mainActivity = (MainActivity) BowlingUtils.getMainActivity();
        if(mainActivity != null){
            mainActivity.setEditPlayerDialog(dialog);
        }
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, (int) (DEFAULT_IMG_WIDTH * DeviceUtils.getDestiny() * BowlingUtils.Gobal_SIZE_SCORE_RADIO),weight);
        if(hasTitle){
            params.topMargin = (int) (DeviceUtils.getDestiny() * 20);
        }
        return params;
    }

    private LayoutParams getLayoutScoreParams(boolean hasTitle,float weight){
        return new LinearLayout.LayoutParams(0, (int) ((hasTitle ? 20 + (DEFAULT_IMG_WIDTH * BowlingUtils.Gobal_SIZE_SCORE_RADIO) :DEFAULT_IMG_WIDTH * BowlingUtils.Gobal_SIZE_SCORE_RADIO) * DeviceUtils.getDestiny()),weight);
    }
}

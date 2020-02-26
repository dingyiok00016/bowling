package com.cloudysea.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.adapter.ChangeScoreAdapter;
import com.cloudysea.bean.PlayerBean;

import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author roof 2019/9/21.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingBallScoreViewManager  {

    private RelativeLayout mRlScore;
    private int mCurrentScore;
    private EditText mEtextViewScore;
    private ImageView imageView;
    private ViewGroup mLlinearLayout;
    private ViewGroup mLLSubGroup;
    private ImageView imageView2;
    private TextView mTvLeft;
    public BowlingBallScoreViewManager(ViewGroup relativeLayout){
        RelativeLayout itemView = (RelativeLayout) relativeLayout.findViewById(R.id.rl_score_container);
        this.mEtextViewScore = (EditText) relativeLayout.findViewById(R.id.et_score);
        this.mRlScore = itemView;
        this.mLlinearLayout = (ViewGroup) relativeLayout.findViewById(R.id.ll_add_score_one);
        mLLSubGroup = (ViewGroup) relativeLayout.findViewById(R.id.ll_sub_score_one);
        imageView2 = (ImageView) relativeLayout.findViewById(R.id.iv_checked_icon_2);
        mTvLeft = (TextView) relativeLayout.findViewById(R.id.tv_checked_icon_text_left);
        if(mLLSubGroup != null){
            initView(relativeLayout);
        }else{
            initViewForAnimation(relativeLayout);
        }
    }

    private ImageView[] scores = new ImageView[10];
    private boolean mLocalSet = false;

    private void initViewForAnimation(View view){
        for(int i = 0; i< scores.length;i++) {
            scores[i] = (ImageView) mRlScore.findViewById(BowlingApplication.getContext().getResources().getIdentifier("iv_number_" + (i + 1), "id", BowlingApplication.getContext().getPackageName()));
        }
    }

    private void initView(View view){
        for(int i = 0; i< scores.length;i++){
            final int k = i;
            scores[i] = (ImageView) mRlScore.findViewById(BowlingApplication.getContext().getResources().getIdentifier("iv_number_" + (i +1),"id",BowlingApplication.getContext().getPackageName()));
            scores[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mPlayBean.HasScore.get(mPosition)){
                        return;
                    }
                    if(scores[k].getTag() == null){
                        mCurrentScore--;
                        if(mCurrentScore == 0){
                            mAdapter.setPlayerBean(mPlayBean,mIndex);
                        }
                        scores[k].setImageResource(R.drawable.icon_ball);
                        mLocalSet = true;
                        booleanList.set(k,true);
                        mEtextViewScore.setText(String.format(Locale.getDefault(),"%d",mCurrentScore));
                        scores[k].setTag(Boolean.TRUE);
                        mLocalSet = false;
                        mPlayBean.pppp.put(mPosition,booleanList);
                        mAdapter.setPlayerBean(mPlayBean,mIndex);
                    }else{
                        mCurrentScore++;
                        scores[k].setImageResource(0);
                        mLocalSet = true;
                        booleanList.set(k,false);
                        mPlayBean.pppp.put(mPosition,booleanList);
                        mAdapter.setPlayerBean(mPlayBean,mIndex);
                        mEtextViewScore.setText(String.format(Locale.getDefault(),"%d",mCurrentScore));
                        scores[k].setTag(null);
                        mLocalSet = false;
                    }
                }
            });
        }
        imageView = (ImageView) view.findViewById(R.id.iv_checked_icon_1);
        mEtextViewScore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!mPlayBean.HasScore.get(mPosition)){
                    return;
                }
                if(s.length() > 0 && !mLocalSet){
                    int number = Integer.parseInt(s.toString());
                    if(number <= 10){
                        setBallByScire(number);
                    }

                }
            }
        });
        // 处理激活一球的逻辑,直接调用edit设置进行处理-》afterTextChange逻辑
        this.mLlinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() == null){
                    imageView.setImageResource(R.drawable.icon_tick);
                    mLlinearLayout.setTag(Boolean.TRUE);
                    mPlayBean.nnnn.put(mPosition,true);
                    mPlayBean.HasScore.put(mPosition,true);
                    mAdapter.setPlayerBean(mPlayBean,mIndex);
                }else{
                //    mEtextViewScore.setText(0 + "");
                    imageView.setImageResource(0);
                    mPlayBean.HasScore.put(mPosition,false);
                    mPlayBean.pppp.put(mPosition,booleanList);
                    mLlinearLayout.setTag(null);
                    mPlayBean.nnnn.put(mPosition,false);
                    mAdapter.setPlayerBean(mPlayBean,mIndex);
                }
            }
        });
        this.mLLSubGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() == null){
                    mLLSubGroup.setTag(Boolean.TRUE);
                    imageView2.setImageResource(R.drawable.icon_tick);
                    mPlayBean.oooo.put(mPosition,true);
                    mAdapter.setPlayerBean(mPlayBean,mIndex);
                    mEtextViewScore.setText(0 + "");
                }else{
                    mLLSubGroup.setTag(null);
                    imageView2.setImageResource(0);
                    mPlayBean.oooo.put(mPosition,false);
                    mAdapter.setPlayerBean(mPlayBean,mIndex);
                }
            }
        });
    }
    private ChangeScoreAdapter mAdapter;
    private int mIndex;
    private int mPosition;

    public void setAdapter(ChangeScoreAdapter adapter,int index, PlayerBean playerBean){
        mPlayBean = playerBean;
        mIndex = index;
        mAdapter = adapter;
    }

    List<Boolean> booleanList = new ArrayList<>();
    List<Boolean> initList = new ArrayList<>();



    public void setBallScoreByCharArrays(List<Boolean> booleans){
        for(int i = 0; i <= 9;i++){
            if(!booleans.get(i)){
                LogcatFileManager.getInstance().writeLog("AnimationConfigHolder","显示球图");
                Log.d("AnimationConfigHolder","显示球图");
                scores[i].setImageResource(R.drawable.icon_ball);
            }else{

            }
        }
    }



    public void setBallScoreByCharArrays(char[] charArrays,int position){
            mPosition = position;
            booleanList.clear();
            if(position == 0){
                mTvLeft.setText(R.string.to_one_ball);
            }else if(position == 1){
                mTvLeft.setText(R.string.to_two_ball);
            }else{
                mTvLeft.setText(R.string.to_three_ball);
            }
            // 处理犯规逻辑
            if(charArrays[1]== '1'){
                mPlayBean.oooo.put(mPosition,true);
                mLLSubGroup.setTag(Boolean.TRUE);
                imageView2.setImageResource(R.drawable.icon_tick);
                mPlayBean.rrrr.put(mPosition,true);
            }else{
                mPlayBean.oooo.put(mPosition,false);
                mPlayBean.rrrr.put(mPosition,false);
            }
            for(int i = 11; i >= 2;i--){
                if(charArrays[i] == '1'){
                    booleanList.add(true);
                    initList.add(true);
                    scores[11-i].setImageResource(R.drawable.icon_ball);
                    scores[11-i].setTag(Boolean.TRUE);
                }else{
                    mCurrentScore++;
                    booleanList.add(false);
                    initList.add(false);
                }
            }
            // 处理激活逻辑
            if(charArrays[12] == '1'){
                imageView.setImageResource(R.drawable.icon_tick);
                mLlinearLayout.setTag(Boolean.TRUE);
                mPlayBean.mmmm.put(mPosition,true);
                mPlayBean.nnnn.put(mPosition,true);
                mPlayBean.HasScore.put(mPosition,true);
            }else{
                mPlayBean.mmmm.put(mPosition,false);
                mPlayBean.HasScore.put(mPosition,false);
                mPlayBean.nnnn.put(mPosition,false);
            }
            mPlayBean.qqqq.put(mPosition,initList);
            mPlayBean.pppp.put(mPosition,booleanList);
            mAdapter.setPlayerBean(mPlayBean,mIndex);
            mLocalSet = true;
            mEtextViewScore.setText(String.format(Locale.getDefault(),"%d",mCurrentScore));
            mLocalSet = false;
    }

    private PlayerBean mPlayBean;
    public void setBallByScire(int score){
        mCurrentScore = score;
        if(score >= 0){
            for(int i = 0; i < score;i++){
                // 模拟处理
                scores[i].setImageResource(0);
                booleanList.set(i,false);
                scores[i].setTag(null);
            }
            for(int k =score; k < scores.length;k++){
                scores[k].setImageResource(R.drawable.icon_ball);
                booleanList.set(k,true);
                scores[k].setTag(Boolean.TRUE);
            }
            mPlayBean.pppp.put(mPosition,booleanList);
            if(mAdapter != null){
                mAdapter.setPlayerBean(mPlayBean,mIndex);
            }
        }

    }
}

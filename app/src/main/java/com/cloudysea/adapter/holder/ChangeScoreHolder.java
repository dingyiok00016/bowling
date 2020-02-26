package com.cloudysea.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudysea.R;
import com.cloudysea.adapter.ChangeScoreAdapter;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.utils.BowlingBallScoreViewManager;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.TypefaceUtil;

/**
 * @author roof 2019/9/21.
 * @email lyj@yhcs.com
 * @detail
 */
public class ChangeScoreHolder extends RecyclerView.ViewHolder {
    private BowlingBallScoreViewManager mManager;
    private PlayerBean mPlayerBean;
    private ChangeScoreAdapter mAdatper;
    private int mIndex;

    public ChangeScoreHolder(View itemView) {
        super(itemView);
        mManager = new BowlingBallScoreViewManager((ViewGroup) itemView);
        initTypeface(itemView);
    }

    public void setAdapter(ChangeScoreAdapter changeScoreAdapter,PlayerBean playerBean,int index){
        mAdatper = changeScoreAdapter;
        mPlayerBean = playerBean;
        mIndex = index;
        mManager.setAdapter(mAdatper,mIndex,mPlayerBean);
    }

    private void initTypeface(View itemView){
        final EditText textViewScore = (EditText) itemView.findViewById(R.id.et_score);
        textViewScore.setTypeface(TypefaceUtil.getStyleOneInstance());
        TextView textViewCheckLeft = (TextView) itemView.findViewById(R.id.tv_checked_icon_text_left);
        TextView textViewCheckRight = (TextView) itemView.findViewById(R.id.tv_checked_icon_text_right);
        textViewCheckLeft.setTypeface(TypefaceUtil.getStyleOneInstance());
        textViewCheckRight.setTypeface(TypefaceUtil.getStyleOneInstance());
    }

    public void bindData(int position){
        // 根据index获取对应分数
        if(mIndex < mPlayerBean.Score.size()){
          int realPosition = 0;
          if(position == 0){
              realPosition = mIndex * 2;
          }else if(position == 1){
              realPosition = mIndex * 2 + 1;
          }else{
              realPosition = mIndex * 2 + 2;
          }
          Integer integer = mPlayerBean.Score.get(realPosition);
          if(integer !=  null){
              char[] datas = BowlingUtils.getRealScore(integer);
              mManager.setBallScoreByCharArrays(datas,position);
          }
        }
    }
}

package com.cloudysea.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cloudysea.R;
import com.cloudysea.ui.MainActivity;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.views.HorizontalScoreContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roof 2019/9/7.
 * @email lyj@yhcs.com
 * @detail
 */
public class BallScoreAdapter extends BaseAdapter {

    private MainActivity mContext;
    private List<PlayerBean> mDatas = new ArrayList<>();
    private boolean mChangeHead;
    private boolean mIsNewMode = true;
    private View mCurrentView;
    private LinearLayout mCurrentLiner;
    private int mCurrentPosition;
    public BallScoreAdapter(MainActivity context, List<PlayerBean> list){
        mContext = context;
        if(list != null){
            mDatas.addAll(list);
        }
    }

    public void setNewDatas(List<PlayerBean> list,boolean changeHead){
        mChangeHead = changeHead;
        mDatas.clear();
        mDatas.addAll(list);
    }

    public void setMode(boolean isNewMode){
        mIsNewMode = isNewMode;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HorizontalScoreContainer container;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.horizontal_score_view,null);
            container = (HorizontalScoreContainer) convertView;
            container.bindAdapter(this);
        }else{
            container = (HorizontalScoreContainer) convertView;
        }
        container.setMode(mIsNewMode);
        if(position  == 0){
            container.initView(mContext,true,position,mDatas.size() > 3,mDatas.get(position),mChangeHead);
        }else{
            container.initView(mContext,false,position,mDatas.size() > 3,mDatas.get(position),mChangeHead);
        }
        container.setOnNameResetListener();
        return convertView;
    }

    public View getCurrentView(){
        return mCurrentView;
    }

    public LinearLayout getCurrentLiner(){
        return mCurrentLiner;
    }

    public int getCurrentPosition(){
        return mCurrentPosition;
    }

    public void setCurretView(View view,LinearLayout linearLayout,int position){
        mCurrentView = view;
        mCurrentLiner = linearLayout;
        mCurrentPosition = position;
    }



}

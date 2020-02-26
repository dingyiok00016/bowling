package com.cloudysea.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.adapter.holder.ChannelSetHolder;
import com.cloudysea.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roof 2019/9/27.
 * @email lyj@yhcs.com
 * @detail
 */
public class ChannelSetAdapter extends RecyclerView.Adapter<ChannelSetHolder> {


    private ImageView mCurrentSelectView;
    // 默认6道
    private int mCount = 1;
    private int mSelectPostion;
    private String mArrays;
    private List<Integer> mDatas = new ArrayList<>();
    public void setHoldreCount(int count){
        mCount = count;
        notifyDataSetChanged();
    }

    public void setArrays(String arrays){
        mDatas.clear();
        if(arrays == null){
            return;
        }
        mArrays = arrays;
        try{
            String[] strings = mArrays.split(",");
            if(strings.length > 1){
                for(String str:strings){
                    mDatas.add(Integer.parseInt(str));
                }
            }else if(!TextUtils.isEmpty(mArrays)){
                mDatas.add(Integer.parseInt(arrays));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Integer> getArrays(){
        return mDatas;
    }

    public void setImageViewUnSelect(){
        if(mCurrentSelectView != null){
            mCurrentSelectView.setImageResource(0);
        }
    }

    public void setCurrentImageView(ImageView imageView,int position){
        mCurrentSelectView = imageView;
        mSelectPostion = position;
    }

    @Override
    public ChannelSetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(BowlingApplication.getContext()).inflate(R.layout.item_channel_set,null);
        ChannelSetHolder holder = new ChannelSetHolder(view);
        holder.bindAdaper(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChannelSetHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return mCount;
    }
}

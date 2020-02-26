package com.cloudysea.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.adapter.holder.CurrentPlayerHolder;
import com.cloudysea.bean.PlayerBean;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * @author roof 2019/9/18.
 * @email lyj@yhcs.com
 * @detail
 */
public class CurrentPlayerAdapter extends RecyclerView.Adapter<CurrentPlayerHolder> {
    private List<PlayerBean> mDatas = new ArrayList<>();
    private List<String> mRemoveIds = new ArrayList<>();

    public CurrentPlayerAdapter(List<PlayerBean> datas){
        if(datas != null){
            mDatas.clear();
            mDatas.addAll(datas);
        }
    }

    public void setPlayerList(List<PlayerBean> datas){
        if(datas != null){
            mDatas.clear();
            mDatas.addAll(datas);
        }
    }

    public List<PlayerBean> getCurrentArrays(){
        return mDatas;
    }

    public List<String> getRemovedId(){
        return mRemoveIds;
    }



    @Override
    public CurrentPlayerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(BowlingApplication.getContext()).inflate(R.layout.item_curren_player,parent,false);
        final CurrentPlayerHolder currentPlayerHolder = new CurrentPlayerHolder(item);
        currentPlayerHolder.setOnDeleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPlayerHolder.getPlayBean() != null){
                    mRemoveIds.add(currentPlayerHolder.getPlayBean().Id);
                    mDatas.remove(currentPlayerHolder.getPlayBean());
                    notifyDataSetChanged();
                }
            }
        });
        return currentPlayerHolder;
    }

    @Override
    public void onBindViewHolder(CurrentPlayerHolder holder, int position) {
        PlayerBean currentPlayerItem = mDatas.get(position);
        holder.bindData(currentPlayerItem);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
}

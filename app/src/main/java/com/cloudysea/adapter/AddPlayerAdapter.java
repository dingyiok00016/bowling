package com.cloudysea.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.adapter.holder.AddPlayerDefaultHolder;
import com.cloudysea.adapter.holder.AddPlayerHolder;
import com.cloudysea.bean.PlayerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roof 2019/9/18.
 * @email lyj@yhcs.com
 * @detail
 */
public class AddPlayerAdapter extends RecyclerView.Adapter {

    private List<PlayerBean> playerBeans = new ArrayList<>();
    
    private CurrentPlayerAdapter mBindAdapter;

    public void bindAdapter(CurrentPlayerAdapter bindAdapter){
        mBindAdapter = bindAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if(viewType == 1){
            View view = LayoutInflater.from(BowlingApplication.getContext()).inflate(R.layout.item_add_player_default,parent,false);
            final AddPlayerDefaultHolder addPlayerDefaultHolder = new AddPlayerDefaultHolder(view);
            addPlayerDefaultHolder.addAddListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlayerBean playerBean = new PlayerBean();
                    playerBean.BowlerName ="Bowler" + (getItemCount() + (mBindAdapter != null ? mBindAdapter.getItemCount() : 0));
                    playerBeans.add(playerBean);
                    notifyDataSetChanged();
                }
            });
            return addPlayerDefaultHolder;
        }else{
            View view = LayoutInflater.from(BowlingApplication.getContext()).inflate(R.layout.item_add_player,parent,false);
            final AddPlayerHolder addPlayerHolder = new AddPlayerHolder(view);
            addPlayerHolder.setAdapter(this);
            addPlayerHolder.addAddListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playerBeans.remove(addPlayerHolder.getPosition());
                    notifyDataSetChanged();
                }
            });
            return addPlayerHolder;
        }
    }

    public void setNewPlayer(int index,PlayerBean playerBean){
        playerBeans.set(index,playerBean);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof AddPlayerDefaultHolder){
            AddPlayerDefaultHolder addPlayerDefaultHolder = (AddPlayerDefaultHolder) holder;
        }else if(holder instanceof  AddPlayerHolder){
            AddPlayerHolder addPlayerHolder = (AddPlayerHolder) holder;
            addPlayerHolder.bindData(playerBeans.get(position),position);
        }
    }



    @Override
    public int getItemViewType(int position) {
        if(position == playerBeans.size()){
            return 1;
        }else{
            return 0;
        }
    }

    public List<PlayerBean> getAddPlayers(){
        return playerBeans;
    }


    @Override
    public int getItemCount() {
        return playerBeans.size() + 1;
    }
}

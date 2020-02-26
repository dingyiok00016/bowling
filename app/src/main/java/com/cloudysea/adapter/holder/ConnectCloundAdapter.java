package com.cloudysea.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.bean.PlayerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roof 2019/10/18.
 * @email lyj@yhcs.com
 * @detail 关联云会员
 */
public class ConnectCloundAdapter extends RecyclerView.Adapter<ConnectCloundHolder> {

    private View.OnClickListener mListener;
    private List<PlayerBean> playerBeans = new ArrayList<>();

    @Override
    public ConnectCloundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(BowlingApplication.getContext()).
                inflate(R.layout.item_vip_member,parent,false);
        ConnectCloundHolder holder = new ConnectCloundHolder(itemView);
        holder.setOnClickListener(mListener);
        return holder;
    }


    public void setOnConnectListner(View.OnClickListener listner){
        mListener = listner;
    }

    public void setPlayBeans(List<PlayerBean> playBeans){
        if(playBeans != null && playBeans.size() > 0){
            this.playerBeans.clear();
            for(int i = 0; i < playBeans.size();i++){
                if(playBeans.get(i).IsMembership){
                    this.playerBeans.add(playBeans.get(i));
                }
            }
            notifyDataSetChanged();
        }

    }


    @Override
    public void onBindViewHolder(ConnectCloundHolder holder, int position) {
        holder.bindData(playerBeans.get(position),position);
    }

    @Override
    public int getItemCount() {
        return playerBeans == null ? 0 : playerBeans.size();
    }
}

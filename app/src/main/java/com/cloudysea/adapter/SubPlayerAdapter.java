package com.cloudysea.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.adapter.holder.SubPlayerHolder;
import com.cloudysea.bean.PlayerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roof 2019/9/8.
 * @email lyj@yhcs.com
 * @detail
 */
public class SubPlayerAdapter extends BaseAdapter {

    private List<PlayerBean> mDatas;
    private List<PlayerBean> mSubPlayers = new ArrayList<>();
    public SubPlayerAdapter(List<PlayerBean> list){
        this.mDatas = list;
    }

    public List<PlayerBean> getSubPlayers(){
        return mSubPlayers;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SubPlayerHolder subPlayerHolder;
        if(convertView == null){
            convertView = View.inflate(BowlingApplication.getContext(), R.layout.item_sub_player,null);
            subPlayerHolder = new SubPlayerHolder(convertView);
            subPlayerHolder.setSubClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.remove(position);
                    mSubPlayers.add(mDatas.get(position));
                    notifyDataSetChanged();
                }
            });
            convertView.setTag(subPlayerHolder);
        }else{
            subPlayerHolder = (SubPlayerHolder) convertView.getTag();
        }
        subPlayerHolder.bind(position,mDatas.get(position));
        return convertView;
    }
}

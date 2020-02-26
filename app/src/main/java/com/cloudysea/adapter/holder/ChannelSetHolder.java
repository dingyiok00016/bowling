package com.cloudysea.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.adapter.ChannelSetAdapter;
import com.cloudysea.utils.SharedPreferencesUtils;
import com.cloudysea.utils.TypefaceUtil;

import java.util.Locale;

/**
 * @author roof 2019/9/27.
 * @email lyj@yhcs.com
 * @detail
 */
public class ChannelSetHolder extends RecyclerView.ViewHolder {
    private TextView textViewChannel;
    private ImageView imageViewChannel;
    private RelativeLayout rlViewChannel;
    private ChannelSetAdapter mAdapter;
    private LinearLayout llViewChannel;

    public ChannelSetHolder(View itemView) {
        super(itemView);
        llViewChannel = (LinearLayout) itemView.findViewById(R.id.ll_channel_set);
        textViewChannel = (TextView) itemView.findViewById(R.id.tv_checked_icon_text_left);
        imageViewChannel = (ImageView) itemView.findViewById(R.id.iv_checked_icon_1);
        rlViewChannel = (RelativeLayout) itemView.findViewById(R.id.rl_image_check);
        llViewChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageViewChannel.getTag() == null){
                    imageViewChannel.setImageResource(R.drawable.icon_tick);
                    imageViewChannel.setTag(Boolean.TRUE);
                    if(!mAdapter.getArrays().contains(getAdapterPosition())){
                        mAdapter.getArrays().add(getAdapterPosition());
                    }

                }else{
                    imageViewChannel.setImageResource(0);
                    imageViewChannel.setTag(null);
                    if(mAdapter.getArrays().contains(getAdapterPosition())){
                        mAdapter.getArrays().remove((Integer) getAdapterPosition());
                    }
                }
                buildNewChannel();
            }
        });
        rlViewChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llViewChannel.performClick();
            }
        });
        textViewChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llViewChannel.performClick();
            }
        });
        imageViewChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llViewChannel.performClick();
            }
        });
    }

    private void buildNewChannel(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < mAdapter.getArrays().size();i++){
            if(i != mAdapter.getArrays().size() - 1){
                sb.append(mAdapter.getArrays().get(i));
                sb.append(",");
            }else{
                sb.append(mAdapter.getArrays().get(i));
            }
        }
    }


    public void bindData(int position){
        textViewChannel.setText(String.format(Locale.getDefault(),"%d " + BowlingApplication.getContext().getResources().getString(R.string.channel),position + 1));
        textViewChannel.setTypeface(TypefaceUtil.getStyleOneInstance());
        if(mAdapter.getArrays().contains(position)){
            imageViewChannel.setImageResource(R.drawable.icon_tick);
            imageViewChannel.setTag(Boolean.TRUE);
        }else{
            imageViewChannel.setImageResource(0);
            imageViewChannel.setTag(null);
        }
    }

    public void bindAdaper(ChannelSetAdapter adapter){
        mAdapter = adapter;
    }

}

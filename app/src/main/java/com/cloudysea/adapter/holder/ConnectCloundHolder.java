package com.cloudysea.adapter.holder;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.CropSquareTransformation;

/**
 * @author roof 2019/10/18.
 * @email lyj@yhcs.com
 * @detail
 */
public class ConnectCloundHolder extends RecyclerView.ViewHolder {
    private Button mTvConnectVip;
    private View.OnClickListener mListener;
    private ImageView mIvAvatar;
    private TextView mTextName;
    private int mPosition;
    private PlayerBean mItem;


    public ConnectCloundHolder(View itemView) {
        super(itemView);
        mIvAvatar = (ImageView) itemView.findViewById(R.id.iv_vip_member_avatar);
        mTextName = (TextView) itemView.findViewById(R.id.tv_vip_member_player_name);
        mTvConnectVip = (Button) itemView.findViewById(R.id.btn_vip_member_connect);
        mTvConnectVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    v.setTag(mItem.Id);
                    mListener.onClick(v);
                }
            }
        });
    }

    public void setOnClickListener(View.OnClickListener l){
        mListener = l;
    }

    public void bindData(PlayerBean item,int position){
        mItem = item;
        mPosition = position;
        mTextName.setText(item.BowlerName);
        BowlingUtils.setImageBitmap(item,mIvAvatar);
    }
}

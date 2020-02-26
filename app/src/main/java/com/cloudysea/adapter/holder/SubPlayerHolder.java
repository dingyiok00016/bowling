package com.cloudysea.adapter.holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cloudysea.R;
import com.cloudysea.bean.PlayerBean;

/**
 * @author roof 2019/9/8.
 * @email lyj@yhcs.com
 * @detail
 */
public class SubPlayerHolder extends BaseHolder<PlayerBean> {
    private TextView mTvSubPlayer;
    private Button mBtnSubPlayer;
    private View.OnClickListener mOnClickListener;
    public SubPlayerHolder(View v) {
        super(v);
        this.mBtnSubPlayer = (Button) findViewById(R.id.btn_sub_player);
        this.mTvSubPlayer = (TextView) findViewById(R.id.tv_player_name);
        this.mBtnSubPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener != null){
                    mOnClickListener.onClick(v);
                }
            }
        });
    }

    public void setSubClickListener(View.OnClickListener listener){
        this.mOnClickListener = listener;
    }

    @Override
    public void bindData(int position,PlayerBean playerBean) {
        this.mTvSubPlayer.setText(playerBean.BowlerName);
    }
}

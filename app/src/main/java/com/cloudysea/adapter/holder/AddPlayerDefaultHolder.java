package com.cloudysea.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudysea.R;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.utils.TypefaceUtil;

/**
 * @author roof 2019/9/18.
 * @email lyj@yhcs.com
 * @detail
 */
public class AddPlayerDefaultHolder extends RecyclerView.ViewHolder {

    private ImageView mIvAddView;
    private View.OnClickListener mListener;

    public AddPlayerDefaultHolder(View itemView) {
        super(itemView);
        mIvAddView = (ImageView) itemView.findViewById(R.id.iv_delete_or_add_icon);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onClick(v);
                }
            }
        });

    }

    public void addAddListener(View.OnClickListener listener){
        mListener = listener;
    }



    public void bindData(PlayerBean playerBean){

    }
}

package com.cloudysea.adapter.holder;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.CropSquareTransformation;
import com.cloudysea.utils.TypefaceUtil;
import com.squareup.picasso.Picasso;

import java.util.function.BinaryOperator;

/**
 * @author roof 2019/9/18.
 * @email lyj@yhcs.com
 * @detail
 */
public class CurrentPlayerHolder extends RecyclerView.ViewHolder {

    private ImageView mIvDeleteIcon;
    private View.OnClickListener mDeleteListener;
    private PlayerBean mItem;
    private ImageView mIv_avatar;
    private TextView mTvName;
    private ImageView mIvVipIcon;

    public CurrentPlayerHolder(View itemView) {
        super(itemView);
        this.mIvDeleteIcon = (ImageView) itemView.findViewById(R.id.iv_delete_or_add_icon);
        this.mIvDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDeleteListener != null){
                    mDeleteListener.onClick(v);
                }
            }
        });
        this.mIv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        this.mTvName = (TextView) itemView.findViewById(R.id.tv_player_name);
        this.mTvName.setTypeface(TypefaceUtil.getStyleOneInstance());
        this.mIvVipIcon = (ImageView) itemView.findViewById(R.id.iv_current_player_vip_icon);
        if(!BowlingUtils.isPad()){
            this.mIv_avatar.getLayoutParams().width *= BowlingUtils.Glbal_SIZE_RADIO;
            this.mIv_avatar.getLayoutParams().height *= BowlingUtils.Glbal_SIZE_RADIO;
            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) this.mIv_avatar.getLayoutParams();
            param.leftMargin = (int) (10 * DeviceUtils.getDestiny());
            mTvName.setTextSize(16);
            this.mIvDeleteIcon.getLayoutParams().height *= BowlingUtils.Glbal_SIZE_RADIO;
            this.mIvDeleteIcon.getLayoutParams().width *= BowlingUtils.Glbal_SIZE_RADIO;
            this.itemView.getLayoutParams().width = (int) (this.itemView.getLayoutParams().width * BowlingUtils.Glbal_SIZE_RADIO + 10 * DeviceUtils.getDestiny());
        }
    }

    public PlayerBean getPlayBean(){
        return this.mItem;
    }

    public void setOnDeleteListener(View.OnClickListener listener){
        this.mDeleteListener = listener;
    }

    public void bindData(PlayerBean item){
        this.mItem = item;
        if(item.HeadPortrait != null){
            Bitmap bitmap = BowlingUtils.stringToBitmap((String) item.HeadPortrait);
            mIv_avatar.setImageBitmap(CropSquareTransformation.getRoundedCornerBitmap(bitmap, (int) (DeviceUtils.getDestiny() * 7.5)));
        }else{
            if(BowlingUtils.getHead(item.Id) != null){
                Bitmap bitmap = BowlingUtils.stringToBitmap(BowlingUtils.getHead(item.Id));
                mIv_avatar.setImageBitmap(CropSquareTransformation.getRoundedCornerBitmap(bitmap, (int) (DeviceUtils.getDestiny() * 7.5)));
            }else{
                try{
                    if(item.resourcePath != null){
                        String reourcePath = item.resourcePath;
                        mIv_avatar.setImageResource(BowlingApplication.getContext().getResources().getIdentifier(reourcePath,"drawable",BowlingApplication.getContext().getPackageName()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        this.mTvName.setText(item.BowlerName);
        if(item.IsMembership){
            mIvVipIcon.setVisibility(View.VISIBLE);
        }else{
            mIvVipIcon.setVisibility(View.GONE);
        }
    }
}

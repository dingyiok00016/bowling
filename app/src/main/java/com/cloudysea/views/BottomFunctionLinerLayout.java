package com.cloudysea.views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.bean.FunctionImageEnum;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.utils.BowlingUtils;

/**
 * @author roof 2019/9/15.
 * @email lyj@yhcs.com
 * @detail
 */
public class BottomFunctionLinerLayout extends LinearLayout {
    public BottomFunctionLinerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addView();
    }

    private void addView(){
        View left = View.inflate(getContext(), R.layout.layout_hide_function_bottom,null);
        addView(left,new LinearLayout.LayoutParams((int)(25.5 * DeviceUtils.getDestiny() * BowlingUtils.Glbal_SIZE_RADIO),(int)(70.5 * DeviceUtils.getDestiny() * BowlingUtils.Glbal_SIZE_RADIO)));
        for(int i = 0; i < 7;i++){
            View item = View.inflate(getContext(), R.layout.item_bottom_function,null);
            ImageView imageView = (ImageView) item.findViewById(R.id.iv_bottom_function_icon);
            TextView textView = (TextView) item.findViewById(R.id.tv_bottom_function_text);
            // 非pad一律进行硕放
            imageView.getLayoutParams().width *= BowlingUtils.Glbal_SIZE_RADIO;
           imageView.getLayoutParams().height *= BowlingUtils.Glbal_SIZE_RADIO;
            textView.getLayoutParams().width *= BowlingUtils.Glbal_SIZE_RADIO;
            textView.getLayoutParams().height *= BowlingUtils.Glbal_SIZE_RADIO;
            if(!BowlingUtils.isPad()){
                textView.setTextSize(14);
            }
            addView(item,new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,1F));
            if(!BowlingUtils.currentLanguageIsSimpleChinese(BowlingApplication.getContext())){
                if(BowlingUtils.isPad()){
                    textView.setTextSize(14);
                }else{
                    textView.setTextSize(12);
                }
            }
            switch (i){
                case 0:
                    imageView.setImageResource(FunctionImageEnum.REMOTE.getDrawable());
                    textView.setText(FunctionImageEnum.REMOTE.getTextName());
                    break;
                case 1:
                    imageView.setImageResource(FunctionImageEnum.CONNECTVIP.getDrawable());
                    textView.setText(FunctionImageEnum.CONNECTVIP.getTextName());
                    break;
                case 2:
                    imageView.setImageResource(FunctionImageEnum.ADDDELETEPLAYER.getDrawable());
                    textView.setText(FunctionImageEnum.ADDDELETEPLAYER.getTextName());
                    break;
                case 3:
                    imageView.setImageResource(FunctionImageEnum.MODIFYSCORE.getDrawable());
                    textView.setText(FunctionImageEnum.MODIFYSCORE.getTextName());
                    break;
                case 4:
                    imageView.setImageResource(FunctionImageEnum.EDITPLAYER.getDrawable());
                    textView.setText(FunctionImageEnum.EDITPLAYER.getTextName());
                    break;
                case 5:
                    imageView.setImageResource(FunctionImageEnum.RESETDIVICE.getDrawable());
                    textView.setText(FunctionImageEnum.RESETDIVICE.getTextName());
                    break;
                case 6:
                    imageView.setImageResource(FunctionImageEnum.SWITCH.getDrawable());
                    textView.setText(FunctionImageEnum.SWITCH.getTextName());
                    break;
            }
        }
    }


    public void setOnItemClickListener(final AdapterView.OnItemClickListener onClickListener){
        for(int i = 0; i < getChildCount();i++){
            final int position = i;
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(null,v,position,v.getId());
                }
            });
        }
    }

    
}

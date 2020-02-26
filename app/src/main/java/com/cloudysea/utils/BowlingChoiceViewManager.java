package com.cloudysea.utils;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudysea.R;

/**
 * @author roof 2019/10/19.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingChoiceViewManager {
    public static void init(ViewGroup viewGroup, final View.OnClickListener l,String next){
        final ImageView imageView = (ImageView) viewGroup.findViewById(R.id.iv_checked_icon_1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageView.getDrawable() == null){
                    imageView.setTag(Boolean.TRUE);
                    imageView.setImageResource(R.drawable.icon_tick);

                }else{
                    imageView.setTag(null);
                    imageView.setImageResource(0);
                }
                if(l != null){
                    l.onClick(imageView);
                }
            }
        });
        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.performClick();
            }
        });
        RelativeLayout relativeLayout = (RelativeLayout) viewGroup.findViewById(R.id.rl_image_check);
        TextView textView = (TextView) viewGroup.findViewById(R.id.tv_checked_icon_text_left);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.performClick();
            }
        });
        textView.setText(next);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.performClick();
             }
        });
    }


}

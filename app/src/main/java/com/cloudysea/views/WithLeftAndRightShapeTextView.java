package com.cloudysea.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.cloudysea.R;
import com.cloudysea.coinfig.DeviceUtils;
import com.cloudysea.utils.TypefaceUtil;

/**
 * @author roof 2019/9/20.
 * @email lyj@yhcs.com
 * @detail
 */
public class WithLeftAndRightShapeTextView extends android.support.v7.widget.AppCompatTextView {
    private static final float PADDING_DEFAULT = 10.5F;

    public WithLeftAndRightShapeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }



    private void initView(){
        Drawable left= getContext().getResources().getDrawable(R.drawable.bg_text_left_to_right);
        Drawable right= getContext().getResources().getDrawable(R.drawable.bg_text_right_to_left);
        left.setBounds(0,0,(int)DeviceUtils.getDestiny() * 33,(int)(DeviceUtils.getDestiny() * 1.5F));
        right.setBounds(0,0,(int)DeviceUtils.getDestiny() * 33,(int)(DeviceUtils.getDestiny() * 1.5F));
        setCompoundDrawables(left,null,right,null);
        setCompoundDrawablePadding((int) (PADDING_DEFAULT * DeviceUtils.getDestiny()));
        setTextColor(Color.WHITE);
        setTextSize(23);
        setTypeface(TypefaceUtil.getStyleOneInstance());
        setGravity(Gravity.CENTER_HORIZONTAL);
    }



}

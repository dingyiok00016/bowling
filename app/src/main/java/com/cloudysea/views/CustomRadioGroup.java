package com.cloudysea.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

/**
 * @author roof 2019/9/25.
 * @email lyj@yhcs.com
 * @detail
 */
public class CustomRadioGroup extends RadioGroup {
    public CustomRadioGroup(Context context) {
        super(context);
    }

    public CustomRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        super.setOnCheckedChangeListener(listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int lineWidth = 0;//用于记录宽度是否超出，超出时归零
        int totalLineHeight = 0;//用于记录已有内容的高度，用于放置下一行时使用
        int lineHeight = 0;//用于记录已有内容的高度，用于放置下一行时使用

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec, heightMeasureSpec);
            int factWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();//radioGroup中可用的宽度
            if ((lineWidth + child.getMeasuredWidth()+10) > factWidth) {
                lineHeight = totalLineHeight;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(child.getLayoutParams());
                params.setMargins( -lineWidth, lineHeight, 0, 0);
                child.setLayoutParams(params);
                totalLineHeight += child.getMeasuredHeight();
                lineWidth = 0;
                lineWidth += child.getMeasuredWidth();
            } else {
                lineWidth += child.getMeasuredWidth();
                int childBottom = (int) (child.getY() + child.getMeasuredHeight());
                if (totalLineHeight < childBottom) {
                    totalLineHeight = childBottom;
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(child.getLayoutParams());
                if(lineHeight == 0) {
                    params.setMargins(0, lineHeight, 0, 0);
                }else {
                    params.setMargins(10, lineHeight, 0, 0);
                    lineWidth+=10;
                }
                child.setLayoutParams(params);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}


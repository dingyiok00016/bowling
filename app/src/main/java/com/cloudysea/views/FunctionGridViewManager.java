package com.cloudysea.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

/**
 * @author roof 2019/10/6.
 * @email lyj@yhcs.com
 * @detail
 */
public class FunctionGridViewManager extends GridLayoutManager {
    private int mMaxHeight;
    public FunctionGridViewManager(Context context, int spanCount,int maxHeight) {
        super(context, spanCount);
        mMaxHeight = maxHeight;
    }

    @Override
    public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
        super.setMeasuredDimension(childrenBounds, wSpec, View.MeasureSpec.makeMeasureSpec(mMaxHeight, android.view.View.MeasureSpec.AT_MOST));
    }
}

package com.cloudysea.views;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author roof 2019/9/30.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingTabLayout extends TabLayout {
    private static final String TAG = "BowlingTabLayout";
    public BowlingTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG,"action_event" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }
}

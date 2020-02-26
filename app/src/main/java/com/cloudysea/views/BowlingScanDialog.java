package com.cloudysea.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * @author roof 2019/10/3.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingScanDialog extends BowlingCommonDialog {
    public BowlingScanDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getDialogStyle() {
        return SMALL_SCREEN_STYLE;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    public int getChildViewLayout() {
        return 0;
    }
}

package com.cloudysea.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.cloudysea.R;

import java.util.Locale;

/**
 * @author roof 2020-02-20.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingUpdateApkDialog extends BowlingCommonDialog {

    private static BowlingUpdateApkDialog mDilaog;

    private TextView mTvUpdate;
    private TextView mTvUpdateProgerss;

    private BowlingUpdateApkDialog(@NonNull Context context) {
        super(context);
    }


    public static BowlingUpdateApkDialog getInstance(Context context){
        if(mDilaog == null){
            mDilaog = new BowlingUpdateApkDialog(context);
        }
        return mDilaog;
    }

    public void setProgress(String progress){
        mTvUpdateProgerss.setText(String.format(Locale.getDefault(),"%s%%",progress));
    }

    @Override
    protected int getDialogStyle() {
        return SMALL_SCREEN_STYLE;
    }

    @Override
    protected void initView(View view) {
        mTvUpdate = (TextView) view.findViewById(R.id.tv_update_desc);
        mTvUpdateProgerss = (TextView) view.findViewById(R.id.tv_update_progress);
    }

    @Override
    public int getChildViewLayout() {
        return R.layout.dialog_update;
    }
}

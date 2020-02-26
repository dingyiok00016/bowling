package com.cloudysea.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.controller.BowlingManager;
import com.cloudysea.utils.ToastUtil;

/**
 * @author roof 2019/9/7.
 * @email lyj@yhcs.com
 * @detail
 */
public class NameReSetDialog extends Dialog implements View.OnClickListener {

    private EditText mEtNameReset;
    private Button mBtnNameReset;

    public NameReSetDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_reset_name);
        mEtNameReset = (EditText) findViewById(R.id.et_name_reset);
        mBtnNameReset = (Button) findViewById(R.id.btn_name_reset);
        initListener();
    }

    private void initListener(){
        mBtnNameReset.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_name_reset:
                if(mEtNameReset.getText() == null || TextUtils.isEmpty(mEtNameReset.getText().toString())){
                    ToastUtil.showText(getContext(),R.string.player_is_blank_2);
                    return;
                }
                String name = mEtNameReset.getText().toString();
                BowlingManager.getInstance().resetListener.executeListeners(name);
                ToastUtil.showText(getContext(),R.string.modify_player_suc);
                dismiss();
                break;
        }
    }

    @Override
    public void dismiss() {
        BowlingManager.getInstance().resetListener.clearListener();
        super.dismiss();
    }
}

package com.cloudysea.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.PluralsRes;
import android.view.View;
import android.widget.TextView;

import com.cloudysea.R;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * @author roof 2019/9/21.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingResetDeviceDialog extends BowlingCommonDialog {
    public BowlingResetDeviceDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getDialogStyle() {
        return SMALL_SCREEN_STYLE;
    }

    @Override
    protected void initView(View view) {
        initTypeface();
    }


    private void initTypeface(){
        TextView textViewTitle = (TextView) findViewById(R.id.tv_reset_device_title);
        TextView textViewContent = (TextView) findViewById(R.id.tv_reset_device_content);
        TextView textViewCancel = (TextView) findViewById(R.id.tv_reset_device_cacel);
        TextView textViewConfirm = (TextView) findViewById(R.id.tv_reset_device_confirm);
        textViewTitle.setTypeface(TypefaceUtil.getStyleOneInstance());
        textViewContent.setTypeface(TypefaceUtil.getStyleOneInstance());
        textViewCancel.setTypeface(TypefaceUtil.getStyleTwoInstance());
        textViewConfirm.setTypeface(TypefaceUtil.getStyleTwoInstance());
        textViewConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildResetInfo();
                dismiss();
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void buildResetInfo(){
        JSONObject jsonObject = new JSONObject();
        JSONObject Data = new JSONObject();
        try {
            jsonObject.put("Id",UUID.randomUUID().toString());
            jsonObject.put("Name","ResetDevice");
            jsonObject.put("Type",0);
            jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER);
            jsonObject.put("AuthCode","120");
            jsonObject.put("Data",Data);
            BowlingClient.getInstance().handleMsg(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getChildViewLayout() {
        return R.layout.dialog_reset_device;
    }
}

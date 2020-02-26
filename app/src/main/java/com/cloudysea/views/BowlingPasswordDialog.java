package com.cloudysea.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.bean.SwitchBean;
import com.cloudysea.net.BowlingClient;
import com.cloudysea.utils.BowlingUtils;
import com.cloudysea.utils.ToastUtil;
import com.cloudysea.utils.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * @author roof 2019/9/21.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingPasswordDialog extends BowlingCommonDialog {
    private int mSourceStyle;
    public static final int SOURCE_MODIFY_SCORE = 0x01;
    public static final int SOURCE_MODIFY_SETTING = 0x02;
    public static final int SOURCE_JOIN_GAME = 0x03;
    private PlayerBean mPlayerBean;
    private EditText editText;
    private View.OnClickListener mClickListener;
    public BowlingPasswordDialog(@NonNull Context context,int sourceStyle) {
        super(context);
        mSourceStyle = sourceStyle;
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        mClickListener = onClickListener;
    }

    public void setPlayerBean(PlayerBean playerBean){
        mPlayerBean = playerBean;
    }

    @Override
    protected int getDialogStyle() {
        return SMALL_SCREEN_STYLE;
    }

    @Override
    protected void initView(View view) {
        initTypeFace();
    }

    private void initTypeFace(){
        TextView textViewPwdTitle = (TextView) findViewById(R.id.tv_pwd_read_title);
        textViewPwdTitle.setTypeface(TypefaceUtil.getStyleOneInstance());
        editText = (EditText) findViewById(R.id.et_pwd_read_try);
        editText.setTypeface(TypefaceUtil.getStyleOneInstance());
        if(!BowlingUtils.currentLanguageIsSimpleChinese(BowlingApplication.getContext())){
            editText.setHint(BowlingUtils.getSpannableString(12,R.string.please_enter_pwd));
        }
        TextView button = (TextView) findViewById(R.id.btn_pwd_read_confirm);
        button.setTypeface(TypefaceUtil.getStyleTwoInstance());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSourceStyle == SOURCE_JOIN_GAME){
                    if(mClickListener != null){
                        v.setTag(editText.getText() == null ? "" : editText.getText().toString());
                        mClickListener.onClick(v);
                    }
                }
                buildPwdTryBean();
                dismiss();
            }
        });
    }

    private void buildPwdTryBean(){
        if(editText.getText() == null || TextUtils.isEmpty(editText.getText().toString())){
            ToastUtil.showText(BowlingApplication.getContext(),"密码不能为空");
            return;
        }
        if(mSourceStyle != SOURCE_JOIN_GAME){
            SwitchBean bean = new SwitchBean();
            bean.Id = UUID.randomUUID().toString();
            bean.AuthCode = "120";
            bean.Name = bean.getName();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("AuthCode",bean.AuthCode);
                jsonObject.put("Id",bean.Id);
                jsonObject.put("LaneNumber",BowlingUtils.Global_LANE_NUMBER);
                jsonObject.put("Name","VerifyPassword");
                jsonObject.put("Type",bean.Type);
                JSONObject child = new JSONObject();
                jsonObject.put("Data",child);
                if(mSourceStyle == SOURCE_MODIFY_SCORE){
                    child.put("Action","ModifyScore");
                }else{
                    child.put("Action","ModifyConfig");
                }
                child.put("Password",editText.getText().toString());
                BowlingUtils.addPwdEntry(bean.Id,mPlayerBean);
                BowlingUtils.lists.add(bean.Id);
                BowlingClient.getInstance().handleMsg(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getChildViewLayout() {
        return R.layout.dialog_pwd_read;
    }
}

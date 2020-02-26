package com.cloudysea.views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cloudysea.R;
import com.cloudysea.coinfig.DeviceUtils;

/**
 * @author roof 2019/9/17.
 * @email lyj@yhcs.com
 * @detail
 */
public abstract class BowlingCommonDialog extends Dialog {


    public final static int ALL_SCREEN_STYLE = 0x01;
    public final static int SMALL_SCREEN_STYLE = 0x02;
    public final static int VEDIO_SCREEN_STYLE = 0x03;
    public final static int REMOTE_CEATE_STYLE = 0x04;
    private View childView;
    private boolean superStyle;

    public BowlingCommonDialog(@NonNull Context context) {
        super(context,R.style.loading_style);
        initView();
    }

    protected abstract int getDialogStyle();

    protected abstract void initView(View view);

    private void initView(){
            ViewGroup viewGroup = (ViewGroup) View.inflate(getContext(), R.layout.dialog_bowling_common_layout,null);

            int childViewLayout = getChildViewLayout();
            if(childViewLayout > 0){
                childView = (View) View.inflate(getContext(), childViewLayout,null);
                initView(childView);
                viewGroup.addView(childView,1,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));
            }
            setContentView(viewGroup);
            superStyle = true;
            initSuperView();
            setCanceledOnTouchOutside(canTouchOutSide());
    }

    protected boolean canTouchOutSide(){
        return false;
    }



    @Override
    public View findViewById(int id) {
        if(childView != null && !superStyle){
            return childView.findViewById(id);
        }
        return super.findViewById(id);
    }

    private void initSuperView(){
        ImageView imageView = (ImageView) findViewById(R.id.iv_dialog_close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        Window dialogWindow = getWindow();
        dialogWindow.getDecorView().setPadding(0,0,0,0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        int style = getDialogStyle();
        if(style == ALL_SCREEN_STYLE){
            lp.width = (int) (DeviceUtils.getScreenWidth() - 85 * DeviceUtils.getDestiny());
            lp.height = (int) (DeviceUtils.getScreenHeight() - 80.5 * DeviceUtils.getDestiny());
        }else if(style == SMALL_SCREEN_STYLE){
            lp.width = (int) (517.5 * DeviceUtils.getDestiny());
            lp.height = (int) (300 * DeviceUtils.getDestiny());
        }else if(style == VEDIO_SCREEN_STYLE){
            lp.width = (int) (674 * DeviceUtils.getScreenWidth() / 960F);
            lp.height = (int) (418 * DeviceUtils.getScreenWidth() / 960F );
        }else if(style == REMOTE_CEATE_STYLE){
            lp.width = (int) (778 * DeviceUtils.getDestiny());
            lp.height = (int) (420 * DeviceUtils.getDestiny());
        }
        dialogWindow.setAttributes(lp);
    }

    protected void setLayoutManager(RecyclerView recyclerView){
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }



    public abstract int getChildViewLayout();


}

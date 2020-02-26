package com.cloudysea.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cloudysea.R;
import com.cloudysea.adapter.holder.ConnectCloundAdapter;
import com.cloudysea.bean.PlayerBean;

import java.util.List;

/**
 * @author roof 2019/10/18.
 * @email lyj@yhcs.com
 * @detail 关联云会员dialog
 */
public class BowlingConnectVipDialog extends BowlingCommonDialog {
    private static final int HORIZONTAL = 53;
    private BowlingQRcodeScanDialog dialog;
    private ConnectCloundAdapter mAdapter;


    public BowlingConnectVipDialog(@NonNull Context context) {
        super(context);
    }



    @Override
    protected int getDialogStyle() {
        return SMALL_SCREEN_STYLE;
    }

    @Override
    protected void initView(View view) {
        RecyclerView mRvConnectVip = (RecyclerView) findViewById(R.id.rv_connect_vip);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        mRvConnectVip.setLayoutManager(linearLayoutManager);
        mRvConnectVip.addItemDecoration(new SpacesItemDecoration(HORIZONTAL));
        mAdapter = new ConnectCloundAdapter();
        mAdapter.setOnConnectListner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BowlingMeVipDialog dialog = new BowlingMeVipDialog(getContext());
                dialog.setStyle(BowlingMeVipDialog.STYLE_CONNECT_VIP);
                dialog.setId((String) v.getTag());
                dialog.show();
            }
        });
        mRvConnectVip.setAdapter(mAdapter);
    }

    public void setPlayBeans(List<PlayerBean> playBeans){
        mAdapter.setPlayBeans(playBeans);
    }

    @Override
    public int getChildViewLayout() {
        return R.layout.dialog_connect_vip;
    }
}

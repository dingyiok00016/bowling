package com.cloudysea.views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.cloudysea.R;
import com.cloudysea.adapter.SubPlayerAdapter;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.controller.BowlingManager;

import java.util.List;

/**
 * @author roof 2019/9/8.
 * @email lyj@yhcs.com
 * @detail
 */
public class SubPlayerDialog extends Dialog {
    private SubPlayerAdapter mAdapter;
    private Button mBtnConfirm;
    public SubPlayerDialog(@NonNull Context context, List<PlayerBean> players) {
        super(context);
        setContentView(R.layout.dialog_sub_player);
        initView(players);
    }


    private void initView(List<PlayerBean> players){
        mAdapter = new SubPlayerAdapter(players);
        ListView listView = (ListView) findViewById(R.id.lv_sub_players);
        mBtnConfirm = (Button) findViewById(R.id.btn_sub_player);
        listView.setAdapter(mAdapter);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAdapter.getSubPlayers().size() > 0){
                    BowlingManager.getInstance().subPlayerListener.executeListeners(mAdapter.getSubPlayers());
                }
                BowlingManager.getInstance().subPlayerListener.clearListener();
                dismiss();
            }
        });
    }

    private void initLauageLayout(){

    }


}

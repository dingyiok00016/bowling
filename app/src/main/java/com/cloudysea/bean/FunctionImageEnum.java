package com.cloudysea.bean;

import com.cloudysea.BowlingApplication;
import com.cloudysea.R;

/**
 * @author roof 2019/10/28.
 * @email lyj@yhcs.com
 * @detail
 */
public enum FunctionImageEnum  {

    // 远程对战
    REMOTE(R.drawable.icon_battle,BowlingApplication.getContext().getResources().getString(R.string.text_remote_battler)),
    // 关联云会员
    CONNECTVIP(R.drawable.icon_vip,BowlingApplication.getContext().getResources().getString(R.string.text_connect_vip)),
    // 加减球员
    ADDDELETEPLAYER(R.drawable.icon_addordelete,BowlingApplication.getContext().getResources().getString(R.string.add_or_delete_player)),
    // 修改分数
    MODIFYSCORE(R.drawable.icon_changethescore,BowlingApplication.getContext().getResources().getString(R.string.modify_score)),
    // 编辑玩家
    EDITPLAYER(R.drawable.icon_playeredit,BowlingApplication.getContext().getResources().getString(R.string.player_edit)),
    // 重置设备
    RESETDIVICE(R.drawable.icon_resetdevice,BowlingApplication.getContext().getResources().getString(R.string.reset_device)),
    // 切换交换
    SWITCH(R.drawable.icon_switch,BowlingApplication.getContext().getResources().getString(R.string.switch_change));
    private final int resId;
    private final String textName;



     private FunctionImageEnum(int resid,String textName){
        this.resId = resid;
        this.textName = textName;
    }

    public int getDrawable(){
         return resId;
    }

    public String getTextName(){
         return textName;
    }
}

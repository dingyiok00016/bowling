package com.cloudysea.controller;

import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.BowlerGameSingLine;
import com.cloudysea.bean.CurrentBowlerInfo;
import com.cloudysea.bean.OnLineGameIdBean;
import com.cloudysea.bean.PlayerBean;
import com.cloudysea.bean.PlayingGame;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roof 2019/9/7.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlingManager {
    private static BowlingManager sBowlingManager;

    private BowlingManager(){

    }


    public static BowlingManager getInstance(){
        if(sBowlingManager == null){
            sBowlingManager = new BowlingManager();
        }
        return sBowlingManager;
    }

    public ListenerManager<NameResetListener> resetListener = new ListenerManager<NameResetListener>();

    public ListenerManager<SubPlayerListener> subPlayerListener = new ListenerManager<SubPlayerListener>();

    public ListenerManager<ImageSetListener> imageListener = new ListenerManager<>();

    public ListenerManager<AddOrDeleteListener> addOrDeleteListener = new ListenerManager<>();

    public ListenerManager<GetScoreListListener> getScoreListListener = new ListenerManager<>();

    public ListenerManager<BaseListener> changeChannelListener = new ListenerManager<>();

    public ListenerManager<PlayerEditListener> playerEditListener = new ListenerManager<>();

    public ListenerManager<BaseListener<PlayingGame>> gameForTurn = new ListenerManager<>();

    public ListenerManager<BaseListener<BowlerGameForTurn>> currentGame = new ListenerManager<>();

    public ListenerManager<BaseListener<BowlerGameSingLine>> singleGame = new ListenerManager<>();

    public ListenerManager<BaseListener<OnLineGameIdBean>> onLineId = new ListenerManager<>();
    public ListenerManager<BaseListener<Integer>> remoteGameNumbers = new ListenerManager<>();

    public ListenerManager<BaseListener<CurrentBowlerInfo>> currentBowlerInfo = new ListenerManager<>();

    public ListenerManager<SetIsExchangeMode> setCurrentMdoe = new ListenerManager<>();

    // 添加分数带来的动画或者瓶位图
    public ListenerManager<BaseListener>  addScoreThenController = new ListenerManager<>();
}

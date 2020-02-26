package com.cloudysea.ui;

import com.cloudysea.bean.BowlerGameForTurn;
import com.cloudysea.bean.BowlerGameSingLine;
import com.cloudysea.bean.GameBasicInfo;
import com.cloudysea.bean.PlayingGame;

/**
 * @author roof 2019/10/24.
 * @email lyj@yhcs.com
 * @detail
 */
public interface NetView {
    void getBasicGameInfo(PlayingGame gameBasicInfo);
    void getBowlerGameForTurn(BowlerGameForTurn bowlerGameForTurn);
    void getBowlerGame(BowlerGameSingLine singLine);
    void getBowlerBasicInfo(GameBasicInfo gameBasicInfo,String onLineId);

}

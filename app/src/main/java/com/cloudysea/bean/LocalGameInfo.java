package com.cloudysea.bean;

/**
 * @author roof 2019/11/2.
 * @email lyj@yhcs.com
 * @detail
 */
public class LocalGameInfo {

    /**
     * HasGame : true
     * EllipsedTurn : 0.5
     * IsPrePay : true
     * IsByTime : false
     * StartDateTime : 2019-11-02T19:59:16.023
     * EndValue : 3
     * GameName : Game
     * LaneInfo : Lane1
     */

    private boolean HasGame;
    private double EllipsedTurn;
    private boolean IsPrePay;
    private boolean IsByTime;
    private String StartDateTime;
    private String EndDateTime;
    private String EndValue;
    private String GameName;

    public String getEndDateTime() {
        return EndDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        EndDateTime = endDateTime;
    }

    private String LaneInfo;

    public boolean isHasGame() {
        return HasGame;
    }

    public void setHasGame(boolean HasGame) {
        this.HasGame = HasGame;
    }

    public double getEllipsedTurn() {
        return EllipsedTurn;
    }

    public void setEllipsedTurn(double EllipsedTurn) {
        this.EllipsedTurn = EllipsedTurn;
    }

    public boolean isIsPrePay() {
        return IsPrePay;
    }

    public void setIsPrePay(boolean IsPrePay) {
        this.IsPrePay = IsPrePay;
    }

    public boolean isIsByTime() {
        return IsByTime;
    }

    public void setIsByTime(boolean IsByTime) {
        this.IsByTime = IsByTime;
    }

    public String getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(String StartDateTime) {
        this.StartDateTime = StartDateTime;
    }

    public String getEndValue() {
        return EndValue;
    }

    public void setEndValue(String EndValue) {
        this.EndValue = EndValue;
    }

    public String getGameName() {
        return GameName;
    }

    public void setGameName(String GameName) {
        this.GameName = GameName;
    }

    public String getLaneInfo() {
        return LaneInfo;
    }

    public void setLaneInfo(String LaneInfo) {
        this.LaneInfo = LaneInfo;
    }
}

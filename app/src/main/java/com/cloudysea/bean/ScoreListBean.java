package com.cloudysea.bean;

import java.util.HashMap;
import java.util.List;

/**
 * @author roof 2019/9/22.
 * @email lyj@yhcs.com
 * @detail
 */
public class ScoreListBean extends PublicBean {
    public List<PlayerBean> Scores;
    public ScoreListBean Data;
    public boolean IsExchangeMode;
    public LocalGameInfo GameInfo;
    public String ScoreMode = "New";
    public boolean HasScore;
    public boolean IncludeHeadPortrait;


    @Override
    public String toString() {
        return "ScoreListBean{" +
                "Scores=" + Scores +
                ", Data=" + Data +
                ", ScoreMode='" + ScoreMode + '\'' +
                ", IncludeHeadPortrait='" + IncludeHeadPortrait + '\'' +
                ", Id='" + Id + '\'' +
                ", Name='" + Name + '\'' +
                ", Type='" + Type + '\'' +
                ", LaneNumber='" + LaneNumber + '\'' +
                ", AuthCode='" + AuthCode + '\'' +
                '}';
    }
}

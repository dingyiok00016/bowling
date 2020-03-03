package com.cloudysea.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author roof 2019/9/8.
 * @email lyj@yhcs.com
 * @detail
 */
public class PlayerBean {
    public int position;
    public String Id;
    public String BowlerName = "Bowler1";
    public List<Integer> Score;
    public List<Integer> TempSocre;
    public List<Integer> RoundTotalScore;
    public String CurrentTurnId;
    public Integer TotalScore;
    public boolean IsExchangeMode; // 是否交换道标志
    public Object HeadPortrait = "";
    public String LaneNumber = "1";
    public boolean IsMembership;
    public String MembershipNumber = "1";
    public boolean isNew = true;
    public HashMap<Integer,Boolean> HasScore = new HashMap<>();

    // 分值逻辑
    public HashMap<Integer,List<Boolean>> pppp = new HashMap<>();
    public HashMap<Integer,List<Boolean>> qqqq = new HashMap<>();
    // 犯规逻辑
    public HashMap<Integer,Boolean>  oooo = new HashMap();
    public HashMap<Integer,Boolean> rrrr = new HashMap<>();
    // 激活逻辑
    public HashMap<Integer,Boolean> mmmm = new HashMap<>();
    public HashMap<Integer,Boolean> nnnn = new HashMap<>();
    public boolean hasChange;
    public int index;
    public String resourcePath;
}

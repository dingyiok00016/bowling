package com.cloudysea.bean;

import java.util.List;

/**
 * @author roof 2020-02-16.
 * @email lyj@yhcs.com
 * @detail
 */
public class AddNewScore extends PublicBean {

    public Data Data;

    public static class Data{
        public int LaneNumber;
        public String BowlerId;
        public int RoundNumber;
        public int ScoreNumber;
        public String AnimationCategory;
        public List<Boolean> PinStates;
    }
}

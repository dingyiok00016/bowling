package com.cloudysea.bean;

import java.util.List;

/**
 * @author roof 2019/11/9.
 * @email lyj@yhcs.com
 * @detail 当前球员信息
 */
public class CurrentBowlerInfo extends PublicBean {

    /**
     * Data : {"HasBowler":true,"BowlerId":"18fd5eb5-24db-4f38-b063-1e1ca8a59b6c","TeamScore":30,"HDP":null}
     */

    private List<DataBean> Data;

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * HasBowler : true
         * BowlerId : 18fd5eb5-24db-4f38-b063-1e1ca8a59b6c
         * TeamScore : 30
         * HDP : null
         */

        private boolean HasBowler;
        private String BowlerId;
        public boolean IsLocalLane;

        public float getSpeed() {
            return Speed;
        }

        public void setSpeed(float speed) {
            Speed = speed;
        }

        private int TeamScore;
        private int HDP;
        private float Speed;
        private String BowlerName;

        public String getBowlerName() {
            return BowlerName;
        }

        public void setBowlerName(String bowlerName) {
            BowlerName = bowlerName;
        }

        public boolean isHasBowler() {
            return HasBowler;
        }

        public void setHasBowler(boolean HasBowler) {
            this.HasBowler = HasBowler;
        }

        public String getBowlerId() {
            return BowlerId;
        }

        public void setBowlerId(String BowlerId) {
            this.BowlerId = BowlerId;
        }

        public int getTeamScore() {
            return TeamScore;
        }

        public void setTeamScore(int TeamScore) {
            this.TeamScore = TeamScore;
        }

        public int getHDP() {
            return HDP;
        }

        public void setHDP(int HDP) {
            this.HDP = HDP;
        }
    }
}

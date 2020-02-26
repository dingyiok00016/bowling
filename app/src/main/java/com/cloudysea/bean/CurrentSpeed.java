package com.cloudysea.bean;

/**
 * @author roof 2019/11/10.
 * @email lyj@yhcs.com
 * @detail
 */
public class CurrentSpeed {


    private CurrentBowlerInfo.DataBean Data;

    public CurrentBowlerInfo.DataBean getData() {
        return Data;
    }

    public void setData(CurrentBowlerInfo.DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * HasBowler : true
         * BowlerId : 18fd5eb5-24db-4f38-b063-1e1ca8a59b6c
         * TeamScore : 30
         * HDP : null
         */

        private float Speed;

        public float getSpeed() {
            return Speed;
        }

        public void setSpeed(float speed) {
            Speed = speed;
        }
    }

}

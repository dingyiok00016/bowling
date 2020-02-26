package com.cloudysea.bean;

import java.util.List;

/**
 * @author roof 2019/10/26.
 * @email lyj@yhcs.com
 * @detail
 */
public class PlayingGame {

    /**
     * data : [{"currentTurnNumber":0,"turnCount":3,"startTime":"2019-10-26 10:47:06","createMerchantName":"李内部测试","state":1,"configurationObj":{"allowedDeviceCategory":"All","allowedLaneCategory":"All","scoreCategory":"New","entryPassword":"123","needAllBowlerDoneToNextTurn":true,"turnCount":3}}]
     * errorCode : 0
     * isSuccessful : true
     * errorMessage :
     */

    private int errorCode;
    private boolean isSuccessful;
    private String errorMessage;
    private List<DataBean> data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof DataBean)){
                return false;
            }
            DataBean dataBean = (DataBean) obj;
            return id.equals(dataBean.id);
        }

        /**
         * currentTurnNumber : 0
         * turnCount : 3
         * startTime : 2019-10-26 10:47:06
         * createMerchantName : 李内部测试
         * state : 1
         * configurationObj : {"allowedDeviceCategory":"All","allowedLaneCategory":"All","scoreCategory":"New","entryPassword":"123","needAllBowlerDoneToNextTurn":true,"turnCount":3}
         */


        private String id;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private int currentTurnNumber;
        private int turnCount;
        private String startTime;
        private String createMerchantName;
        private int state;
        private ConfigurationObjBean configurationObj;

        public int getCurrentTurnNumber() {
            return currentTurnNumber;
        }

        public void setCurrentTurnNumber(int currentTurnNumber) {
            this.currentTurnNumber = currentTurnNumber;
        }

        public int getTurnCount() {
            return turnCount;
        }

        public void setTurnCount(int turnCount) {
            this.turnCount = turnCount;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getCreateMerchantName() {
            return createMerchantName;
        }

        public void setCreateMerchantName(String createMerchantName) {
            this.createMerchantName = createMerchantName;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public ConfigurationObjBean getConfigurationObj() {
            return configurationObj;
        }

        public void setConfigurationObj(ConfigurationObjBean configurationObj) {
            this.configurationObj = configurationObj;
        }

        public static class ConfigurationObjBean {
            /**
             * allowedDeviceCategory : All
             * allowedLaneCategory : All
             * scoreCategory : New
             * entryPassword : 123
             * needAllBowlerDoneToNextTurn : true
             * turnCount : 3
             */

            private String allowedDeviceCategory;
            private String allowedLaneCategory;
            private String scoreCategory;
            private String entryPassword;

            public boolean isAllowWatch() {
                return allowWatch;
            }

            public void setAllowWatch(boolean allowWatch) {
                this.allowWatch = allowWatch;
            }

            private boolean needAllBowlerDoneToNextTurn;
            private boolean allowWatch;
            private int turnCount;

            public String getAllowedDeviceCategory() {
                return allowedDeviceCategory;
            }

            public void setAllowedDeviceCategory(String allowedDeviceCategory) {
                this.allowedDeviceCategory = allowedDeviceCategory;
            }

            public String getAllowedLaneCategory() {
                return allowedLaneCategory;
            }

            public void setAllowedLaneCategory(String allowedLaneCategory) {
                this.allowedLaneCategory = allowedLaneCategory;
            }

            public String getScoreCategory() {
                return scoreCategory;
            }

            public void setScoreCategory(String scoreCategory) {
                this.scoreCategory = scoreCategory;
            }

            public String getEntryPassword() {
                return entryPassword;
            }

            public void setEntryPassword(String entryPassword) {
                this.entryPassword = entryPassword;
            }

            public boolean isNeedAllBowlerDoneToNextTurn() {
                return needAllBowlerDoneToNextTurn;
            }

            public void setNeedAllBowlerDoneToNextTurn(boolean needAllBowlerDoneToNextTurn) {
                this.needAllBowlerDoneToNextTurn = needAllBowlerDoneToNextTurn;
            }

            public int getTurnCount() {
                return turnCount;
            }

            public void setTurnCount(int turnCount) {
                this.turnCount = turnCount;
            }
        }
    }
}

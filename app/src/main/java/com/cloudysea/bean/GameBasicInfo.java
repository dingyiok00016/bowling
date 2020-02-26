package com.cloudysea.bean;

import java.io.Serializable;

/**
 * @author roof 2019/10/24.
 * @email lyj@yhcs.com
 * @detail
 */
public class GameBasicInfo implements Serializable{

    /**
     * data : {"currentTurnNumber":0,"turnCount":3,"startTime":"2019-10-27 17:06:34","createMerchantName":null,"state":1,"configurationObj":{"allowedDeviceCategory":"All","allowedLaneCategory":"All","scoreCategory":"New","entryPassword":"123","needAllBowlerDoneToNextTurn":true,"turnCount":3},"id":"16ac6efd-d4d5-479e-ac99-b29f7ec334ce"}
     * errorCode : 0
     * isSuccessful : true
     * errorMessage :
     */

    private DataBean data;
    private int errorCode;
    private boolean isSuccessful;
    private String errorMessage;
    private String onLineId;

    public String getOnLineId() {
        return onLineId;
    }

    public void setOnLineId(String onLineId) {
        this.onLineId = onLineId;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean implements Serializable {
        /**
         * currentTurnNumber : 0
         * turnCount : 3
         * startTime : 2019-10-27 17:06:34
         * createMerchantName : null
         * state : 1
         * configurationObj : {"allowedDeviceCategory":"All","allowedLaneCategory":"All","scoreCategory":"New","entryPassword":"123","needAllBowlerDoneToNextTurn":true,"turnCount":3}
         * id : 16ac6efd-d4d5-479e-ac99-b29f7ec334ce
         */

        private int currentTurnNumber;
        private int turnCount;
        private String startTime;
        private Object createMerchantName;
        private int state;
        private ConfigurationObjBean configurationObj;
        private String id;

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

        public Object getCreateMerchantName() {
            return createMerchantName;
        }

        public void setCreateMerchantName(Object createMerchantName) {
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public static class ConfigurationObjBean implements Serializable {
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
            private boolean needAllBowlerDoneToNextTurn;
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

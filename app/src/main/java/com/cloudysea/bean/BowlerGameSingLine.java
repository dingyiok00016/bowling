package com.cloudysea.bean;

import java.util.List;

/**
 * @author roof 2019/10/26.
 * @email lyj@yhcs.com
 * @detail
 */
public class BowlerGameSingLine {

    /**
     * data : {"id":"82ab9e4f-a205-4648-96cd-07bd83fe169f","onlineGameId":"d1d3457b-97df-4c52-8eda-ba6253f6882b","cloudBowlerId":null,"localBowlerId":"463cb6f9-a420-4c87-99f9-f6a2b2857a94","bowlerScoreID":"c373cf28-6e2b-4d56-8120-55bcbd3db012","turnNumber":1,"numberInTurn":2,"score":{"scores":[1,0,1,0,1,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0],"roundTotalScores":[30,60,90,120,150,180,null,null,null,null],"isOldScore":false,"totalScore":180,"scoreSpread":0},"replayInfos":[]}
     * errorCode : 0
     * isSuccessful : true
     * errorMessage :
     */

    private DataBean data;
    private int errorCode;
    private boolean isSuccessful;
    private String errorMessage;

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

    public static class DataBean {
        /**
         * id : 82ab9e4f-a205-4648-96cd-07bd83fe169f
         * onlineGameId : d1d3457b-97df-4c52-8eda-ba6253f6882b
         * cloudBowlerId : null
         * localBowlerId : 463cb6f9-a420-4c87-99f9-f6a2b2857a94
         * bowlerScoreID : c373cf28-6e2b-4d56-8120-55bcbd3db012
         * turnNumber : 1
         * numberInTurn : 2
         * score : {"scores":[1,0,1,0,1,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0],"roundTotalScores":[30,60,90,120,150,180,null,null,null,null],"isOldScore":false,"totalScore":180,"scoreSpread":0}
         * replayInfos : []
         */

        private String id;
        private String onlineGameId;
        private Object cloudBowlerId;
        private String localBowlerId;
        private String bowlerScoreID;
        private int turnNumber;
        private int numberInTurn;
        private ScoreBean score;
        private List<?> replayInfos;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOnlineGameId() {
            return onlineGameId;
        }

        public void setOnlineGameId(String onlineGameId) {
            this.onlineGameId = onlineGameId;
        }

        public Object getCloudBowlerId() {
            return cloudBowlerId;
        }

        public void setCloudBowlerId(Object cloudBowlerId) {
            this.cloudBowlerId = cloudBowlerId;
        }

        public String getLocalBowlerId() {
            return localBowlerId;
        }

        public void setLocalBowlerId(String localBowlerId) {
            this.localBowlerId = localBowlerId;
        }

        public String getBowlerScoreID() {
            return bowlerScoreID;
        }

        public void setBowlerScoreID(String bowlerScoreID) {
            this.bowlerScoreID = bowlerScoreID;
        }

        public int getTurnNumber() {
            return turnNumber;
        }

        public void setTurnNumber(int turnNumber) {
            this.turnNumber = turnNumber;
        }

        public int getNumberInTurn() {
            return numberInTurn;
        }

        public void setNumberInTurn(int numberInTurn) {
            this.numberInTurn = numberInTurn;
        }

        public ScoreBean getScore() {
            return score;
        }

        public void setScore(ScoreBean score) {
            this.score = score;
        }

        public List<?> getReplayInfos() {
            return replayInfos;
        }

        public void setReplayInfos(List<?> replayInfos) {
            this.replayInfos = replayInfos;
        }

        public static class ScoreBean {
            /**
             * scores : [1,0,1,0,1,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0]
             * roundTotalScores : [30,60,90,120,150,180,null,null,null,null]
             * isOldScore : false
             * totalScore : 180
             * scoreSpread : 0
             */

            private boolean isOldScore;
            private int totalScore;
            private int scoreSpread;
            private List<Integer> scores;
            private List<Integer> roundTotalScores;

            public boolean isIsOldScore() {
                return isOldScore;
            }

            public void setIsOldScore(boolean isOldScore) {
                this.isOldScore = isOldScore;
            }

            public int getTotalScore() {
                return totalScore;
            }

            public void setTotalScore(int totalScore) {
                this.totalScore = totalScore;
            }

            public int getScoreSpread() {
                return scoreSpread;
            }

            public void setScoreSpread(int scoreSpread) {
                this.scoreSpread = scoreSpread;
            }

            public List<Integer> getScores() {
                return scores;
            }

            public void setScores(List<Integer> scores) {
                this.scores = scores;
            }

            public List<Integer> getRoundTotalScores() {
                return roundTotalScores;
            }

            public void setRoundTotalScores(List<Integer> roundTotalScores) {
                this.roundTotalScores = roundTotalScores;
            }
        }
    }
}

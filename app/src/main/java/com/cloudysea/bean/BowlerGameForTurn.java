package com.cloudysea.bean;

import android.support.annotation.Keep;

import java.util.List;

/**
 * @author roof 2019/10/24.
 * @email lyj@yhcs.com
 * @detail
 */
@Keep
public class BowlerGameForTurn {


    /**
     * data : {"onlineGameId":"d1d3457b-97df-4c52-8eda-ba6253f6882b","turnNumber":1,"bowlerGames":[{"id":"2d73fc78-dba4-4225-be8a-0d16cd7f0e1f","onlineGameId":"d1d3457b-97df-4c52-8eda-ba6253f6882b","cloudBowlerId":null,"localBowlerId":"06984fbb-65a1-4ded-87ec-a0f11b887389","bowlerScoreID":"256b8ad5-c68f-417e-a8f0-69a7364ff9a9","turnNumber":1,"numberInTurn":1,"score":{"scores":[1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"roundTotalScores":[30,60,null,null,null,null,null,null,null,null],"isOldScore":false,"totalScore":60,"scoreSpread":0},"replayInfos":[]},{"id":"82ab9e4f-a205-4648-96cd-07bd83fe169f","onlineGameId":"d1d3457b-97df-4c52-8eda-ba6253f6882b","cloudBowlerId":null,"localBowlerId":"463cb6f9-a420-4c87-99f9-f6a2b2857a94","bowlerScoreID":"c373cf28-6e2b-4d56-8120-55bcbd3db012","turnNumber":1,"numberInTurn":2,"score":{"scores":[1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"roundTotalScores":[30,60,null,null,null,null,null,null,null,null],"isOldScore":false,"totalScore":60,"scoreSpread":0},"replayInfos":[]}]}
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
         * onlineGameId : d1d3457b-97df-4c52-8eda-ba6253f6882b
         * turnNumber : 1
         * bowlerGames : [{"id":"2d73fc78-dba4-4225-be8a-0d16cd7f0e1f","onlineGameId":"d1d3457b-97df-4c52-8eda-ba6253f6882b","cloudBowlerId":null,"localBowlerId":"06984fbb-65a1-4ded-87ec-a0f11b887389","bowlerScoreID":"256b8ad5-c68f-417e-a8f0-69a7364ff9a9","turnNumber":1,"numberInTurn":1,"score":{"scores":[1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"roundTotalScores":[30,60,null,null,null,null,null,null,null,null],"isOldScore":false,"totalScore":60,"scoreSpread":0},"replayInfos":[]},{"id":"82ab9e4f-a205-4648-96cd-07bd83fe169f","onlineGameId":"d1d3457b-97df-4c52-8eda-ba6253f6882b","cloudBowlerId":null,"localBowlerId":"463cb6f9-a420-4c87-99f9-f6a2b2857a94","bowlerScoreID":"c373cf28-6e2b-4d56-8120-55bcbd3db012","turnNumber":1,"numberInTurn":2,"score":{"scores":[1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"roundTotalScores":[30,60,null,null,null,null,null,null,null,null],"isOldScore":false,"totalScore":60,"scoreSpread":0},"replayInfos":[]}]
         */

        private String onlineGameId;
        private int turnNumber;
        private List<BowlerGamesBean> bowlerGames;

        public String getOnlineGameId() {
            return onlineGameId;
        }

        public void setOnlineGameId(String onlineGameId) {
            this.onlineGameId = onlineGameId;
        }

        public int getTurnNumber() {
            return turnNumber;
        }

        public void setTurnNumber(int turnNumber) {
            this.turnNumber = turnNumber;
        }

        public List<BowlerGamesBean> getBowlerGames() {
            return bowlerGames;
        }

        public void setBowlerGames(List<BowlerGamesBean> bowlerGames) {
            this.bowlerGames = bowlerGames;
        }

        @Keep
        public static class BowlerGamesBean {
            /**
             * id : 2d73fc78-dba4-4225-be8a-0d16cd7f0e1f
             * onlineGameId : d1d3457b-97df-4c52-8eda-ba6253f6882b
             * cloudBowlerId : null
             * localBowlerId : 06984fbb-65a1-4ded-87ec-a0f11b887389
             * bowlerScoreID : 256b8ad5-c68f-417e-a8f0-69a7364ff9a9
             * turnNumber : 1
             * numberInTurn : 1
             * score : {"scores":[1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"roundTotalScores":[30,60,null,null,null,null,null,null,null,null],"isOldScore":false,"totalScore":60,"scoreSpread":0}
             * replayInfos : []
             */
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            private String name;
            public int position;
            private String onlineGameId;
            private Object cloudBowlerId;
            private String localBowlerId;
            private String bowlerScoreID;
            private int turnNumber;
            private int numberInTurn;
            private ScoreBean score;
            private List<ReplayInfosClass> replayInfos;
            private List<ReplayInfosClass> bowlerReplayInfos;
            public HeadPortraitInfoClass headPortraitInfo;

            public List<ReplayInfosClass> getBowlerReplayInfos() {
                return bowlerReplayInfos;
            }

            public void setBowlerReplayInfos(List<ReplayInfosClass> bowlerReplayInfos) {
                this.bowlerReplayInfos = bowlerReplayInfos;
            }

            public static class HeadPortraitInfoClass{
                public String fileUrl;
                public String md5;
            }
            public static class ReplayInfosClass{
                public int number;
                public String url;

            }

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

            public void setReplayInfos(List<ReplayInfosClass> replayInfos) {
                this.replayInfos = replayInfos;
            }

            public static class ScoreBean {
                /**
                 * scores : [1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
                 * roundTotalScores : [30,60,null,null,null,null,null,null,null,null]
                 * isOldScore : false
                 * totalScore : 60
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
}

package com.cloudysea.bean;

import java.util.List;

/**
 * @author roof 2019/10/24.
 * @email lyj@yhcs.com
 * @detail
 */
public class CurrentGameInfo {

    /**
     * onlineGameId : c5f3faf1-fdef-441a-a485-fb4546ce053f
     * turnNumber : 2
     * bowlerGames : [{"id":"5046afa6-2987-4e08-82d0-6c8f44b7aa00","onlineGameId":"76107b36-5b58-45bd-9084-25472cfaadd9","cloudBowlerId":"5ce872d1-7e7d-451d-a588-070e364de537","localBowlerId":"ac7ba4e1-c87c-4faf-97a0-97d4b8039b7c","bowlerScoreID":"c245ab81-7510-4801-8dce-60d8e4462308","turnNumber":5,"numberInTurn":6,"score":{"scores":[1,1],"roundTotalScores":[1,1],"isOldScore":true,"totalScore":2,"scoreSpread":3},"replayInfos":[{"number":1,"url":"sample string 2"},{"number":1,"url":"sample string 2"}]},{"id":"5046afa6-2987-4e08-82d0-6c8f44b7aa00","onlineGameId":"76107b36-5b58-45bd-9084-25472cfaadd9","cloudBowlerId":"5ce872d1-7e7d-451d-a588-070e364de537","localBowlerId":"ac7ba4e1-c87c-4faf-97a0-97d4b8039b7c","bowlerScoreID":"c245ab81-7510-4801-8dce-60d8e4462308","turnNumber":5,"numberInTurn":6,"score":{"scores":[1,1],"roundTotalScores":[1,1],"isOldScore":true,"totalScore":2,"scoreSpread":3},"replayInfos":[{"number":1,"url":"sample string 2"},{"number":1,"url":"sample string 2"}]}]
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

    public static class BowlerGamesBean {
        /**
         * id : 5046afa6-2987-4e08-82d0-6c8f44b7aa00
         * onlineGameId : 76107b36-5b58-45bd-9084-25472cfaadd9
         * cloudBowlerId : 5ce872d1-7e7d-451d-a588-070e364de537
         * localBowlerId : ac7ba4e1-c87c-4faf-97a0-97d4b8039b7c
         * bowlerScoreID : c245ab81-7510-4801-8dce-60d8e4462308
         * turnNumber : 5
         * numberInTurn : 6
         * score : {"scores":[1,1],"roundTotalScores":[1,1],"isOldScore":true,"totalScore":2,"scoreSpread":3}
         * replayInfos : [{"number":1,"url":"sample string 2"},{"number":1,"url":"sample string 2"}]
         */

        private String id;
        private String onlineGameId;
        private String cloudBowlerId;
        private String localBowlerId;
        private String bowlerScoreID;
        private int turnNumber;
        private int numberInTurn;
        private ScoreBean score;
        private List<ReplayInfosBean> replayInfos;

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

        public String getCloudBowlerId() {
            return cloudBowlerId;
        }

        public void setCloudBowlerId(String cloudBowlerId) {
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

        public List<ReplayInfosBean> getReplayInfos() {
            return replayInfos;
        }

        public void setReplayInfos(List<ReplayInfosBean> replayInfos) {
            this.replayInfos = replayInfos;
        }

        public static class ScoreBean {
            /**
             * scores : [1,1]
             * roundTotalScores : [1,1]
             * isOldScore : true
             * totalScore : 2
             * scoreSpread : 3
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

        public static class ReplayInfosBean {
            /**
             * number : 1
             * url : sample string 2
             */

            private int number;
            private String url;

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}

// File: src/main/java/com/xnest/recognition/dto/response/AnalyticsResponse.java
package com.xnest.recognition.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private List<TeamStat> teamStats;
    private List<KeywordStat> keywordStats;
    private EngagementStat engagementStats;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamStat {
        private String team;
        private int sentCount;
        private int receivedCount;
        
        public void incrementReceivedCount() {
            this.receivedCount++;
        }
        
        public void incrementSentCount() {
            this.sentCount++;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeywordStat {
        private String keyword;
        private int count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EngagementStat {
        private int totalRecognitions;
        private double averageRecognitionsPerUser;
        private String mostRecognized;
        private String mostActiveSender;
    }
}
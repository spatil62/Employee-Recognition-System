package com.xnest.recognition.service;

import com.xnest.recognition.dto.response.AnalyticsResponse;
import com.xnest.recognition.enums.Visibility;
import com.xnest.recognition.model.Recognition;
import com.xnest.recognition.model.User;
import com.xnest.recognition.repository.InMemoryRecognitionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private static final List<String> POSITIVE_KEYWORDS = List.of(
        "great", "awesome", "excellent", "helpful", "teamwork", 
        "amazing", "outstanding", "fantastic", "brilliant", "superb"
    );
    private static final int MAX_KEYWORD_TYPO_DISTANCE = 2;
    private static final int TOP_KEYWORDS_LIMIT = 10;

    private final InMemoryRecognitionRepository repository;
    private final LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

    public AnalyticsResponse getAnalytics() {
        List<Recognition> recognitions = repository.getAllRecognitions();
        List<User> users = repository.getAllUsers();

        return new AnalyticsResponse(
            calculateTeamStats(recognitions, users),
            calculateKeywordStats(recognitions),
            calculateEngagementStats(recognitions, users)
        );
    }

    public void recordRecognition(Recognition recognition) {
        // Analytics recording logic
    }

    private List<AnalyticsResponse.TeamStat> calculateTeamStats(List<Recognition> recognitions, List<User> users) {
        Map<String, String> userTeamMap = users.stream()
            .collect(Collectors.toMap(User::getId, User::getTeam));

        Map<String, AnalyticsResponse.TeamStat> teamStats = new ConcurrentHashMap<>();

        recognitions.forEach(recognition -> {
            String receiverTeam = userTeamMap.get(recognition.getReceiverId());
            teamStats.computeIfAbsent(receiverTeam, 
                t -> new AnalyticsResponse.TeamStat(t, 0, 0))
                .incrementReceivedCount();

            if (recognition.getVisibility() != Visibility.ANONYMOUS) {
                String senderTeam = userTeamMap.get(recognition.getSenderId());
                teamStats.computeIfAbsent(senderTeam, 
                    t -> new AnalyticsResponse.TeamStat(t, 0, 0))
                    .incrementSentCount();
            }
        });

        return new ArrayList<>(teamStats.values());
    }

    private List<AnalyticsResponse.KeywordStat> calculateKeywordStats(List<Recognition> recognitions) {
        Map<String, Integer> keywordMap = new ConcurrentHashMap<>();

        recognitions.stream()
            .filter(rec -> rec.getMessage() != null)
            .forEach(rec -> {
                String[] words = rec.getMessage().toLowerCase().split("\\s+");
                for (String word : words) {
                    POSITIVE_KEYWORDS.forEach(keyword -> {
                        if (levenshteinDistance.apply(word, keyword) <= MAX_KEYWORD_TYPO_DISTANCE) {
                            keywordMap.merge(keyword, 1, Integer::sum);
                        }
                    });
                }
            });

        return keywordMap.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(TOP_KEYWORDS_LIMIT)
            .map(entry -> new AnalyticsResponse.KeywordStat(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    private AnalyticsResponse.EngagementStat calculateEngagementStats(List<Recognition> recognitions, List<User> users) {
        if (recognitions.isEmpty() || users.isEmpty()) {
            return new AnalyticsResponse.EngagementStat(0, 0.0, "N/A", "N/A");
        }

        Map<String, Long> receiverCounts = recognitions.stream()
            .collect(Collectors.groupingBy(Recognition::getReceiverId, Collectors.counting()));

        Map<String, Long> senderCounts = recognitions.stream()
            .filter(rec -> rec.getVisibility() != Visibility.ANONYMOUS)
            .collect(Collectors.groupingBy(Recognition::getSenderId, Collectors.counting()));

        String mostRecognizedId = receiverCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

        String mostActiveSenderId = senderCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

        Map<String, String> userNameMap = users.stream()
            .collect(Collectors.toMap(User::getId, User::getName));

        return new AnalyticsResponse.EngagementStat(
            recognitions.size(),
            (double) recognitions.size() / users.size(),
            userNameMap.getOrDefault(mostRecognizedId, "Unknown"),
            userNameMap.getOrDefault(mostActiveSenderId, "Unknown")
        );
    }
}
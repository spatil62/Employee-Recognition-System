package com.xnest.recognition.service;

import com.xnest.recognition.dto.input.RecognitionInput;
import com.xnest.recognition.dto.response.AnalyticsResponse;
import com.xnest.recognition.enums.Visibility;
import com.xnest.recognition.exception.InvalidRecognitionException;
import com.xnest.recognition.model.Recognition;
import com.xnest.recognition.model.User;
import com.xnest.recognition.repository.InMemoryRecognitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecognitionService {

    private final InMemoryRecognitionRepository repository;
    private final NotificationService notificationService;
    private final AnalyticsService analyticsService;

    public List<Recognition> getAllRecognitions() {
        return repository.getAllRecognitions();
    }

    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

    public Recognition createRecognition(RecognitionInput input) {
        validateRecognition(input);
        
        Recognition recognition = buildRecognition(input);
        repository.save(recognition);
        
        notifyParties(recognition);
        analyticsService.recordRecognition(recognition);
        
        log.info("Recognition created from {} to {}", 
                recognition.getSender().getName(), 
                recognition.getReceiver().getName());
        
        return recognition;
    }

    public AnalyticsResponse getAnalytics() {
        return analyticsService.getAnalytics();
    }

    private Recognition buildRecognition(RecognitionInput input) {
        User sender = repository.getUserById(input.getSenderId());
        User receiver = repository.getUserById(input.getReceiverId());

        if (sender == null || receiver == null) {
            throw new InvalidRecognitionException("Sender or receiver not found");
        }

        Recognition recognition = new Recognition();
        recognition.setId(UUID.randomUUID().toString());
        recognition.setSenderId(sender.getId());
        recognition.setReceiverId(receiver.getId());
        recognition.setMessage(input.getMessage());
        recognition.setEmoji(input.getEmoji());
        recognition.setVisibility(input.getVisibility());
        recognition.setTimestamp(Instant.now());
        recognition.setSender(input.getVisibility() == Visibility.ANONYMOUS ? anonymizeUser(sender) : sender);
        recognition.setReceiver(receiver);

        return recognition;
    }

    private void notifyParties(Recognition recognition) {
        notificationService.notifyRecognition(recognition);
    }

    private void validateRecognition(RecognitionInput input) {
        if (input.getSenderId().equals(input.getReceiverId())) {
            throw new InvalidRecognitionException("Cannot recognize yourself");
        }
        
        if (input.getMessage() == null || input.getMessage().trim().length() < 5) {
            throw new InvalidRecognitionException("Message must be at least 5 characters");
        }
        
        if (input.getEmoji() == null || input.getEmoji().isEmpty()) {
            throw new InvalidRecognitionException("Emoji is required");
        }
    }

    private User anonymizeUser(User user) {
        User anonymous = new User();
        anonymous.setId("ANONYMOUS_" + user.getId());
        anonymous.setName("Anonymous");
        anonymous.setTeam("Hidden");
        anonymous.setRole("ANONYMOUS");
        return anonymous;
    }
}
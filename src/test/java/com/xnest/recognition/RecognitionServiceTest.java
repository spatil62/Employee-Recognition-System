// RecognitionServiceTest.java (updated)
package com.xnest.recognition;

import com.xnest.recognition.dto.input.RecognitionInput;
import com.xnest.recognition.enums.Visibility;
import com.xnest.recognition.model.Recognition;
import com.xnest.recognition.repository.InMemoryRecognitionRepository;
import com.xnest.recognition.service.AnalyticsService;
import com.xnest.recognition.service.NotificationService;
import com.xnest.recognition.service.RecognitionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecognitionServiceTest {

    private RecognitionService recognitionService;
    private InMemoryRecognitionRepository repository;
    private NotificationService notificationService;
    private AnalyticsService analyticsService;

    @BeforeEach
    public void setup() {
        repository = new InMemoryRecognitionRepository();
        repository.initData();  // initialize mock data

        notificationService = new NotificationService();
        analyticsService = new AnalyticsService(repository);
        recognitionService = new RecognitionService(repository, notificationService, analyticsService);
    }

    @Test
    public void testCreateRecognition() {
        RecognitionInput input = new RecognitionInput();
        input.setSenderId("1");  // Alice
        input.setReceiverId("2");  // Bob
        input.setMessage("Thanks for the help!");
        input.setEmoji("👍");
        input.setVisibility(Visibility.PUBLIC);

        Recognition created = recognitionService.createRecognition(input);

        assertNotNull(created.getId());
        assertEquals("Thanks for the help!", created.getMessage());
        assertEquals("👍", created.getEmoji());
        assertEquals(Visibility.PUBLIC, created.getVisibility());
        assertEquals("Alice", created.getSender().getName());
        assertEquals("Bob", created.getReceiver().getName());

        List<Recognition> all = recognitionService.getAllRecognitions();
        assertTrue(all.stream().anyMatch(r -> r.getId().equals(created.getId())));
    }

    @Test
    public void testAnonymousRecognition() {
        RecognitionInput input = new RecognitionInput();
        input.setSenderId("1");
        input.setReceiverId("2");
        input.setMessage("You're awesome!");
        input.setEmoji("🌟");
        input.setVisibility(Visibility.ANONYMOUS);

        Recognition created = recognitionService.createRecognition(input);

        assertEquals("Anonymous", created.getSender().getName());
    }

    @Test
public void testNotificationSubscription() {
    // Change from subscribeToRecognition() to getRecognitionPublisher()
    Flux<Recognition> flux = notificationService.getRecognitionPublisher();
    
    RecognitionInput input = new RecognitionInput();
    input.setSenderId("1");
    input.setReceiverId("2");
    input.setMessage("Real-time test");
    input.setEmoji("🚀");
    input.setVisibility(Visibility.PUBLIC);

    StepVerifier.create(flux)
            .then(() -> recognitionService.createRecognition(input))
            .expectNextMatches(recognition -> recognition.getMessage().equals("Real-time test"))
            .thenCancel()
            .verify();
}
}
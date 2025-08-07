package com.xnest.recognition.resolver;

import com.xnest.recognition.model.Recognition;
import com.xnest.recognition.service.NotificationService;
import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SubscriptionResolver {

    private final NotificationService notificationService;

    public SubscriptionResolver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @SubscriptionMapping
    public Publisher<Recognition> recognitionAdded() {
        return notificationService.getRecognitionPublisher();
    }
}

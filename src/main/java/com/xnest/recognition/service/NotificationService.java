// File: src/main/java/com/xnest/recognition/service/NotificationService.java
package com.xnest.recognition.service;

import com.xnest.recognition.model.Recognition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@Service
public class NotificationService {
    private final Sinks.Many<Recognition> recognitionSink = Sinks.many().multicast().onBackpressureBuffer();

    public void notifyRecognition(Recognition recognition) {
        recognitionSink.tryEmitNext(recognition);
    }

    public Flux<Recognition> getRecognitionPublisher() {
        return recognitionSink.asFlux();
    }
}
package com.xnest.recognition.resolver;

import com.xnest.recognition.model.Recognition;
import com.xnest.recognition.model.User;
import com.xnest.recognition.dto.response.AnalyticsResponse;
import com.xnest.recognition.service.RecognitionService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class QueryResolver {

    private final RecognitionService recognitionService;

    public QueryResolver(RecognitionService recognitionService) {
        this.recognitionService = recognitionService;
    }

    @QueryMapping
    public List<Recognition> recognitions() {
        return recognitionService.getAllRecognitions();
    }

    @QueryMapping
    public List<User> users() {
        return recognitionService.getAllUsers();
    }

    @QueryMapping
    public AnalyticsResponse analytics() {
        return recognitionService.getAnalytics();
    }
}

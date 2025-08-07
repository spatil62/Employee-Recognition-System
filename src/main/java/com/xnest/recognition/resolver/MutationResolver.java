package com.xnest.recognition.resolver;

import com.xnest.recognition.dto.input.RecognitionInput;
import com.xnest.recognition.model.Recognition;
import com.xnest.recognition.service.RecognitionService;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

@Controller
public class MutationResolver {

    private final RecognitionService recognitionService;

    public MutationResolver(RecognitionService recognitionService) {
        this.recognitionService = recognitionService;
    }

    @MutationMapping
    public Recognition createRecognition(@Argument RecognitionInput input) {
        return recognitionService.createRecognition(input);
    }
}

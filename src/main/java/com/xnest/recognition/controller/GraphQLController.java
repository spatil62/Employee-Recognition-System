package com.xnest.recognition.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GraphQLController {
    @GetMapping("/health")
    public String healthCheck() {
        return "Employee Recognition API is running!";
    }
}

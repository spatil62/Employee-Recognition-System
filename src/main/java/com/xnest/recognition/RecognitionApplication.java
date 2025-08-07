package com.xnest.recognition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan; // Add this import

@SpringBootApplication
@ServletComponentScan // This should now work
public class RecognitionApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecognitionApplication.class, args);
    }
}
// File: src/main/java/com/xnest/recognition/model/Recognition.java
package com.xnest.recognition.model;

import com.xnest.recognition.enums.Visibility;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recognition {
    private String id;
    private String senderId;
    private String receiverId;
    private String message;
    private String emoji;
    private Visibility visibility;
    private Instant timestamp;
    private User sender;
    private User receiver;
}
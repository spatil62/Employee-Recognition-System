package com.xnest.recognition.dto.input;

import com.xnest.recognition.enums.Visibility;
import lombok.Data;

@Data
public class RecognitionInput {
    private String senderId;
    private String receiverId;
    private String message;
    private String emoji;
    private Visibility visibility;
    
    // Explicit getters
    public String getSenderId() {
        return senderId;
    }
    
    public String getReceiverId() {
        return receiverId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getEmoji() {
        return emoji;
    }
    
    public Visibility getVisibility() {
        return visibility;
    }
}
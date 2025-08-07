// File: src/main/java/com/xnest/recognition/model/User.java
package com.xnest.recognition.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String name;
    private String team;
    private String role;
}
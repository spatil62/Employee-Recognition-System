package com.xnest.recognition.repository;

import com.xnest.recognition.model.Recognition;
import com.xnest.recognition.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import com.xnest.recognition.enums.Visibility;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryRecognitionRepository {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Recognition> recognitions = new HashMap<>();

    public List<Recognition> findAll() {
        return new ArrayList<>(recognitions.values());
    }

    public Recognition save(Recognition recognition) {
        if (recognition.getId() == null) {
            recognition.setId(java.util.UUID.randomUUID().toString());
        }
        recognitions.put(recognition.getId(), recognition);
        return recognition;
    }

    public List<Recognition> getAllRecognitions() {
        return new ArrayList<>(recognitions.values());
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(String id) {
        return users.get(id);
    }

    @PostConstruct
    public void initData() {
        this.users.clear();
        this.recognitions.clear();

        User alice = new User();
        alice.setId("1");
        alice.setName("Alice");
        alice.setTeam("Engineering");
        alice.setRole("Developer");

        User bob = new User();
        bob.setId("2");
        bob.setName("Bob");
        bob.setTeam("Marketing");
        bob.setRole("Manager");

        this.users.put(alice.getId(), alice);
        this.users.put(bob.getId(), bob);

        Recognition r1 = new Recognition();
        r1.setId("101");
        r1.setSenderId("1");
        r1.setReceiverId("2");
        r1.setMessage("Great job, Bob!");
        r1.setEmoji("👍");
        r1.setVisibility(Visibility.PUBLIC);
        r1.setTimestamp(Instant.now());
        r1.setSender(alice);
        r1.setReceiver(bob);

        this.recognitions.put(r1.getId(), r1);
    }
}
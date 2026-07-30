package org.example.service.generator;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class IdGenerator {
    private final AtomicLong userId = new AtomicLong();
    private final AtomicLong trainingId = new AtomicLong();

    public long nextUserId(){
        return userId.incrementAndGet();
    }

    public long nextTrainingId() {
        return trainingId.incrementAndGet();
    }
}

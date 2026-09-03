package org.example.initializer;

import org.example.dao.TrainingTypeDao;
import org.example.model.TrainingType;
import org.example.model.TrainingTypeName;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TrainingTypeInitializer {
    private final TrainingTypeDao trainingTypeDao;

    public TrainingTypeInitializer(TrainingTypeDao trainingTypeDao) {
        this.trainingTypeDao = trainingTypeDao;
    }

    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void initializeTrainingTypes() {
        for (TrainingTypeName name : TrainingTypeName.values()) {

            if (trainingTypeDao.findByName(name).isEmpty()) {
                trainingTypeDao.save(new TrainingType(name));
            }
        }
    }
}
